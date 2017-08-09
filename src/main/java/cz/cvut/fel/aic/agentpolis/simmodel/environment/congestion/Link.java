/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.CollectionUtil;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author fido
 */
public class Link {

    final CongestionModel congestionModel;

    /**
     * Lanes mapped by next nodes
     */
    private final Map<SimulationNode, Lane> lanesMappedByNodes;

    final SimulationEdge edge;

    final SimulationNode toNode;

    final SimulationNode fromNode;
    
    final Connection fromConnection;

    public SimulationEdge getEdge() {
        return edge;
    }


    public Link(CongestionModel congestionModel, SimulationEdge edge, SimulationNode fromNode,
                SimulationNode targetNode, Connection fromConnection) {
        this.congestionModel = congestionModel;
        this.edge = edge;
        this.toNode = targetNode;
        this.fromNode = fromNode;
        this.fromConnection = fromConnection;
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

    long startDriving(VehicleTripData vehicleData) {
        Trip<SimulationNode> trip = vehicleData.getTrip();
        Lane nextLane = null;
        if (trip.isEmpty()) {
            nextLane = getLaneForTripEnd();
            vehicleData.setTripFinished(true);
        } else {
            SimulationNode nextLocation = trip.getAndRemoveFirstLocation();
            nextLane = getLaneByNextNode(nextLocation);
        }
        
        long delay = congestionModel.computeDelayAndSetVehicleData(vehicleData, nextLane);
        
        nextLane.startDriving(vehicleData, delay);
        
        return delay;
    }

    void addLane(Lane lane, SimulationNode nextNode) {
        lanesMappedByNodes.put(nextNode, lane);
    }

    Lane getLaneForTripEnd() {
        Entry<SimulationNode, Lane> randomEntry
                = CollectionUtil.getRandomEntryFromMap(lanesMappedByNodes, congestionModel.getRandom());
        return randomEntry.getValue();
    }

    

    public Collection<Lane> getLanes() {
        return lanesMappedByNodes.values();
    }

}
