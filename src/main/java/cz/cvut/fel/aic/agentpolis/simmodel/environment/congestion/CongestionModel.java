/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.ConnectionEvent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
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

/**
 * @author fido
 */
@Singleton
public class CongestionModel {
    final Graph<SimulationNode, SimulationEdge> graph;

    protected final Map<SimulationNode, Connection> connectionsMappedByNodes;

    private final List<Link> links;

    protected final Map<SimulationEdge, Link> linksMappedByEdges;

    private final AgentpolisConfig config;

    private final SimulationProvider simulationProvider;

    final TimeProvider timeProvider;

    private final Random random;

    final boolean addFundamentalDiagramDelay;


    private boolean queueHandlingStarted;
    private ShapeUtils shapeUtils;

    Random getRandom() {
        return random;
    }

    TimeProvider getTimeProvider() {
        return timeProvider;
    }


    @Inject
    public CongestionModel(TransportNetworks transportNetworks, AgentpolisConfig config,
                           SimulationProvider simulationProvider, TimeProvider timeProvider, ShapeUtils shapeUtils)
            throws ModelConstructionFailedException, ProviderException {
        this.graph = transportNetworks.getGraph(EGraphType.HIGHWAY);
        this.config = config;
        this.simulationProvider = simulationProvider;
        this.timeProvider = timeProvider;
        this.shapeUtils = shapeUtils;
        connectionsMappedByNodes = new HashMap<>();
        linksMappedByEdges = new HashMap<>();
        links = new LinkedList<>();
        queueHandlingStarted = false;
//        buildCongestionGraph();
        random = new Random(config.congestionModel.randomSeed);
        addFundamentalDiagramDelay = config.congestionModel.fundamentalDiagramDelay;
    }

    @Inject
    public void buildCongestionGraph() throws ModelConstructionFailedException {
        buildConnections(graph.getAllNodes());
        buildLinks(graph.getAllEdges());
        buildLanes();
        initCrossroads();
    }

    public void drive(PhysicalVehicle vehicle, Trip<SimulationNode> trip) {
//        if(!queueHandlingStarted){
//            startQueeHandling();
//        }
        VehicleTripData vehicleData = new VehicleTripData(vehicle, trip);
        SimulationNode startLocation = trip.getAndRemoveFirstLocation();
        Connection startConnection = connectionsMappedByNodes.get(startLocation);
        startConnection.startDriving(vehicleData);
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

    private void buildConnections(Collection<SimulationNode> allNodes) {
        for (SimulationNode node : allNodes) {
            if (graph.getOutEdges(node).size() > 1 || graph.getInEdges(node).size() > 1) {
                Crossroad crossroad = new Crossroad(config, simulationProvider, this, node);
                connectionsMappedByNodes.put(node, crossroad);
            } else {
                Connection connection = new Connection(simulationProvider, config, this, node);
                connectionsMappedByNodes.put(node, connection);
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
//			SimulationNode fromNode = graph.getNode(link.getEdge().fromId);
            SimulationNode toNode = graph.getNode(link.getEdge().toId);
            List<SimulationEdge> nextEdges = graph.getOutEdges(toNode);

            //dead end test
            if (nextEdges.isEmpty()) {
                throw new ModelConstructionFailedException("Dead end detected - this is prohibited in road graph");
            }

//            Connection fromConnection = connectionsMappedByNodes.get(fromNode);
            Connection toConnection = connectionsMappedByNodes.get(toNode);
            for (SimulationEdge outEdge : nextEdges) {
                SimulationNode nextNode = graph.getNode(outEdge.toId);
                Lane newLane = new Lane(link, link.getLength(), timeProvider, simulationProvider);
                link.addLane(newLane, nextNode);
                Link outLink = linksMappedByEdges.get(outEdge);
                if (toConnection instanceof Crossroad) {
                    ((Crossroad) toConnection).addNextLink(outLink, newLane, connectionsMappedByNodes.get(nextNode));
                } else {
                    toConnection.setOutLink(outLink, newLane);
                }
            }
        }
    }

    private void initCrossroads() {
        for (Entry<SimulationNode, Connection> entry : connectionsMappedByNodes.entrySet()) {
            SimulationNode key = entry.getKey();
            Connection connection = entry.getValue();
            if (connection instanceof Crossroad) {
                ((Crossroad) connection).init();
            }
        }
    }


    public List<Link> getLinks() {
        return links;
    }

    long computeDelayAndSetVehicleData(VehicleTripData vehicleData, Lane nextLane) {
        Link nextLink = nextLane.link;

        // for visio
        Driver driver = vehicleData.getVehicle().getDriver();
        PhysicalVehicle vehicle = vehicleData.getVehicle();

        vehicle.setPosition(nextLink.fromNode);
        vehicle.setQueueBeforeVehicleLength(nextLane.getUsedLaneCapacityInMeters() - vehicle.getLength());

        double distance = nextLink.edge.shape.getShapeLength() - vehicle.getQueueBeforeVehicleLength();

        long delay = nextLane.computeDelay(vehicleData.getVehicle(), distance);
        DelayData delayData = new DelayData(delay, getTimeProvider().getCurrentSimTime(), distance);

        driver.setTargetNode(nextLink.toNode);
        driver.setDelayData(delayData);

        return delayData.getDelay();
    }

    public void wakeUpConnection(Connection connection, long delay) {
        makeTickEvent(connection, delay);
    }

    public void makeTickEvent(EventHandler target, long delay) {
        simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, target, null, null, delay + 80);
    }

    public static long computeFreeflowTransferDelay(PhysicalVehicle vehicle) {
        return Math.round(vehicle.getLength() * 1E3 / vehicle.getVelocity());
    }
}
