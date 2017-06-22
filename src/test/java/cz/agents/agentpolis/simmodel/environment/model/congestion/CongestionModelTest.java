/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import cz.agents.agentpolis.AgentPolisInitializer;
import cz.agents.agentpolis.simmodel.environment.model.Graphs;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.HighwayNetwork;
import cz.agents.basestructures.Graph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author fido
 */
public class CongestionModelTest {
    
    public void run(Graph<SimulationNode, SimulationEdge> graph) throws Throwable{
        
        Map<GraphType,Graph<SimulationNode, SimulationEdge>> graphs = new HashMap<>();
        graphs.put(EGraphType.HIGHWAY, graph);
        
        // Guice configuration
        AgentPolisInitializer agentPolisInitializer = new AgentPolisInitializer();
        agentPolisInitializer.overrideModule(new TestModule());
        Injector injector = agentPolisInitializer.initialize();
        
        //map init
        injector.getInstance(Graphs.class).setGraphs(graphs);
        
        try{
            TestCongestionModel congestionModel = injector.getInstance(TestCongestionModel.class);
            Graph<SimulationNode,SimulationEdge> roadGraph = injector.getInstance(HighwayNetwork.class).getNetwork();

            nodeTest(congestionModel, roadGraph);
            edgeTest(congestionModel, roadGraph);
        }
        catch(ProvisionException e){
            throw e.getCause();
        }
    }

    private void nodeTest(TestCongestionModel congestionModel, Graph<SimulationNode, SimulationEdge> roadGraph) {
        for (SimulationNode simulationNode : roadGraph.getAllNodes()) {
            Connection connection = congestionModel.getConnectionByNode(simulationNode);
            
            //each node have a connection test
            assertNotNull(connection);
            
            if(connection instanceof Crossroad){
                checkCrossroad((Crossroad) connection);
            }
            else{
                checkConnection(connection);
            }
        }
    }
    
    private void edgeTest(TestCongestionModel congestionModel, Graph<SimulationNode, SimulationEdge> roadGraph) {
        for (SimulationEdge simulationEdge : roadGraph.getAllEdges()) {
            Link link = congestionModel.getLinkByEdge(simulationEdge);
            
            // each edge have a link test
            assertNotNull("Edge " + simulationEdge.getLogInfo()+ " does not have a corresponding link!",link);
            
            // no empty links test
            assertTrue("Link " + simulationEdge.getLogInfo()+ " has no lanes!", 
                    link.getLaneCount() > 0);
            
            // check all next nodes has a lane
            checkLaneForEachNextNode(roadGraph, link);
        }
    }

    private void checkLaneForEachNextNode(Graph<SimulationNode, SimulationEdge> roadGraph, Link link) {
        SimulationNode targetNode = roadGraph.getNode(link.getEdge().toId);
        List<SimulationEdge> outEdges = roadGraph.getOutEdges(targetNode);
        
        for (SimulationEdge outEdge : outEdges) {
            SimulationNode nextNode = roadGraph.getNode(outEdge.toId);
            
            assertNotNull("No lane for output edge " + outEdge.getLogInfo()+ "!", link.getLaneByNextNode(nextNode));
        }
    }

    private void checkConnection(Connection connection) {
        assertNotNull(connection.getNextLink((Connection) null));
    }

    private void checkCrossroad(Crossroad crossroad) {
        assertTrue(crossroad.getNumberOfInputLanes() > 0);
    }
}
