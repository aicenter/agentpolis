/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.ConnectionEvent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.ShapeUtils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.EventHandler;
import cz.cvut.fel.aic.geographtools.Graph;

import java.security.ProviderException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;

@Singleton
public class CongestionModel {
    private final SimulationProvider simulationProvider;
    final TimeProvider timeProvider;

    private final Graph<SimulationNode, SimulationEdge> graph;
    private final AgentpolisConfig config;

    protected final Map<SimulationNode, Connection> connectionsMappedByNodes = new HashMap<>();
    protected final Map<SimulationEdge, Link> linksMappedByEdges = new HashMap<>();
    protected final Map<Integer, SimulationEdge> edgesMappedById = new HashMap<>();

    private final List<Link> links = new LinkedList<>();

    private final Random random;
    final boolean addFundamentalDiagramDelay;
    private LaneCongestionModel laneCongestionModel;


    @Inject
    public CongestionModel(TransportNetworks transportNetworks, AgentpolisConfig config,
                           SimulationProvider simulationProvider, TimeProvider timeProvider, ShapeUtils shapeUtils, LaneCongestionModel laneCongestionModel)
            throws ModelConstructionFailedException, ProviderException {
        this.graph = transportNetworks.getGraph(EGraphType.HIGHWAY);
        this.config = config;
        this.simulationProvider = simulationProvider;
        this.timeProvider = timeProvider;
        this.laneCongestionModel = laneCongestionModel;

        random = new Random(config.congestionModel.randomSeed);
        addFundamentalDiagramDelay = config.congestionModel.fundamentalDiagramDelay;
    }

    @Inject
    public void buildCongestionGraph() throws ModelConstructionFailedException {
        buildEdges(graph.getAllEdges());
        buildConnections(graph.getAllNodes());
        buildLinks(graph.getAllEdges());
        buildLanes();

        initCrossroads();
    }

    public void drive(PhysicalVehicle vehicle, Trip<SimulationNode> trip) {
        VehicleTripData vehicleData = new VehicleTripData(vehicle, trip);
        SimulationNode startLocation = trip.getAndRemoveFirstLocation();
        Connection startConnection = connectionsMappedByNodes.get(startLocation);
        startConnection.startDriving(vehicleData);
    }

    public List<Link> getLinks() {
        return links;
    }

    public void makeScheduledEvent(Object caller, EventHandler target, long delay) {
        Log.info(this, "adding scheduled event: delay = " + delay + "source:" + caller + "target =" + target);
        simulationProvider.getSimulation().addEvent(ConnectionEvent.SCHEDULED_EVENT, target, null, null, delay != 0 ? delay : 1);
    }

    public long computeTransferDelay(VehicleTripData vehicleTripData, CongestionLane toLane) {
        return laneCongestionModel.computeTransferDelay(vehicleTripData, toLane);
    }

    public long computeArrivalDelay(VehicleTripData vehicleTripData) {
        long arrivalDelay = 0;
        DelayData delayData = vehicleTripData.getVehicle().getDelayData();
        if (delayData != null) {
            long currentSimTime = timeProvider.getCurrentSimTime();
            long arrivalExpectedTime = delayData.getDelayStartTime() + delayData.getDelay();
            arrivalDelay = Math.max(0, arrivalExpectedTime - currentSimTime);
        }
        return arrivalDelay;
    }

    /**
     * Wakeup connection
     *
     * @param connection - to be waked up
     * @param delay      - positive number
     */
    public void wakeUpConnection(Connection connection, long delay) {
        makeTickEvent(this, connection, delay);
    }

