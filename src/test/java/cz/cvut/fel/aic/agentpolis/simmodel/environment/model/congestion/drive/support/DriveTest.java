/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import com.google.inject.Injector;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.system.AgentPolisInitializer;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.CongestedDriveFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.mock.CongestionTestType;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import cz.cvut.fel.aic.geographtools.Graph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fido
 */
public class DriveTest {
    private final int time_limit_in_milis;
    private DriveAgentStorage agents;

    public DriveTest(int time_limit){
        time_limit_in_milis = time_limit;
    }

    public DriveTest(){
        time_limit_in_milis = -1;
    }

    public void run(Graph<SimulationNode, SimulationEdge> graph, Trip<SimulationNode> ... trips) {

        // Guice configuration
        AgentPolisInitializer agentPolisInitializer = new AgentPolisInitializer(new TestModule());
        //agentPolisInitializer.overrideModule(new TestModule());
        Injector injector = agentPolisInitializer.initialize();

        SimulationCreator creator = injector.getInstance(SimulationCreator.class);

        // prepare map, entity storages...
        if(time_limit_in_milis != -1) {
            AgentpolisConfig config = injector.getInstance(AgentpolisConfig.class);
            config.simulationDurationInMillis = time_limit_in_milis;
            Log.info(this,"Override simulation time limit (ms): "+ time_limit_in_milis);
        }
        creator.prepareSimulation(getMapData(graph));
        
        CongestedDriveFactory congestedDriveFactory = injector.getInstance(CongestedDriveFactory.class);
        DriveAgentStorage driveAgentStorage = injector.getInstance(DriveAgentStorage.class);

        
        for (int i = 0; i < trips.length; i++) {
            Trip<SimulationNode> trip = trips[i];
            PhysicalVehicle vehicle = new PhysicalVehicle("Test vehicle" + i, CongestionTestType.TEST_VEHICLE, 5, 2, 
               EGraphType.HIGHWAY, graph.getNode(0), 15);
        
            DriveAgent driveAgent = new DriveAgent("Test driver" + i, graph.getNode(0));

            driveAgentStorage.addEntity(driveAgent);
            congestedDriveFactory.runActivity(driveAgent, vehicle, trip);
        }
        agents = driveAgentStorage;
        creator.startSimulation();

    }

    public DriveAgentStorage getAgents(){
        return this.agents;
    }
    
    private MapData getMapData(Graph<SimulationNode, SimulationEdge> graph){
        Map<GraphType,Graph<SimulationNode, SimulationEdge>> graphs = new HashMap<>();
        graphs.put(EGraphType.HIGHWAY, graph);
        
        Map<Integer, SimulationNode> nodes = createAllGraphNodes(graphs);

        return new MapData(graphs, nodes);
    }
    
    /**
     * Build map data
     */
    private Map<Integer, SimulationNode> createAllGraphNodes(Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByGraphType) {

        Map<Integer, SimulationNode> nodesFromAllGraphs = new HashMap<>();

        for (GraphType graphType : graphByGraphType.keySet()) {
            Graph<SimulationNode, SimulationEdge> graphStorageTmp = graphByGraphType.get(graphType);
            for (SimulationNode node : graphStorageTmp.getAllNodes()) {
                nodesFromAllGraphs.put(node.getId(), node);
            }

        }

        return nodesFromAllGraphs;
    }
}
