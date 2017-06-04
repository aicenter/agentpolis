/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.agentpolis.config.Config;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simulator.SimulationProvider;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fido
 */
@Singleton
public class CongestionModel {
    private final Graph<SimulationNode, SimulationEdge> graph;
    
    private final Map<SimulationNode,Connection> crossroadsMappedByNodes;
	
	private final List<Link> links;
    
    private final Config config;
    
    private final SimulationProvider simulationProvider;

    @Inject
    public CongestionModel(Graph<SimulationNode, SimulationEdge> graph, Config config, 
            SimulationProvider simulationProvider) {
        this.graph = graph;
        this.config = config;
        this.simulationProvider = simulationProvider;
        crossroadsMappedByNodes = new HashMap<>();
		links = new LinkedList<>();
        buildCongestionGraph();
    }

    private void buildCongestionGraph() {
        buildCrossroads(graph.getAllNodes());
        buildLinks(graph.getAllEdges());
		buildLanes();
    }
    
    public void drive(PhysicalVehicle vehicle){
        VehicleTripData vehicleData = new VehicleTripData(vehicle, vehicle.getDriver().getCurrentTrip());
        Trip<SimulationNode> trip = vehicleData.getTrip();
        SimulationNode startLocation = trip.getAndRemoveFirstLocation();
        Connection startCrossroad = crossroadsMappedByNodes.get(startLocation);
        startCrossroad.startDriving(vehicleData);        
    }

    private void buildCrossroads(Collection<SimulationNode> allNodes) {
        for (SimulationNode node : allNodes) {
			if(graph.getInEdges(node).size() > 2){
				Crossroad crossroad = new Crossroad(config, simulationProvider);
				crossroadsMappedByNodes.put(node, crossroad);
			}
			else{
				Connection connection = new Connection(simulationProvider, config);
				crossroadsMappedByNodes.put(node, connection);
			}
        }
    }

    private void buildLinks(Collection<SimulationEdge> allEdges) {
        for (SimulationEdge edge : allEdges) {
			Link link = new Link(edge);
			links.add(link);
		}
    }

	private void buildLanes() {
		for (Link link : links) {
			SimulationNode targetNode = graph.getNode(link.getEdge().toId);
			
		}
	}
    
    
}
