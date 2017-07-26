/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.graph.VisGraph;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.graph.VisGraphLayer;
import cz.agents.alite.simulation.Simulation;
import cz.agents.alite.vis.VisManager;
import cz.agents.alite.vis.layer.common.FpsLayer;
import cz.agents.alite.vis.layer.common.HelpLayer;
import cz.agents.alite.vis.layer.common.VisInfoLayer;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.BikewayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.MetrowayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.PedestrianNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.RailwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TramwayNetwork;

import java.awt.*;
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
    
    private final SimulationControlLayer simulationControlLayer;
    
    

	@Inject
	public DefaultVisioInitializer(PedestrianNetwork pedestrianNetwork, BikewayNetwork bikewayNetwork, 
			HighwayNetwork highwayNetwork, TramwayNetwork tramwayNetwork, MetrowayNetwork metrowayNetwork, 
			RailwayNetwork railwayNetwork, SimulationControlLayer simulationControlLayer) {
		this.pedestrianNetwork = pedestrianNetwork;
		this.bikewayNetwork = bikewayNetwork;
		this.highwayNetwork = highwayNetwork;
		this.tramwayNetwork = tramwayNetwork;
		this.metrowayNetwork = metrowayNetwork;
		this.railwayNetwork = railwayNetwork;
        this.simulationControlLayer = simulationControlLayer;
	}
	
	

	@Override
	public void initialize(Simulation simulation, Projection projection) {
        initWindow();
        initGraphLayers(projection);
        initLayersBeforeEntityLayers();
        initEntityLayers(simulation, projection);
		initLayersAfterEntityLayers();
        initInfoLayers();
	}
	
	protected <TNode extends Node, TEdge extends Edge> VisGraph wrapGraph(Graph<TNode, TEdge> graph) {

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

    }

    protected void initLayersAfterEntityLayers() {
        
    }

    protected void initLayersBeforeEntityLayers() {
        
    }

    protected void initInfoLayers() {
        VisManager.registerLayer(HelpLayer.create());
        VisManager.registerLayer(FpsLayer.create());
        VisManager.registerLayer(VisInfoLayer.create());
        
        // VisManager.registerLayer(LogoLayer.create(ResourceReader.getPathToResource("/img/atg_blue.png")));
        
        VisManager.registerLayer(simulationControlLayer);
    }

    private void initWindow() {
        final int windowHight = 0;
        final int windowWidth = 0;

        VisManager.setInitParam("Agentpolis operator", windowWidth, windowHight);
        VisManager.init();

//        final double zoomFactor = windowWidth / projection.sceneWidth;
        
        final double zoomFactor = 1.2;

        VisManager.setSceneParam(new VisManager.SceneParams() {

            @Override
            public double getDefaultZoomFactor() {
                return zoomFactor;
            }

//            @Override
//            public Rectangle getWorldBounds() {
//                return new Rectangle(projection.sceneWidth, projection.sceneHeight);
//            }

        });
    }
	
}
