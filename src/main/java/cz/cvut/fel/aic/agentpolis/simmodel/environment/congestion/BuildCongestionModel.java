package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.Connection;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.ConnectionTwoWay;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.Crossroad;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.lanes.CongestionLane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.support.ModelConstructionFailedException;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

import java.util.*;

/**
 * @author Zdenek Bousa
 */
class BuildCongestionModel {
    private CongestionModel model;

    BuildCongestionModel(CongestionModel model){
        this.model = model;
    }

    void build() throws ModelConstructionFailedException{
        buildEdges(model.graph.getAllEdges());
        buildConnections(model.graph.getAllNodes());
        buildLinks(model.graph.getAllEdges());
        buildLanes();

        initCrossroads();
    }
    //
    // ========================= Private Build ======================
    //
    private void buildEdges(Collection<SimulationEdge> allEdges) {
        for (SimulationEdge e : allEdges) {
            model.edgesMappedById.put(e.getUniqueId(), e);
        }
    }

    private void buildConnections(Collection<SimulationNode> allNodes) {
        for (SimulationNode node : allNodes) {

            if (model.graph.getOutEdges(node).size() > 1 || model.graph.getInEdges(node).size() > 1) {
                //
                List<SimulationEdge> outEdges = model.graph.getOutEdges(node);
                List<SimulationEdge> inEdges = model.graph.getInEdges(node);
                if (isTwoWayConnection(outEdges, inEdges)) {
                    ConnectionTwoWay connectionTwoWay = new ConnectionTwoWay(model.simulationProvider, model.config, model, node);
                    model.connectionsMappedByNodes.put(node, connectionTwoWay);
                } else {
                    Crossroad crossroad = new Crossroad(model.config, model.simulationProvider, model, node, model.timeProvider);
                    model.connectionsMappedByNodes.put(node, crossroad);
                }
            } else {
                Connection connection = new Connection(model.simulationProvider, model.config, model, node);
                model.connectionsMappedByNodes.put(node, connection);
            }
        }
    }

    private void buildLinks(Collection<SimulationEdge> allEdges) {
        for (SimulationEdge edge : allEdges) {
            SimulationNode fromNode = model.graph.getNode(edge.fromId);
            SimulationNode toNode = model.graph.getNode(edge.toId);
            Link link = new Link(model, edge, fromNode, toNode, model.connectionsMappedByNodes.get(fromNode), model.connectionsMappedByNodes.get(toNode));
            model.links.add(link);
            model.linksMappedByEdges.put(edge, link);
        }
    }

    private void buildLanes() throws ModelConstructionFailedException {
        for (Link link : model.links) {
            SimulationNode toNode = model.graph.getNode(link.getEdge().toId);

            // outgoing edges from toNode
            List<SimulationEdge> allFollowingEdges = model.graph.getOutEdges(toNode); // following edges
            List<Lane> lanes = link.getEdge().getLanes();

            //dead end test
            if (allFollowingEdges.isEmpty()) {
                throw new ModelConstructionFailedException("Dead end detected - this is prohibited in road graph");
            }

            Connection toConnection = model.connectionsMappedByNodes.get(toNode);

            if (lanes != null) {
                for (Lane lane : lanes) {
                    Set<Integer> followingEdgeId = lane.getAvailableEdges();
                    List<SimulationNode> followingNodes = new LinkedList<>();

                    // in case of all directions or unknown
                    if (!followingEdgeId.contains(-1)) {
                        for (Integer i : followingEdgeId) {
                            followingNodes.add(model.graph.getNode(model.edgesMappedById.get(i).toId));
                        }
                    } else {
                        for (SimulationEdge e : allFollowingEdges) {
                            followingNodes.add(model.graph.getNode(e.toId));
                        }
                    }

                    CongestionLane congestionLane = new CongestionLane(link, lane.getLaneUniqueId(), link.getLength(), model.timeProvider, model.simulationProvider);
                    link.addCongestionLane(congestionLane, followingNodes);
                    model.congestionLanes.add(congestionLane);

                    // Build info for connection
                    for (SimulationNode e : followingNodes) { // following nodes for congestion lane
                        Link outLink = model.linksMappedByEdges.get(model.graph.getEdge(toNode.id, e.id));
                        if (toConnection instanceof Crossroad) {
                            ((Crossroad) toConnection).addNextLink(outLink, congestionLane, model.connectionsMappedByNodes.get(e));
                        } else {
                            toConnection.setOutLink(outLink, congestionLane);
                        }
                    }
                }
            } else {
                throw new ModelConstructionFailedException("Lanes cannot be null");
            }
        }
    }

    private boolean isTwoWayConnection(List<SimulationEdge> outEdges, List<SimulationEdge> inEdges) {
        return (outEdges.get(0).toId == inEdges.get(0).fromId && outEdges.get(1).toId == inEdges.get(1).fromId) ||
                (outEdges.get(0).toId == inEdges.get(1).fromId && outEdges.get(1).toId == inEdges.get(0).fromId);
    }

    private void initCrossroads() {
        for (Map.Entry<SimulationNode, Connection> entry : model.connectionsMappedByNodes.entrySet()) {
            Connection connection = entry.getValue();
            if (connection instanceof Crossroad) {
                ((Crossroad) connection).init();
            }
        }
    }
}
