/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

import java.util.*;

/**
 * @author fido
 */
public class Link {
    final CongestionModel congestionModel;

    /**
     * Lanes mapped by next nodes
     */
    private final Map<SimulationNode, List<CongestionLane>> lanesMappedByNodes;

    final SimulationEdge edge;

    final SimulationNode toNode;

    final SimulationNode fromNode;

    final Connection fromConnection;

    final Connection toConnection;

    private CongestionLane congestionLaneForTripEnd;


    public Link(CongestionModel congestionModel, SimulationEdge edge, SimulationNode fromNode,
                SimulationNode targetNode, Connection fromConnection, Connection toConnection) {
        this.congestionModel = congestionModel;
        this.edge = edge;
        this.toNode = targetNode;
        this.fromNode = fromNode;
        this.fromConnection = fromConnection;
        this.toConnection = toConnection;
        this.lanesMappedByNodes = new HashMap<>();
    }

    public SimulationEdge getEdge() {
        return edge;
    }

    public int getLaneCount() {
        return edge.getLanesCount();
    }

    public double getLength() {
        return edge.shape.getShapeLength();
    }

    public List<CongestionLane> getLaneByNextNode(SimulationNode node) {
        return lanesMappedByNodes.get(node);
    }

    /**
     * Return best lane for selected direction
     */
    public CongestionLane getBestLaneByNextNode(SimulationNode node) {
        return getLane(node);
    }

    /**
     * Select available lanes
     *
     * @return
     */
    public Collection<CongestionLane> getLanes() {
        Collection<CongestionLane> lanes = new LinkedList<>();
        for (Map.Entry<SimulationNode, List<CongestionLane>> entry : lanesMappedByNodes.entrySet()) {
            List<CongestionLane> c = entry.getValue();
            if (c != null) {
                for (CongestionLane l : c) {
                    if (lanes.contains(l)) {
                        continue;
                    }
                    lanes.add(l);
                }
            }
        }
        return lanes;
    }

    /**
     * Default lane for trip end, vehicle does not drive to next connection.
     */
    CongestionLane getCongestionLaneForTripEnd() {
        return congestionLaneForTripEnd;
    }

    void startDriving(VehicleTripData vehicleData) {
        Trip<SimulationNode> trip = vehicleData.getTrip();
        CongestionLane nextCongestionLane = null;
        if (trip.isEmpty()) {
            nextCongestionLane = getCongestionLaneForTripEnd();
            vehicleData.setTripFinished(true);
        } else {
            SimulationNode nextLocation = trip.getAndRemoveFirstLocation();
            nextCongestionLane = getBestLaneByNextNode(nextLocation);
        }

        nextCongestionLane.startDriving(vehicleData);
    }

    /**
     * Build data
     */
    void addCongestionLane(CongestionLane congestionLane, List<SimulationNode> nextNodes) {
        if (congestionLaneForTripEnd == null) {
            congestionLaneForTripEnd = congestionLane;
        }
        if (nextNodes != null) {
            for (SimulationNode e : nextNodes) {
                if (!lanesMappedByNodes.containsKey(e)) {
                    lanesMappedByNodes.put(e, new LinkedList<>());
                }
                lanesMappedByNodes.get(e).add(congestionLane);
            }
        }
    }


    //
    // ========================= Lanes balancing ======================
    //
    private CongestionLane getLane(SimulationNode direction) {
        List<CongestionLane> possibleLanes = lanesMappedByNodes.get(direction);
        if (possibleLanes != null) {
            // balancing
            CongestionLane bestLane = getBestLane(possibleLanes);
            if (bestLane != null) {
                return bestLane;
            }
            // or selected randomly
            return getRandomLane(possibleLanes);
        }
        return null;
    }

    private CongestionLane getRandomLane(List<CongestionLane> possibleLanes) {
        return possibleLanes.get(congestionModel.getRandom().nextInt(possibleLanes.size()));
    }

    private CongestionLane getBestLane(List<CongestionLane> possibleLanes) {
        Map<CongestionLane, Double> available = availablePossibleLanes(possibleLanes);
        if (available == null) {
            return null;
        }
        CongestionLane selected = null;
        for (Map.Entry<CongestionLane, Double> entry : available.entrySet()) {
            if (selected != null) {
                if (selected.getUsedLaneCapacityInMeters() < entry.getValue()) {
                    selected = entry.getKey();
                }
            } else {
                selected = entry.getKey();
            }
        }
        return selected;
    }

    private Map<CongestionLane, Double> availablePossibleLanes(List<CongestionLane> possibleLanes) {
        Map<CongestionLane, Double> available = new HashMap<>();
        for (CongestionLane l : possibleLanes) {
            if (l.getUsedLaneCapacityInMeters() > 5) {
                available.put(l, l.getUsedLaneCapacityInMeters());
            }
        }
        return available;
    }


}
