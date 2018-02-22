/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
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
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.agent.CongestedTripData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.agent.VehicleTripData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.Connection;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.ConnectionEvent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.support.ModelConstructionFailedException;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.support.VehicleEventData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.support.VehicleTransferData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.lanes.CongestionLane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.lanes.CongestionLaneModel;
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

@Singleton
public class CongestionModel {
    protected final SimulationProvider simulationProvider;
    public final TimeProvider timeProvider;

    protected final Graph<SimulationNode, SimulationEdge> graph;
    protected final AgentpolisConfig config;

    public final Map<SimulationNode, Connection> connectionsMappedByNodes = new HashMap<>();
    protected final Map<SimulationEdge, Link> linksMappedByEdges = new HashMap<>();
    protected final Map<Integer, SimulationEdge> edgesMappedById = new HashMap<>();

    protected final List<Link> links = new LinkedList<>();
    protected final List<CongestionLane> congestionLanes = new LinkedList<>();

    private final Random random;
    public final boolean addFundamentalDiagramDelay;
    private CongestionLaneModel congestionLaneModel;


    @Inject
    public CongestionModel(TransportNetworks transportNetworks, AgentpolisConfig config,
                           SimulationProvider simulationProvider, TimeProvider timeProvider, ShapeUtils shapeUtils, CongestionLaneModel congestionLaneModel)
            throws ModelConstructionFailedException, ProviderException {
        this.graph = transportNetworks.getGraph(EGraphType.HIGHWAY);
        this.config = config;
        this.simulationProvider = simulationProvider;
        this.timeProvider = timeProvider;
        this.congestionLaneModel = congestionLaneModel;

        random = new Random(config.congestionModel.randomSeed);
        addFundamentalDiagramDelay = config.congestionModel.fundamentalDiagramDelay;
    }

    //
    // ========================= Build ======================
    //
    @Inject
    public void buildCongestionGraph() throws ModelConstructionFailedException {
        BuildCongestionModel model = new BuildCongestionModel(this);
        model.build();
    }

    //
    // ========================= Setter & Getter ======================
    //

    /**
     * All links in simulation
     * @return list
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * Random gen.
     * @return random seed
     */
    public Random getRandom() {
        return random;
    }

    /**
     * List of all lanes
     * @return list
     */
    public List<CongestionLane> getCongestionLanes() {
        return congestionLanes;
    }

    /**
     * Timer
     * @return TimeProvider
     */
    public TimeProvider getTimeProvider() {
        return timeProvider;
    }

    //
    // ========================= Driving ======================
    //

    /**
     * Add vehicle and put it in start queue
     * @param vehicle agent
     * @param trip path (nodes)
     */
    public void drive(PhysicalVehicle vehicle, Trip<SimulationNode> trip) {
        SimulationNode startLocation = trip.getAndRemoveFirstLocation();
        Connection startConnection = connectionsMappedByNodes.get(startLocation);
        startConnection.startDriving(new VehicleTripData(vehicle, trip));
    }

    //
    // ========================= Simulation events ======================
    //
    /**
     * Wakeup connection
     *
     * @param connection - to be waked up
     * @param delay      - positive number
     */
    public void wakeUpConnection(Connection connection, long delay) {
        makeTickEvent(this, connection, delay);
    }

    public void makeTickEvent(Object caller, EventHandler target, long delay) {
        Log.info(this, "                adding tick: delay=" + delay + " source=" + caller + " target =" + target);
        if (target instanceof Connection) {
            Connection c = (Connection) target;
            VehicleEventData e = c.vehicleEventData;
        }
        simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, target, null, null, delay != 0 ? delay : 1);
    }

    public void makeScheduledEvent(Object caller, EventHandler target, long delay) {
        if (target instanceof Connection) {
            Connection c = (Connection) target;
            VehicleEventData e = c.vehicleEventData;
            if (e instanceof VehicleTransferData) {
                VehicleTransferData v = (VehicleTransferData) e;
                Log.info(this, "adding scheduled event: delay=" + delay + //" source=" + caller + " target=" + target);
                        " vehicle=" + v.congestedTripData.getVehicle());
            }
        }
        //Log.info(this, "adding scheduled event: delay=" + delay + //" source=" + caller + " target=" + target);
        //        " vehicle="+e);
        simulationProvider.getSimulation().addEvent(ConnectionEvent.SCHEDULED_EVENT, target, null, null, delay != 0 ? delay : 1);
    }


    //
    // ========================= Delay on  ======================
    //
    /**
     * Compute delay on lane -> get status on lane
     * @param congestedTripData vehicle
     * @param transferToLane moving to lane
     * @return ms
     */
    public long computeTransferDelay(CongestedTripData congestedTripData, CongestionLane transferToLane) {
        return congestionLaneModel.computeTransferDelay(congestedTripData, transferToLane);
    }

    /**
     * Delay for visio
     * @param congestedTripData vehicle data
     * @param nextCongestionLane following lane
     * @return ms
     */
    public long computeDelayAndSetVehicleData(CongestedTripData congestedTripData, CongestionLane nextCongestionLane) {
        Link nextLink = nextCongestionLane.parentLink;

        // for visio
        Driver driver = congestedTripData.getVehicle().getDriver();
        PhysicalVehicle vehicle = congestedTripData.getVehicle();

        vehicle.setPosition(nextLink.fromNode);
        vehicle.setQueueBeforeVehicleLength(nextCongestionLane.getUsedLaneCapacityInMeters() - vehicle.getLength());
        vehicle.setLaneId(nextCongestionLane.getId());

        double distance = nextLink.edge.shape.getShapeLength() - vehicle.getQueueBeforeVehicleLength();

        long delay = nextCongestionLane.computeDelay(congestedTripData.getVehicle(), distance);
        DelayData delayData = new DelayData(delay, getTimeProvider().getCurrentSimTime(), distance);

        driver.setTargetNode(nextLink.toNode);
        driver.setDelayData(delayData);

        return delayData.getDelay();
    }

    /**
     * Maximum speed over node
     * @param vehicle for length of vehicle
     * @return ms
     */
    public static long computeFreeflowTransferDelay(PhysicalVehicle vehicle) {
        return Math.round(vehicle.getLength() * 1E3 / vehicle.getVelocity());
    }

    /**
     * Update expected delay
     * @param congestedTripData vehicle trip
     * @return ms
     */
    public long computeArrivalDelay(CongestedTripData congestedTripData) {
        long arrivalDelay = 0;
        DelayData delayData = congestedTripData.getVehicle().getDelayData();
        if (delayData != null) {
            long currentSimTime = timeProvider.getCurrentSimTime();
            long arrivalExpectedTime = delayData.getDelayStartTime() + delayData.getDelay();
            arrivalDelay = Math.max(0, arrivalExpectedTime - currentSimTime);
        }
        return arrivalDelay;
    }
}
