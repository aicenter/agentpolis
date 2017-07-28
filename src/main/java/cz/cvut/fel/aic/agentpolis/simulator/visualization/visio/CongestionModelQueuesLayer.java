package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.CongestionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.Link;
import cz.agents.alite.vis.Vis;
import cz.agents.alite.vis.layer.AbstractLayer;
import cz.cvut.fel.aic.geographtools.Graph;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by martin on 6/27/17.
 */
@Singleton
public class CongestionModelQueuesLayer extends AbstractLayer {
    private static final float EDGE_WIDTH = 8;
    private final Graph<SimulationNode, SimulationEdge> graph;
    private PositionUtil positionUtil;
    private CongestionModel congestionModel;

    @Inject
    public CongestionModelQueuesLayer(PositionUtil positionUtil, CongestionModel congestionModel, HighwayNetwork network) {

        this.positionUtil = positionUtil;
        this.congestionModel = congestionModel;
        this.graph = network.getNetwork();
    }

    @Override
    public void paint(Graphics2D canvas) {
        canvas.setStroke(new BasicStroke(EDGE_WIDTH));

        Dimension dim = Vis.getDrawingDimension();
        Rectangle2D drawingRectangle = new Rectangle(dim);

        paintQueues(canvas, drawingRectangle);
    }

    private void paintQueues(Graphics2D canvas, Rectangle2D drawingRectangle) {
        for (Link link : congestionModel.getLinks()) {
            SimulationEdge edge = link.getEdge();
            double length = 0;
            double usedCapacity = 0;
            double sumLength = 0;
            double sumUsedCapacity = 0;
            for (Lane lane : link.getLanes()) {
                length = Math.max(length, lane.getQueueLength());
                sumLength += lane.getQueueLength();
                usedCapacity = Math.max(usedCapacity, lane.getUsedLaneCapacityInMeters());
                sumUsedCapacity += lane.getUsedLaneCapacityInMeters();
            }
            paintBarOnEdge(canvas, drawingRectangle, edge, usedCapacity, sumUsedCapacity, Color.LIGHT_GRAY);
            paintBarOnEdge(canvas, drawingRectangle, edge, length, sumLength, Color.RED);
        }

    }

    private void paintBarOnEdge(Graphics2D canvas, Rectangle2D drawingRectangle, SimulationEdge edge, double length, double sumLength, Color color) {
        int edgeLength = edge.getLength();
        Point2d from = positionUtil.getCanvasPosition(graph.getNode(edge.fromId));
        Point2d to = positionUtil.getCanvasPosition(graph.getNode(edge.toId));
        Vector2d vector = new Vector2d(to.x - from.x, to.y - from.y);
        vector.scale(length / edgeLength);

        canvas.setColor(color);
        Line2D line2d = new Line2D.Double(to.x - vector.x, to.y - vector.y, to.x, to.y);
        if (line2d.intersects(drawingRectangle)) {
            canvas.draw(line2d);
        }
        canvas.setColor(Color.BLACK);
        canvas.drawString("max: "+length + "m, sum: "+sumLength, (float) (to.x - vector.x), (float) (to.y - vector.y));
        canvas.setColor(color);
    }
}
