/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import cz.agents.agentpolis.siminfrastructure.CollectionUtil;
import cz.agents.agentpolis.siminfrastructure.Log;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MoveUtil;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author fido
 */
public class Link {

    private final CongestionModel congestionModel;

    /**
     * Lanes mapped by next nodes
     */
    private final Map<SimulationNode, Lane> lanesMappedByNodes;

    private final SimulationEdge edge;

    final SimulationNode toNode;

    final SimulationNode fromNode;

    public SimulationEdge getEdge() {
        return edge;
    }


    public Link(CongestionModel congestionModel, SimulationEdge edge, SimulationNode fromNode,
                SimulationNode targetNode) {
        this.congestionModel = congestionModel;
        this.edge = edge;
        this.toNode = targetNode;
        this.fromNode = fromNode;
        this.lanesMappedByNodes = new HashMap<>();
    }


    public int getLaneCount() {
        return lanesMappedByNodes.size();
    }

    public int getLength() {
        return edge.length;
    }

    public Lane getLaneByNextNode(SimulationNode node) {
        return lanesMappedByNodes.get(node);
    }

    void startDriving(VehicleTripData vehicleData, long delay) {
        Trip<SimulationNode> trip = vehicleData.getTrip();
        SimulationNode nextLocation = trip.getAndRemoveFirstLocation();
        Lane nextLane = null;
        if (trip.isEmpty()) {
            nextLane = getLaneForTripEnd();
            vehicleData.setTripFinished(true);
        } else {
            nextLane = getLaneByNextNode(nextLocation);
        }
        nextLane.startDriving(vehicleData, delay);
    }

    void addLane(Lane lane, SimulationNode nextNode) {
        lanesMappedByNodes.put(nextNode, lane);
    }

    Lane getLaneForTripEnd() {
        Entry<SimulationNode, Lane> randomEntry
                = CollectionUtil.getRandomEntryFromMap(lanesMappedByNodes, congestionModel.getRandom());
        return randomEntry.getValue();
    }

    long computeDelay(PhysicalVehicle vehicle) {
        double freeFlowVelocity = MoveUtil.computeAgentOnEdgeVelocity(vehicle.getVelocity(), edge.getAllowedMaxSpeedInMpS());
        double usedCapacity = getUsedCapacityInMeters();
        double capacity = edge.getLanesCount() * edge.length;
        double level = usedCapacity / capacity;

        double speed = freeFlowVelocity * interpolateSquared(1, 0.1, 1 - level);
        double duration = edge.length / speed;
        long durationInMs = Math.max(1, (long) (1000 * duration));
        return durationInMs;
    }

    public Collection<Lane> getLanes() {
        return lanesMappedByNodes.values();
    }

    public double getUsedCapacityInMeters() {
        double usedCapacity = 0;
        for (Lane lane : getLanes()) {
            usedCapacity += lane.getUsedLaneCapacityInMeters();
        }
        return usedCapacity;
    }

    private double interpolateSquared(double from, double to, double x) {
        double v = x * x;
        double y = (from * v) + (to * (1 - v));
        if (y < Math.min(from, to) || y > Math.max(from, to))
            Log.error(this, y + ": value out of range (" + from + "," + to + ")!");
        return y;
    }
}
