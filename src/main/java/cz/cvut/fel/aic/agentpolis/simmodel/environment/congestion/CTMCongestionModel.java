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
import cz.cvut.fel.aic.agentpolis.simmodel.environment.ctm.CTMConnection;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.ctm.Segment;
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

@Singleton
public class CTMCongestionModel {
    final Graph<SimulationNode, SimulationEdge> graph;

    protected final Map<SimulationNode, CTMConnection> connectionsMappedByNodes;

    private final List<Link> links;

    protected final Map<SimulationEdge, Segment> linksMappedByEdges;

    private final AgentpolisConfig config;

    private final SimulationProvider simulationProvider;

    final TimeProvider timeProvider;

    private final Random random;
    private ShapeUtils shapeUtils;


    Random getRandom() {
        return random;
    }

    TimeProvider getTimeProvider() {
        return timeProvider;
    }


    @Inject
    public CTMCongestionModel(TransportNetworks transportNetworks, AgentpolisConfig config,
                              SimulationProvider simulationProvider, TimeProvider timeProvider, ShapeUtils shapeUtils, LaneCongestionModel laneCongestionModel)
            throws ModelConstructionFailedException, ProviderException {
        this.graph = transportNetworks.getGraph(EGraphType.HIGHWAY);
        this.config = config;
        this.simulationProvider = simulationProvider;
        this.timeProvider = timeProvider;
        this.shapeUtils = shapeUtils;
        connectionsMappedByNodes = new HashMap<>();
        linksMappedByEdges = new HashMap<>();
        links = new LinkedList<>();
        random = new Random(config.congestionModel.randomSeed);
    }

    @Inject
    public void buildCongestionGraph() throws ModelConstructionFailedException {
        buildLinks(graph.getAllEdges());
        buildConnections(graph.getAllNodes());
    }

    public void drive(PhysicalVehicle vehicle, Trip<SimulationNode> trip) {
//        if(!queueHandlingStarted){
//            startQueeHandling();
//        }
        VehicleTripData vehicleData = new VehicleTripData(vehicle, trip);
        SimulationNode startLocation = trip.getAndRemoveFirstLocation();
        CTMConnection startConnection = connectionsMappedByNodes.get(startLocation);
        startConnection.startDriving(vehicleData);
    }

    private void buildConnections(Collection<SimulationNode> allNodes) {
        for (SimulationNode node : allNodes) {
            List<SimulationEdge> inEdges = graph.getInEdges(node);
            List<SimulationEdge> outEdges = graph.getOutEdges(node);
            List<Segment> inSegments = new ArrayList<>();
            Map<SimulationNode, Segment> outSegments = new HashMap<>();
            inEdges.forEach(edge -> {
                inSegments.add(linksMappedByEdges.get(edge));
            });
            outEdges.forEach(edge -> {
                outSegments.put(graph.getNode(edge.toId), linksMappedByEdges.get(edge));
            });

//            if (graph.getOutEdges(node).size() > 1 || graph.getInEdges(node).size() > 1) {
//                Crossroad crossroad = new Crossroad(config, simulationProvider, this, node, timeProvider);
//                connectionsMappedByNodes.put(node, crossroad);
//            } else {
                CTMConnection connection = new CTMConnection(node, simulationProvider, inSegments, outSegments);
                connectionsMappedByNodes.put(node, connection);
//            }
        }
    }

    private void buildLinks(Collection<SimulationEdge> allEdges) {
        for (SimulationEdge edge : allEdges) {
            SimulationNode fromNode = graph.getNode(edge.fromId);
            SimulationNode toNode = graph.getNode(edge.toId);
            Segment link = new Segment(edge);
            linksMappedByEdges.put(edge, link);
        }
    }


    public List<Link> getLinks() {
        return links;
    }


    public void makeTickEvent(Object caller, EventHandler target, long delay) {
        Log.debug(this, "adding tick: delay = " + delay + "source:" + caller + "target =" + target);
        simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, target, null, null, delay != 0 ? delay : 1);
    }

    public void makeScheduledEvent(Object caller, EventHandler target, long delay) {
        Log.debug(this, "adding scheduled event: delay = " + delay + "source:" + caller + "target =" + target);
        simulationProvider.getSimulation().addEvent(ConnectionEvent.SCHEDULED_EVENT, target, null, null, delay != 0 ? delay : 1);
    }

}
