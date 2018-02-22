package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection;

import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CongestionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.Link;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.lanes.CongestionLane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Zdenek Bousa
 */
public class ConnectionTwoWay extends Connection {
    private Map<SimulationNode,List<CongestionLane>> inCongestionLanesMappedByNode = new HashMap<>();
    private Map<SimulationNode,Link> linksMappedByNode = new HashMap<>();


    public ConnectionTwoWay(SimulationProvider simulationProvider, AgentpolisConfig config, CongestionModel congestionModel, SimulationNode node) {
        super(simulationProvider, config, congestionModel, node);
    }

    //
    // ========================= Build ======================
    //
    @Override
    public Link getNextLink(Connection nextConnection) {
        return linksMappedByNode.get(nextConnection.node);
    }

    @Override
    public Link getNextLink(CongestionLane inputCongestionLane) {
        return linksMappedByNode.get(inputCongestionLane.parentLink.toNode);
    }

    @Override
    public void setOutLink(Link outLink, CongestionLane inCongestionLane) {
        if (!linksMappedByNode.values().contains(outLink)){
            linksMappedByNode.put(outLink.toNode,outLink);
            inCongestionLanesMappedByNode.put(outLink.toNode,new LinkedList<>());
        }
        inCongestionLanesMappedByNode.get(outLink.toNode).add(inCongestionLane);
    }
}
