package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.init;

import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import cz.cvut.fel.aic.geographtools.Graph;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;


public abstract class MapInitializer {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MapInitializer.class);

    protected final AgentpolisConfig config;


    
    public MapInitializer(AgentpolisConfig config) {
        this.config = config;
    }


    /**
     * init map
     *
     * @return map data with simulation graph
     */
    public MapData getMap() {
        Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphs = new HashMap<>();
        graphs.put(EGraphType.HIGHWAY, getGraph());

        Map<Integer, SimulationNode> nodes = createAllGraphNodes(graphs);

        LOGGER.info("Graphs imported, highway graph details: {}", graphs.get(EGraphType.HIGHWAY));
        return new MapData(graphs, nodes);
    }
	
	protected abstract Graph<SimulationNode, SimulationEdge> getGraph();

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
