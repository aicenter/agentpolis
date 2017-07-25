/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.HighwayNetwork;
import cz.agents.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.agents.alite.vis.layer.AbstractLayer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.vecmath.Point2d;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author fido
 */
@Singleton
public class NodeIdLayer extends AbstractLayer{
    
    private final HighwayNetwork highwayNetwork;
    
    private final PositionUtil positionUtil;
    
    private final LinkedList<Integer> highLightedNodes;

    
    
    @Inject
    public NodeIdLayer(HighwayNetwork highwayNetwork, PositionUtil positionUtil) {
        this.highwayNetwork = highwayNetwork;
        this.positionUtil = positionUtil;
        highLightedNodes = new LinkedList<>();
        
        highLightedNodes.add(12775);
//        highLightedNodes.add(39675);
//        
//        highLightedNodes.add(37832);
//        highLightedNodes.add(19420);
    }

    
    
    
    @Override
    public void paint(Graphics2D canvas) {
        canvas.setColor(Color.BLUE);
        canvas.setFont(new Font("TimesRoman", Font.BOLD, 15)); 
        List<SimulationNode> highlightedNodes = new LinkedList<>();
        for (SimulationNode node : highwayNetwork.getNetwork().getAllNodes()) {
            Font f = null;
            if(highLightedNodes.contains(node.getId())){
                highlightedNodes.add(node);
                continue;
                 
            }
            
            Point2d nodePoint = positionUtil.getCanvasPosition(node);
            canvas.drawString(Integer.toString(node.getId()), (int) nodePoint.x, (int) nodePoint.y);
        }
        
        canvas.setColor(Color.GREEN);
        canvas.setFont(new Font("TimesRoman", Font.BOLD, 25));
        for (SimulationNode highlightedNode : highlightedNodes) {
            Point2d nodePoint = positionUtil.getCanvasPosition(highlightedNode);
            canvas.drawString(Integer.toString(highlightedNode.getId()), (int) nodePoint.x, (int) nodePoint.y);
        }
        
        canvas.setColor(Color.BLUE);
        canvas.setFont(new Font("TimesRoman", Font.BOLD, 15));
    }
    
    
    
    
}
