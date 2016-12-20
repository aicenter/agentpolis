package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder.osm.OsmGraphBuilderExtended;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.RoadEdgeExtended;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.RoadNodeExtended;
import cz.agents.basestructures.Graph;
import cz.agents.geotools.StronglyConnectedComponentsFinder;
import cz.agents.geotools.Transformer;
import cz.agents.gtdgraphimporter.osm.OsmGraphBuilder;
import cz.agents.gtdgraphimporter.structurebuilders.TmpGraphBuilder;
import cz.agents.gtdgraphimporter.structurebuilders.edge.EdgeBuilder;
import cz.agents.gtdgraphimporter.structurebuilders.edge.RoadEdgeBuilder;
import cz.agents.multimodalstructures.additional.ModeOfTransport;
import cz.agents.multimodalstructures.edges.RoadEdge;
import cz.agents.multimodalstructures.nodes.RoadNode;
import org.apache.log4j.Logger;

import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;

/**
 * Instead of {@link cz.agents.gtdgraphimporter.GTDGraphBuilder}
 * Lighter version of it. Preparation for RoadEdgeExtended
 *
 * @author Zdenek Bousa
 */
public class TransportNetworkGraphBuilder {
    private static final Logger LOGGER = Logger.getLogger(TransportNetworkGraphBuilder.class);

    private final Transformer projection;

    /**
     * Modes to be loaded from OSM.
     */
    private final Set<ModeOfTransport> allowedOsmModes;

    /**
     *
     */
    private final OsmGraphBuilder.Builder osmBuilderBuilder;

    /**
     * Builder and parser OSM for RoadExtended
     */
    private final OsmGraphBuilderExtended.Builder osmBuilderBuilderExtended;

    /**
     * Set of all modes that can be loaded from OSM without any additional information required.
     */
    public static final Set<ModeOfTransport> OSM_MODES = Sets.immutableEnumSet(
            ModeOfTransport.WALK,
            ModeOfTransport.TAXI,
            ModeOfTransport.CAR,
            ModeOfTransport.MOTORCYCLE,
            ModeOfTransport.BIKE);

    /**
     * Constructor
     *
     * @param projection      -
     * @param osmFile         -
     * @param allowedOsmModes -
     */
    public TransportNetworkGraphBuilder(Transformer projection, File osmFile, Set<ModeOfTransport> allowedOsmModes) {
        this.projection = projection;
        this.allowedOsmModes = allowedOsmModes;
        //TODO: reduce builders
        this.osmBuilderBuilder = new OsmGraphBuilder.Builder(osmFile, projection, allowedOsmModes);
        this.osmBuilderBuilderExtended = new OsmGraphBuilderExtended.Builder(osmFile, projection, allowedOsmModes);
    }

    /**
     * Build Graph<RoadNode,RoadEdge>
     *
     * @return
     */
    public Graph<RoadNode, RoadEdge> buildRoadGraph() {
        TmpGraphBuilder<RoadNode, RoadEdge> osmGraph = buildOsmGraph();
        //TODO: Simplifier: Make switch for visio graph(visio and curves) // computation graph
        //RoadGraphSimplifier.simplify(osmGraph, Collections.emptySet());
        return osmGraph.createGraph();
    }

    private TmpGraphBuilder<RoadNode, RoadEdge> buildOsmGraph() {
        TmpGraphBuilder<RoadNode, RoadEdge> osmGraph = osmBuilderBuilder.build().readOsmAndGetGraphBuilder();
        removeMinorComponents(osmGraph);
        return osmGraph;
    }

    public Graph<RoadNodeExtended, RoadEdgeExtended> buildRoadExtendedGraph() {
        TmpGraphBuilder<RoadNodeExtended, RoadEdgeExtended> osmGraph = buildOsmGraphExtended();
        //TODO: Simplifier: Make switch for visio graph(visio and curves) // computation graph
        return osmGraph.createGraph();
    }

    private TmpGraphBuilder<RoadNodeExtended, RoadEdgeExtended> buildOsmGraphExtended() {
        TmpGraphBuilder<RoadNodeExtended, RoadEdgeExtended> osmGraph = osmBuilderBuilderExtended.build().readOsmAndGetGraphBuilder();
        return osmGraph;
    }

    /**
     * Removes from the {@code osmGraph} all nodes and edges that are not in the main component for any mode.
     *
     * @param osmGraph
     */
    private void removeMinorComponents(TmpGraphBuilder<RoadNode, RoadEdge> osmGraph) {
        LOGGER.debug("Calculating main components for all modes...");
        SetMultimap<Integer, ModeOfTransport> modesOnNodes = HashMultimap.create();
        for (ModeOfTransport mode : allowedOsmModes) {
            Set<Integer> mainComponent = getMainComponent(osmGraph, mode);
            mainComponent.forEach(i -> modesOnNodes.put(i, mode));
        }

        Predicate<EdgeBuilder<? extends RoadEdge>> filter = edge -> {
            RoadEdgeBuilder roadEdgeBuilder = (RoadEdgeBuilder) edge;
            roadEdgeBuilder.intersectModeOfTransports(modesOnNodes.get(roadEdgeBuilder.getTmpFromId()));
            roadEdgeBuilder.intersectModeOfTransports(modesOnNodes.get(roadEdgeBuilder.getTmpToId()));
            return roadEdgeBuilder.getModeOfTransports().isEmpty();
        };
        int removedEdges = osmGraph.removeEdges(filter);
        LOGGER.debug("Removed " + removedEdges + " edges.");

        int removedNodes = osmGraph.removeIsolatedNodes();
        LOGGER.debug("Removed " + removedNodes + " nodes.");
        LOGGER.debug("Nodes by degree: ");
        osmGraph.getNodesByDegree().forEach((k, v) -> LOGGER.debug(k + "->" + v.size()));
    }

    /**
     * @param graph
     * @param mode
     * @return
     */
    private Set<Integer> getMainComponent(TmpGraphBuilder<RoadNode, RoadEdge> graph, ModeOfTransport mode) {
        List<EdgeBuilder<? extends RoadEdge>> feasibleEdges = graph.getFeasibleEdges(mode);
        return getMainComponent(feasibleEdges);
    }

    private Set<Integer> getMainComponent(Collection<EdgeBuilder<? extends RoadEdge>> edges) {
        Set<Integer> nodeIds = new HashSet<>();
        Map<Integer, Set<Integer>> edgeIds = new HashMap<>();
        for (EdgeBuilder<? extends RoadEdge> edgeBuilder : edges) {
            int fromId = edgeBuilder.getTmpFromId();
            int toId = edgeBuilder.getTmpToId();
            nodeIds.add(fromId);
            nodeIds.add(toId);
            Set<Integer> outgoing = edgeIds.get(fromId);
            if (outgoing == null) {
                outgoing = new HashSet<>();
                edgeIds.put(fromId, outgoing);
            }
            outgoing.add(toId);
        }
        return StronglyConnectedComponentsFinder.getStronglyConnectedComponentsSortedBySize(nodeIds, edgeIds).get(0);
    }

    private LocalDate createLocalDate(java.sql.Date oldDate) {
        return oldDate.toLocalDate();
    }

    private boolean checkOneComponent(TmpGraphBuilder<RoadNode, RoadEdge> osmGraph) {
        Set<Integer> mainComponent = getMainComponent(osmGraph.getAllEdges());
        Set<Integer> graphNodeIds = osmGraph.getAllNodes().stream().map(n -> n.tmpId).collect(toSet());
        return mainComponent.equals(graphNodeIds);
    }

}
