/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Injector;
import cz.cvut.fel.aic.agentpolis.AgentPolisInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.TestModule;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.cvut.fel.aic.agentpolis.simulator.creator.initializator.MapDataGenerator;
import cz.cvut.fel.aic.agentpolis.simulator.creator.initializator.impl.MapData;
import cz.cvut.fel.aic.geographtools.BoundingBox;
import cz.cvut.fel.aic.geographtools.Graph;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fido
 */
public class VisioTest {
    
    public void run(Graph<SimulationNode, SimulationEdge> graph){
        
        // Guice configuration
        AgentPolisInitializer agentPolisInitializer = new AgentPolisInitializer(
                new TestModule());
//        agentPolisInitializer.overrideModule(new TestModule());
        Injector injector = agentPolisInitializer.initialize();

        SimulationCreator creator = injector.getInstance(SimulationCreator.class);

        // prepare map, entity storages...
        creator.prepareSimulation(getMapData(graph));
        
        creator.startSimulation();
    }

    private MapData getMapData(Graph<SimulationNode, SimulationEdge> graph){
         MapDataGenerator mapDataGenerator = new MapDataGenerator(graph);
         
         return mapDataGenerator.getMap();
     }
}
