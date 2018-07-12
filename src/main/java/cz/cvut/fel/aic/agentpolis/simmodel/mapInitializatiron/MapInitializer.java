package cz.cvut.fel.aic.agentpolis.simmodel.mapInitializatiron;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import cz.cvut.fel.aic.agentpolis.simmodel.mapInitializatiron.SimulationEdgeFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.mapInitializatiron.SimulationNodeFactory;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.TransportMode;
import cz.cvut.fel.aic.geographtools.util.Transformer;
import cz.cvut.fel.aic.graphimporter.GraphCreator;
import cz.cvut.fel.aic.graphimporter.geojson.GeoJSONReader;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class MapInitializer {

    private static final Logger LOGGER = Logger.getLogger(MapInitializer.class);

    private final File mapFile;

    private final Transformer projection;

    private final Set<TransportMode> allowedOsmModes;

    private final AgentpolisConfig config;


    @Inject
    public MapInitializer(Transformer projection, @Named("osm File") File mapFile, Set<TransportMode> allowedOsmModes, AgentpolisConfig config) {
        this.mapFile = mapFile;
        this.projection = projection;
        this.allowedOsmModes = allowedOsmModes;
        this.config = config;
    }


    /**
     * init map
     *
     * @return map data with simulation graph
     */
    public MapData getMap() {
        Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphs = new HashMap<>();
//        OsmImporter importer = new OsmImporter(mapFile, allowedOsmModes, projection); // OSM importer is not used yet
        String nodeFile = config.pathToNodes;
        String edgeFile = config.pathToEdges;
        String serializedGraphFile = config.pathToSerializedGraph;
        GeoJSONReader importer = new GeoJSONReader(edgeFile, nodeFile, serializedGraphFile, projection);

        GraphCreator<SimulationNode, SimulationEdge> graphCreator = new GraphCreator(
                true, true, importer, new SimulationNodeFactory(), new SimulationEdgeFactory());

        graphs.put(EGraphType.HIGHWAY, graphCreator.getMap());

        Map<Integer, SimulationNode> nodes = createAllGraphNodes(graphs);

        LOGGER.info("Graphs imported, highway graph details: " + graphs.get(EGraphType.HIGHWAY));
        return new MapData(graphs, nodes);
    }

    /**
     * Build map data
     */
    private Map<Integer, SimulationNode> createAllGraphNodes(Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByGraphType) {

        Map<Integer, SimulationNode> nodesFromAllGraphs = new HashMap<>();

        for (GraphType graphType : graphByGraphType.keySet()) {
            Graph<SimulationNode, SimulationEdge> graphStorageTmp = graphByGraphType.get(graphType);
            for (SimulationNode node : graphStorageTmp.getAllNodes()) {
                nodesFromAllGraphs.put(node.getId(), node);
            }

        }

        return nodesFromAllGraphs;

    }
}
