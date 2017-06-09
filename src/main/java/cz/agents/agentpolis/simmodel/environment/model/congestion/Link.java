/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fido
 */
public class Link {
    private List<Lane> lanes;
    
    private final Map<SimulationNode,Lane> lanesMappedByNodes;
    
    private final SimulationEdge edge;

	public SimulationEdge getEdge() {
		return edge;
	}
    
    
 

    public Link(SimulationEdge edge) {
        this.edge = edge;
        this.lanesMappedByNodes = new HashMap<>();
    }
    
    
    
    
    public int getLaneCount(){
        return lanes.size();
    }
    
    public int getLength(){
        return edge.length;
    }
    
    public Lane getLaneByNextNode(SimulationNode node){
        return lanesMappedByNodes.get(node);
    }
    
    void startDriving(VehicleTripData vehicleData){
        Trip<SimulationNode> trip = vehicleData.getTrip();
        SimulationNode nextLocation = trip.getAndRemoveFirstLocation();
        lanesMappedByNodes.get(nextLocation).startDriving(vehicleData);
    }

    void addLane(Lane lane, SimulationNode nextNode) {
        lanesMappedByNodes.put(nextNode, lane);
    }
}
