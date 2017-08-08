package cz.cvut.fel.aic.agentpolis.simulator.creator.initializator.impl;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;

import java.util.Map;

/**
 * The wrapper of data created by {@code MapInitFactory}
 * 
 * @author Zbynek Moler
 * 
 */
public class MapData {

    public final Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType;
    public final Map<Integer,SimulationNode> nodesFromAllGraphs;

    public MapData(Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType,
            Map<Integer, SimulationNode> nodesFromAllGraphs) {
        super();
        this.graphByType = graphByType;
        this.nodesFromAllGraphs = nodesFromAllGraphs;
    }

}