    /**
     * Tick
     *
     * @param target
     * @param delay
     */
    public void makeTickEvent(Object caller, EventHandler target, long delay) {
        Log.info(this, "adding tick: delay = " + delay + "source:" + caller + "target =" + target);
        simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, target, null, null, delay != 0 ? delay : 1);
    }

    long computeDelayAndSetVehicleData(VehicleTripData vehicleData, CongestionLane nextCongestionLane) {
        Link nextLink = nextCongestionLane.parentLink;

        // for visio
        Driver driver = vehicleData.getVehicle().getDriver();
        PhysicalVehicle vehicle = vehicleData.getVehicle();

        vehicle.setPosition(nextLink.fromNode);
        vehicle.setQueueBeforeVehicleLength(nextCongestionLane.getUsedLaneCapacityInMeters() - vehicle.getLength());

        double distance = nextLink.edge.shape.getShapeLength() - vehicle.getQueueBeforeVehicleLength();

        long delay = nextCongestionLane.computeDelay(vehicleData.getVehicle(), distance);
        DelayData delayData = new DelayData(delay, getTimeProvider().getCurrentSimTime(), distance);

        driver.setTargetNode(nextLink.toNode);
        driver.setDelayData(delayData);

        return delayData.getDelay();
    }

    static long computeFreeflowTransferDelay(PhysicalVehicle vehicle) {
        return Math.round(vehicle.getLength() * 1E3 / vehicle.getVelocity());
    }

    Random getRandom() {
        return random;
    }

    TimeProvider getTimeProvider() {
        return timeProvider;
    }

    /**
     * Build connection or crossroad
     *
     * @param allNodes
     */
    private void buildConnections(Collection<SimulationNode> allNodes) {
        for (SimulationNode node : allNodes) {
            if (graph.getOutEdges(node).size() > 1 || graph.getInEdges(node).size() > 1) {
                Crossroad crossroad = new Crossroad(config, simulationProvider, this, node, timeProvider);
                connectionsMappedByNodes.put(node, crossroad);
            } else {
                Connection connection = new Connection(simulationProvider, config, this, node);
                connectionsMappedByNodes.put(node, connection);
            }
        }
    }

    private void buildEdges(Collection<SimulationEdge> allEdges) {
        for (SimulationEdge e : allEdges) {
            edgesMappedById.put(e.getUniqueId(), e);
        }
    }

    private void initCrossroads() {
        for (Entry<SimulationNode, Connection> entry : connectionsMappedByNodes.entrySet()) {
            Connection connection = entry.getValue();
            if (connection instanceof Crossroad) {
                ((Crossroad) connection).init();
            }
        }
    }

    private void buildLinks(Collection<SimulationEdge> allEdges) {
        for (SimulationEdge edge : allEdges) {
            SimulationNode fromNode = graph.getNode(edge.fromId);
            SimulationNode toNode = graph.getNode(edge.toId);
            Link link = new Link(this, edge, fromNode, toNode,
                    connectionsMappedByNodes.get(fromNode), connectionsMappedByNodes.get(toNode));
            links.add(link);
            linksMappedByEdges.put(edge, link);
        }
    }

    private void buildLanes() throws ModelConstructionFailedException {
        for (Link link : links) {
            SimulationNode toNode = graph.getNode(link.getEdge().toId);

            // outgoing edges from toNode
            List<SimulationEdge> allFollowingEdges = graph.getOutEdges(toNode); // following edges
            List<Lane> lanes = link.getEdge().getLanes();

            //dead end test
            if (allFollowingEdges.isEmpty()) {
                throw new ModelConstructionFailedException("Dead end detected - this is prohibited in road graph");
            }

            Connection toConnection = connectionsMappedByNodes.get(toNode);

            if(lanes != null) {
                for (Lane lane : lanes) {
                    Set<Integer> followingEdgeId = lane.getAvailableEdges();
                    List<SimulationNode> followingNodes = new LinkedList<>();

                    // in case of all directions or unknown
                    if (!followingEdgeId.contains(-1)) {
                        for (Integer i : followingEdgeId) {
                            followingNodes.add(graph.getNode(this.edgesMappedById.get(i).toId));
                        }
                    } else {
                        for (SimulationEdge e : allFollowingEdges) {
                            followingNodes.add(graph.getNode(e.toId));
                        }
                    }

                    CongestionLane congestionLane = new CongestionLane(link, lane.getLaneUniqueId(), link.getLength(), timeProvider, simulationProvider);
                    link.addCongestionLane(congestionLane, followingNodes);

                    // Build info for connection
                    for (SimulationNode e : followingNodes) { // following nodes for congestion lane
                        Link outLink = linksMappedByEdges.get(graph.getEdge(toNode.id, e.id));
                        if (toConnection instanceof Crossroad) {
                            ((Crossroad) toConnection).addNextLink(outLink, congestionLane, connectionsMappedByNodes.get(e));
                        } else {
                            toConnection.setOutLink(outLink, congestionLane);
                        }
                    }
                }
            } else{
                throw new ModelConstructionFailedException("Lanes cannot be null");
            }
        }
    }

    //    private void startQueeHandling(){
//        for (Entry<SimulationNode,Connection> entry : connectionsMappedByNodes.entrySet()) {
//            if(entry.getKey().id == 280){
//                int a = 1;
//            }
//            entry.getValue().start();
//        }
//        queueHandlingStarted = true;
//    }
}
