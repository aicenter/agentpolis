package cz.agents.agentpolis.simulator.creator.initializator.impl;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simulator.visualization.visio.Bounds;
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

    public final Bounds bounds;
    public final Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType;
    public final Map<Integer,SimulationNode> nodesFromAllGraphs;

    public MapData(Bounds bounds, Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType,
            Map<Integer, SimulationNode> nodesFromAllGraphs) {
        super();
        this.bounds = bounds;
        this.graphByType = graphByType;
        this.nodesFromAllGraphs = nodesFromAllGraphs;
    }

}
