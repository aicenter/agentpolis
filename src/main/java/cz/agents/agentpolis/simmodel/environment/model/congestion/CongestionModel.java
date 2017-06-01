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
import java.util.Map;

/**
 *
 * @author fido
 */
@Singleton
public class CongestionModel {
    private final Graph<SimulationNode, SimulationEdge> graph;
    
    private final Map<SimulationNode,Crossroad> crossroadsMappedByNodes;
    
    private final Config config;
    
    private final SimulationProvider simulationProvider;

    @Inject
    public CongestionModel(Graph<SimulationNode, SimulationEdge> graph, Config config, 
            SimulationProvider simulationProvider) {
        this.graph = graph;
        this.config = config;
        this.simulationProvider = simulationProvider;
        crossroadsMappedByNodes = new HashMap<>();
        buildCongestionGraph();
    }

    private void buildCongestionGraph() {
        buildCrossroads(graph.getAllNodes());
        buildLinks(graph.getAllEdges());
    }
    
    private void drive(PhysicalVehicle vehicle){
        Trip<SimulationNode> trip = vehicle.getDriver().getCurrentTrip();
        SimulationNode startLocation = trip.getAndRemoveFirstLocation();
        Crossroad startCrossroad = crossroadsMappedByNodes.get(startLocation);
        startCrossroad.startDriving(vehicle);        
    }

    private void buildCrossroads(Collection<SimulationNode> allNodes) {
        for (SimulationNode node : allNodes) {
            Crossroad crossroad = new Crossroad(config, simulationProvider);
            crossroadsMappedByNodes.put(node, crossroad);
        }
    }

    private void buildLinks(Collection<SimulationEdge> allEdges) {
        
    }
    
    
}
