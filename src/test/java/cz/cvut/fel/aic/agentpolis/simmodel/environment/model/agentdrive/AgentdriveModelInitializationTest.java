package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.agentdrive;

import com.google.inject.Injector;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.Graphs;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.AgentDriveModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.ModelConstructionFailedException;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.agentdrive.support.TestModule;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.Utils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.cvut.fel.aic.agentpolis.system.AgentPolisInitializer;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AgentdriveModelInitializationTest {

    private AgentDriveModel agentDriveModel;
    private Injector injector;
    private Graph<SimulationNode, SimulationEdge> graph;
    private Map<GraphType,Graph<SimulationNode, SimulationEdge>> graphs;

    @Test
    public void initAgentdriveModelTest() throws ModelConstructionFailedException {
        AgentPolisInitializer agentPolisInitializer = new AgentPolisInitializer();
        agentPolisInitializer.overrideModule(new TestModule());
        injector = agentPolisInitializer.initialize();
        injector.injectMembers(SimulationCreator.class);
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = Utils.getCompleteGraph(10);
        graph = graphBuilder.createGraph();
        graphs = new HashMap<>();
        graphs.put(EGraphType.HIGHWAY, graph);
        SimulationCreator creator = injector.getInstance(SimulationCreator.class);
        creator.prepareSimulation(getMapData(graph));
        injector.getInstance(Graphs.class).setGraphs(graphs);
        agentDriveModel = injector.getInstance(AgentDriveModel.class);
        Assert.assertNotNull(agentDriveModel);
    }

    private MapData getMapData(Graph<SimulationNode, SimulationEdge> graph){
    Map<GraphType,Graph<SimulationNode, SimulationEdge>> graphs = new HashMap<>();
        graphs.put(EGraphType.HIGHWAY, graph);

    Map<Integer, SimulationNode> nodes = createAllGraphNodes(graphs);

        return new MapData(graphs, nodes);
}


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
