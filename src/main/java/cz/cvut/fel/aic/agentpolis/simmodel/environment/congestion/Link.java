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

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

import java.util.*;

/**
 * @author fido
 */
public class Link {
    final CongestionModel congestionModel;
    final SimulationEdge edge;

    final SimulationNode toNode;
    final SimulationNode fromNode;

    final Connection fromConnection;
    final Connection toConnection;

    private final Map<SimulationNode, List<CongestionLane>> lanesMappedByNodes;
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
        return edge.length;
    }

    /**
     * Return list of lanes for direction
     * @param node
     * @return
     */
    public List<CongestionLane> getLanesByNextNode(SimulationNode node) {
        return this.lanesMappedByNodes.get(node);
    }

    /**
     * Return best lane for selected direction
     */
    public CongestionLane getBestLaneByNextNode(SimulationNode node) {
        return getBestLane(node);
    }

    /**
     * Select all lanes
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
    public CongestionLane getCongestionLaneForTripEnd() {
        return congestionLaneForTripEnd;
    }


    //
    // ========================= Driving ======================
    //
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

        Log.info(this, "Congestion lane=" + nextCongestionLane.getLane().getLaneUniqueId() +  " car="+ vehicleData.getVehicle().getId(), congestionModel.timeProvider.getCurrentSimTime());
        nextCongestionLane.startDriving(vehicleData);
    }

    //
    // ========================= Build ======================
    //
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
    private CongestionLane getBestLane(SimulationNode direction) {
        List<CongestionLane> possibleLanes = lanesMappedByNodes.get(direction);
        if (possibleLanes != null) {
            // balancing
            CongestionLane bestLane = getBestLane(possibleLanes);
            if (bestLane != null) {
                return bestLane;
            }
            // or selected randomly
            // TODO: debug remove
            return getRandomLane(possibleLanes);
            //possibleLanes.get(0);
        }
        return null;
    }

    // return random lane out of possible
    private CongestionLane getRandomLane(List<CongestionLane> possibleLanes) {
        return possibleLanes.get(congestionModel.getRandom().nextInt(possibleLanes.size()));
    }

    // get best lane out of possible
    private CongestionLane getBestLane(List<CongestionLane> possibleLanes) {
        Map<CongestionLane, Double> available = availablePossibleLanes(possibleLanes);
        if (available == null) {
            return null;
        }
        CongestionLane selected = null;
        for (Map.Entry<CongestionLane, Double> entry : available.entrySet()) {
            if (selected != null) {
                if (selected.getUsedLaneCapacityInMeters() > entry.getValue()) {
                    selected = entry.getKey();
                }
            } else {
                selected = entry.getKey();
            }
        }
        return selected;
    }

    // Return all possible lanes based of free space in them
    private Map<CongestionLane, Double> availablePossibleLanes(List<CongestionLane> possibleLanes) {
        Collections.shuffle(possibleLanes, congestionModel.getRandom()); // randomly shuffle lanes - better for same lanes
        Map<CongestionLane, Double> available = new HashMap<>();
        for (CongestionLane l : possibleLanes) {
            if (l.parentLink.getEdge().length - l.getUsedLaneCapacityInMeters() > 5) {
                available.put(l, l.getUsedLaneCapacityInMeters());
            }
        }
        return available;
    }


}
