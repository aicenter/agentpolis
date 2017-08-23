/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.agents.alite.vis.Vis;
import cz.agents.alite.vis.layer.AbstractLayer;
import cz.cvut.fel.aic.geographtools.Graph;

import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author fido
 */
@Singleton
public class HighwayLayer extends AbstractLayer{
    
    private static final int DEFAULT_EDGE_WIDTH = 8;
    
    
    
    
    protected final PositionUtil positionUtil;
    
    protected final Graph<SimulationNode,SimulationEdge> graph;
    
    
    private int edgeWidth = DEFAULT_EDGE_WIDTH;
    
    
    
    
    @Inject(optional=true)
    public void setEdgeWidth(@Named("HighwayLayer edge width") int width) {
        this.edgeWidth = width;
    }
    
    
    
    
    @Inject
    public HighwayLayer(HighwayNetwork highwayNetwork, PositionUtil positionUtil) {
        this.positionUtil = positionUtil;
        graph = highwayNetwork.getNetwork();
    }
    
    
    
    
    @Override
    public void paint(Graphics2D canvas) {
        canvas.setStroke(new BasicStroke(Vis.transW(edgeWidth)));
        canvas.setColor(Color.BLACK);

        Dimension dim = Vis.getDrawingDimension();
        Rectangle2D drawingRectangle = new Rectangle(dim);
        
        paintGraph(canvas, drawingRectangle);
    }

    protected void paintGraph(Graphics2D canvas, Rectangle2D drawingRectangle) {
        for (SimulationEdge edge : graph.getAllEdges()) {
            Point2d from = positionUtil.getCanvasPosition(graph.getNode(edge.fromId));
            Point2d to = positionUtil.getCanvasPosition(graph.getNode(edge.toId));
            Line2D line2d = new Line2D.Double(from.x, from.y, to.x, to.y);
            if (line2d.intersects(drawingRectangle)) {
                canvas.draw(line2d);
            }
        }
    }
}
