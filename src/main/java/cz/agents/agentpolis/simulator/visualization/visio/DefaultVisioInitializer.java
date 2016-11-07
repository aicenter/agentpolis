/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.environment.model.AgentPositionModel;
import cz.agents.agentpolis.simmodel.environment.model.AgentStorage;
import cz.agents.agentpolis.simmodel.environment.model.EntityStorage;
import cz.agents.agentpolis.simmodel.environment.model.VehiclePositionModel;
import cz.agents.agentpolis.simmodel.environment.model.VehicleStorage;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.AllNetworkNodes;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.BikewayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.HighwayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.MetrowayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.PedestrianNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.RailwayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.TramwayNetwork;
import cz.agents.agentpolis.simulator.creator.SimulationCreator;
import cz.agents.agentpolis.simulator.visualization.visio.entity.VisEntityLayer;
import cz.agents.agentpolis.simulator.visualization.visio.graph.VisGraph;
import cz.agents.agentpolis.simulator.visualization.visio.graph.VisGraphLayer;
import cz.agents.alite.simulation.MultipleDrawListener;
import cz.agents.alite.simulation.Simulation;
import cz.agents.alite.vis.VisManager;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author F-I-D-O
 */
public class DefaultVisioInitializer implements VisioInitializer{
	
	private final PedestrianNetwork pedestrianNetwork;
	
	private final BikewayNetwork bikewayNetwork;
	
	private final HighwayNetwork highwayNetwork;
	
	private final TramwayNetwork tramwayNetwork;
	
	private final MetrowayNetwork metrowayNetwork;
	
	private final RailwayNetwork railwayNetwork;
	
	private final AgentStorage agentStorage;
	
	private final VehicleStorage vehicleStorage;
	
	private final AgentPositionModel agentPositionModel;
	
	private final VehiclePositionModel vehiclePositionModel;
	
	private final AllNetworkNodes allNetworkNodes;
	
	private final SimulationCreator simulationCreator;

	@Inject
	public DefaultVisioInitializer(PedestrianNetwork pedestrianNetwork, BikewayNetwork bikewayNetwork, 
			HighwayNetwork highwayNetwork, TramwayNetwork tramwayNetwork, MetrowayNetwork metrowayNetwork, 
			RailwayNetwork railwayNetwork, AgentStorage agentStorage, 
			VehicleStorage vehicleStorage, AgentPositionModel agentPositionModel, 
			VehiclePositionModel vehiclePositionModel, AllNetworkNodes allNetworkNodes, 
			SimulationCreator simulationCreator) {
		this.pedestrianNetwork = pedestrianNetwork;
		this.bikewayNetwork = bikewayNetwork;
		this.highwayNetwork = highwayNetwork;
		this.tramwayNetwork = tramwayNetwork;
		this.metrowayNetwork = metrowayNetwork;
		this.railwayNetwork = railwayNetwork;
		this.agentStorage = agentStorage;
		this.vehicleStorage = vehicleStorage;
		this.agentPositionModel = agentPositionModel;
		this.vehiclePositionModel = vehiclePositionModel;
		this.allNetworkNodes = allNetworkNodes;
		this.simulationCreator = simulationCreator;
	}
	
	

	@Override
	public void initialize(Simulation simulation, Projection projection) {
        initGraphLayers(projection);
        initLayersBeforeEntityLayers();
        initEntityLayers(simulation, projection);
		initLayersAfterEntityLayers();
	}
	
	private <TNode extends Node, TEdge extends Edge> VisGraph wrapGraph(Graph<TNode, TEdge> graph) {

        List<Node> nodes = new ArrayList<>(graph.getAllNodes());
        Map<Integer, Node> nodesWithIds = new HashMap<>();
        for (Node node : nodes) {
            nodesWithIds.put(node.id, node);
        }

        return new VisGraph(nodesWithIds, nodes, new ArrayList<>(graph.getAllEdges()));
    }

    protected void initGraphLayers(Projection projection) {
        VisManager.registerLayer(VisGraphLayer.create(wrapGraph(pedestrianNetwork.getNetwork()), Color.GRAY, 2, 
				Color.GRAY, 2, projection));
        VisManager.registerLayer(VisGraphLayer.create(wrapGraph(bikewayNetwork.getNetwork()), Color.DARK_GRAY, 2, 
				Color.DARK_GRAY, 2, projection));
        VisManager.registerLayer(VisGraphLayer.create(wrapGraph(highwayNetwork.getNetwork()), Color.BLACK, 4,
				Color.BLACK, 2, projection));
        VisManager.registerLayer(VisGraphLayer.create(wrapGraph(tramwayNetwork.getNetwork()), Color.ORANGE, 3, 
				Color.ORANGE, 5, projection));
        VisManager.registerLayer(VisGraphLayer.create(wrapGraph(metrowayNetwork.getNetwork()), Color.RED, 3, Color.RED, 
				5, projection));
        VisManager.registerLayer(VisGraphLayer.create(wrapGraph(railwayNetwork.getNetwork()), Color.MAGENTA, 3, 
				Color.MAGENTA, 5, projection));
    }

    protected void initEntityLayers(Simulation simulation, Projection projection) {
        MultipleDrawListener drawListener = new MultipleDrawListener(simulation, 1000, 40);
        
        EntityStorage<AgentPolisEntity> entityStorage = new EntityStorage<>();

        for (String entityId : agentStorage.getEntityIds()) {
            entityStorage.addEntity(agentStorage.getEntityById(entityId));
        }

        for (String entityId : vehicleStorage.getEntityIds()) {
            entityStorage.addEntity(vehicleStorage.getEntityById(entityId));
        }

        VisManager.registerLayer(VisEntityLayer.createSynchronized(entityStorage, agentPositionModel, 
				vehiclePositionModel, allNetworkNodes.getAllNetworkNodes(), drawListener, 
				simulationCreator.getEntityStyles(), projection));
    }

    protected void initLayersAfterEntityLayers() {
        
    }

    protected void initLayersBeforeEntityLayers() {
        
    }
	
}
