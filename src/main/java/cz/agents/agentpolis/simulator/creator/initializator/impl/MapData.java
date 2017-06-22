package cz.agents.agentpolis.simulator.creator.initializator.impl;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.BoundingBox;
import cz.agents.basestructures.Graph;

import java.util.Map;

/**
 * The wrapper of data created by {@code MapInitFactory}
 * 
 * @author Zbynek Moler
 * 
 */
public class MapData {

    public final BoundingBox bounds;
    public final Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType;
    public final Map<Integer,SimulationNode> nodesFromAllGraphs;

    public MapData(BoundingBox bounds, Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType,
            Map<Integer, SimulationNode> nodesFromAllGraphs) {
        super();
        this.bounds = bounds;
        this.graphByType = graphByType;
        this.nodesFromAllGraphs = nodesFromAllGraphs;
    }

}
