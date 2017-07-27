/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import com.google.inject.Injector;
import cz.cvut.fel.aic.agentpolis.AgentPolisInitializer;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.CongestedDriveFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.mock.CongestionTestType;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.cvut.fel.aic.agentpolis.simulator.creator.initializator.impl.MapData;
//import cz.agents.agentpolis.simulator.visualization.visio.Bound;
import cz.agents.basestructures.BoundingBox;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fido
 */
public class DriveTest {
    
    private static final int TEN_MINUTES_IN_MILIS = 600000;
    
    // we expect trips to be no longer then 40 minutes
    private static final int TRIP_MAX_DURATION = 2400000;
    
    private static final int START_TIME_MILIS = 25200000;
    
    
    public void run(Graph<SimulationNode, SimulationEdge> graph, Trip<SimulationNode> trip) {
        
        
//        Config config = Configuration.load(new Config());
        
        //config overwrite
//        config.agentpolis.simulationDurationInMillis = TEN_MINUTES_IN_MILIS;
//        config.agentpolis.startTime = START_TIME_MILIS;
//        config.agentpolis.showVisio = true;
//        Common.setTestResultsDir(config, "test");
        
        // Guice configuration
        AgentPolisInitializer agentPolisInitializer = new AgentPolisInitializer(new TestModule());
//        agentPolisInitializer.overrideModule(new TestModule());
        Injector injector = agentPolisInitializer.initialize();

        SimulationCreator creator = injector.getInstance(SimulationCreator.class);

        // prepare map, entity storages...
        creator.prepareSimulation(getMapData(graph));
        
        CongestedDriveFactory congestedDriveFactory = injector.getInstance(CongestedDriveFactory.class);
        
        PhysicalVehicle vehicle = new PhysicalVehicle("Test vehicle", CongestionTestType.TEST_VEHICLE, 5, 2, 
                EGraphType.HIGHWAY, graph.getNode(0), 15);
        
        DriveAgent driveAgent = new DriveAgent("Test driver", graph.getNode(0));
        
        injector.getInstance(DriveAgentStorage.class).addEntity(driveAgent);
        
        congestedDriveFactory.runActivity(driveAgent, vehicle, trip);
        
        creator.startSimulation();
    }
    
    private MapData getMapData(Graph<SimulationNode, SimulationEdge> graph){
        Map<GraphType,Graph<SimulationNode, SimulationEdge>> graphs = new HashMap<>();
        graphs.put(EGraphType.HIGHWAY, graph);
        
        Map<Integer, SimulationNode> nodes = createAllGraphNodes(graphs);
        BoundingBox bounds = computeBounds(nodes.values());

        return new MapData(bounds, graphs, nodes);
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
    
    private BoundingBox computeBounds(Collection<SimulationNode> nodes) {
        double latMin = Double.POSITIVE_INFINITY;
        double latMax = Double.NEGATIVE_INFINITY;

        double lonMin = Double.POSITIVE_INFINITY;
        double lonMax = Double.NEGATIVE_INFINITY;

        for (Node node : nodes) {
            double lat = node.getLatitude();
            double lon = node.getLongitude();

            if (lat < latMin) latMin = lat;
            else if (lat > latMax) latMax = lat;
            if (lon < lonMin) lonMin = lon;
            else if (lon > lonMax) lonMax = lon;
        }
        return new BoundingBox((int) (lonMin * 1E6), (int) (latMin * 1E6), (int) (lonMax * 1E6), (int) (latMax * 1E6));
    }
}
