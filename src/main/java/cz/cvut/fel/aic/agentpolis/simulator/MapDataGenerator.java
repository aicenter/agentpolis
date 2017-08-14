/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simulator;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO add support for other modes of transport
 * @author fido
 */
public class MapDataGenerator {
   
    /**
     * init map
     *
     * @param graphs
     * @return map data with simulation graph
     */
    public static MapData getMap(Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphs) {

        Map<Integer, SimulationNode> nodes = createAllGraphNodes(graphs);

//        LOGGER.info("Graphs imported, highway graph details: " + graphs.get(EGraphType.HIGHWAY));
        return new MapData(graphs, nodes);
    }
    
    /**
     * Build map data
     */
    private static Map<Integer, SimulationNode> createAllGraphNodes(
            Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByGraphType) {

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
