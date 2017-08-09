package cz.cvut.fel.aic.agentpolis.simulator;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
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
