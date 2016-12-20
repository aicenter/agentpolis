package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder.osm.OsmGraphBuilderExtended;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder.structurebuilders.edge.RoadEdgeExtendedBuilder;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.RoadEdgeExtended;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.RoadNodeExtended;
import cz.agents.basestructures.Graph;
import cz.agents.geotools.StronglyConnectedComponentsFinder;
import cz.agents.geotools.Transformer;
import cz.agents.gtdgraphimporter.structurebuilders.TmpGraphBuilder;
import cz.agents.gtdgraphimporter.structurebuilders.edge.EdgeBuilder;
import cz.agents.multimodalstructures.additional.ModeOfTransport;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;

/**
 * Instead of {@link cz.agents.gtdgraphimporter.GTDGraphBuilder}
 * Lighter version of it. Preparation for RoadEdgeExtended
 *
 * @author Zdenek Bousa
 */
public class RoadNetworkGraphBuilder {
    private static final Logger LOGGER = Logger.getLogger(RoadNetworkGraphBuilder.class);

    private final Transformer projection;

    /**
     * Modes to be loaded from OSM.
     */
    private final Set<ModeOfTransport> allowedOsmModes;

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
     * @param projection      SRID
     * @param osmFile         file with OSM map
     * @param allowedOsmModes based on {@link RoadNetworkGraphBuilder#OSM_MODES}
     */
    public RoadNetworkGraphBuilder(Transformer projection, File osmFile, Set<ModeOfTransport> allowedOsmModes) {
        this.projection = projection;
        this.allowedOsmModes = allowedOsmModes;
        this.osmBuilderBuilderExtended = new OsmGraphBuilderExtended.Builder(osmFile, projection, allowedOsmModes);
    }

    /**
     * Construct road graph
     *
     * @return Graph that has one main strong component and might have been simplified (impact on visio - more sharp curves)
     */
    public Graph<RoadNodeExtended, RoadEdgeExtended> build() {
        TmpGraphBuilder<RoadNodeExtended, RoadEdgeExtended> osmGraph = buildOsmGraphExtended();
        //TODO: Simplifier: Make switch for visio graph(visio and curves) // computation graph
        //RoadGraphSimplifier.simplify(osmGraph, Collections.emptySet()); //not working for RoadExtended
        return osmGraph.createGraph();
    }

    /**
     * Build temporary graph and apply minor components reduction.
     *
     * @return Full road graph with only one strong component.
     */
    private TmpGraphBuilder<RoadNodeExtended, RoadEdgeExtended> buildOsmGraphExtended() {
        TmpGraphBuilder<RoadNodeExtended, RoadEdgeExtended> osmGraph = osmBuilderBuilderExtended.build().readOsmAndGetGraphBuilder();
        removeMinorComponents(osmGraph);
        return osmGraph;
    }

    /**
     * Removes from the {@code osmGraph} all nodes and edges that are not in the main component for any mode.
     *
     * @param osmGraph osm graph with multiple strong components
     */
    private void removeMinorComponents(TmpGraphBuilder<RoadNodeExtended, RoadEdgeExtended> osmGraph) {
        LOGGER.debug("Calculating main components for all modes...");
        SetMultimap<Integer, ModeOfTransport> modesOnNodes = HashMultimap.create();
        for (ModeOfTransport mode : allowedOsmModes) {
            Set<Integer> mainComponent = getMainComponent(osmGraph, mode);
            mainComponent.forEach(i -> modesOnNodes.put(i, mode));
        }

        Predicate<EdgeBuilder<? extends RoadEdgeExtended>> filter = edge -> {
            RoadEdgeExtendedBuilder roadEdgeExtendedBuilder = (RoadEdgeExtendedBuilder) edge;
            roadEdgeExtendedBuilder.intersectModeOfTransports(modesOnNodes.get(roadEdgeExtendedBuilder.getTmpFromId()));
            roadEdgeExtendedBuilder.intersectModeOfTransports(modesOnNodes.get(roadEdgeExtendedBuilder.getTmpToId()));
            return roadEdgeExtendedBuilder.getModeOfTransports().isEmpty();
        };
        int removedEdges = osmGraph.removeEdges(filter);
        LOGGER.debug("Removed " + removedEdges + " edges.");

        int removedNodes = osmGraph.removeIsolatedNodes();
        LOGGER.debug("Removed " + removedNodes + " nodes.");
        LOGGER.debug("Nodes by degree: ");
        osmGraph.getNodesByDegree().forEach((k, v) -> LOGGER.debug(k + "->" + v.size()));
    }

    /**
     * Main strong component
     */
    private Set<Integer> getMainComponent(TmpGraphBuilder<RoadNodeExtended, RoadEdgeExtended> graph, ModeOfTransport mode) {
        List<EdgeBuilder<? extends RoadEdgeExtended>> feasibleEdges = graph.getFeasibleEdges(mode);
        return getMainComponent(feasibleEdges);
    }

    /**
     * Find strong component by size
     */
    private Set<Integer> getMainComponent(Collection<EdgeBuilder<? extends RoadEdgeExtended>> edges) {
        Set<Integer> nodeIds = new HashSet<>();
        Map<Integer, Set<Integer>> edgeIds = new HashMap<>();
        for (EdgeBuilder<? extends RoadEdgeExtended> edgeExtendedBuilder : edges) {
            int fromId = edgeExtendedBuilder.getTmpFromId();
            int toId = edgeExtendedBuilder.getTmpToId();
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
}
