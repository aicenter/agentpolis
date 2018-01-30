/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.MovingEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.EntityStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.load.AllEdgesLoad;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.alite.vis.layer.AbstractLayer;
import cz.cvut.fel.aic.geographtools.Edge;
import cz.cvut.fel.aic.geographtools.Graph;
import edu.mines.jtk.awt.ColorMap;

import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Layer that shows traffic on edges. Two-way edges are split for each direction. Start of each edge is with light blue
 * dot.
 * Refreshing of computed positions in a canvas is only done when something has changed.
 *
 * @author Zdenek Bousa
 */
@Singleton
public class TrafficDensityByDirectionLayer<E extends AgentPolisEntity & MovingEntity, ES extends EntityStorage<E>> extends AbstractLayer {

    private static final int EDGE_WIDTH = 5;

    private static final double MAX_SLACK = 2;
    private static final double MAX_LOAD = MAX_SLACK * 1900 / 60 / 60;


    private final Provider<AllEdgesLoad<E, ES>> allEdgesLoadProvider;

    private final Graph<SimulationNode, SimulationEdge> graph;

    protected ColorMap colorMap;

    private final PositionUtil positionUtil;

    private Dimension dimension;

    private Point2d lastPoint = new Point2d(0, 0);

    private Map<Edge, Edge> twoWayEdges;

    private Map<Edge, SimulationEdge> edgeMapping;

    private Map<Edge, Line2D> edgePosition;


    /**
     * On/Off {@link }
     *
     * @param highwayNetwork
     * @param positionUtil
     * @param allEdgesLoadProvider
     */
    @Inject
    public TrafficDensityByDirectionLayer(HighwayNetwork highwayNetwork, PositionUtil positionUtil,
                                          Provider<AllEdgesLoad<E, ES>> allEdgesLoadProvider) {
        this.positionUtil = positionUtil;
        this.allEdgesLoadProvider = allEdgesLoadProvider;
        graph = highwayNetwork.getNetwork();
        colorMap = new ColorMap(0, MAX_LOAD, ColorMap.HUE_BLUE_TO_RED);

        this.setHelpOverrideString("Traffic density layer by direction");
    }

    @Override
    public void paint(Graphics2D canvas) {
        Dimension dimTemp = Vis.getDrawingDimension();

        // TODO: correctly check for zoom and other visio changes
        // Hacked via change of the calculated position for nodeId
        Point2d point = positionUtil.getCanvasPosition(0);

        if (!point.equals(lastPoint)) {

            // Debug
            // System.out.println(lastPoint);

            lastPoint = point;
            dimension = dimTemp;

            // refresh list of all visible edges
            refreshListOfVisibleEdgesAndMapping();

            // generate list of one-way and two-way edges
            refreshTwoWayEdgesList();

            // regenerate new position for each edge
            refreshEdgesPosition();

        }

        canvas.setStroke(new BasicStroke(EDGE_WIDTH));
        canvas.getClipBounds();

        // refresh load provider
        AllEdgesLoad allEdgesLoad = allEdgesLoadProvider.get();

        for (Edge edge : edgePosition.keySet()) {
            canvas.setColor(getColorForEdge(allEdgesLoad, edgeMapping.get(edge)));
            canvas.draw(edgePosition.get(edge));

            //Debug - show begin of the line
            Line2D line = edgePosition.get(edge);
            int load = allEdgesLoad.getLoadPerEdge(edgeMapping.get(edge).getUniqueId());
            int textPositionX = (int) ((line.getX1() * 1.2 + line.getX2() * 0.8) / 2);
            int textPositionY = (int) ((line.getY1() * 1.2 + line.getY2() * 0.8) / 2);
            float loadPerS = load / (edge.getLength() / edgeMapping.get(edge).allowedMaxSpeedInMpS);
            String text = String.format("cars = %s ,  %.2f cars/s", load, loadPerS);
            canvas.drawString(text, textPositionX, textPositionY);
            canvas.setColor(Color.CYAN);
            canvas.fillRect((int) line.getX1(), (int) line.getY1(), 1, 1);

        }
    }

    /**
     * Refresh or create list of edges that intersects with Rectangle(dimension)
     * Also maintenance mapping between Edge and SimulationEdge
     */
    private void refreshListOfVisibleEdgesAndMapping() {
        edgeMapping = new HashMap<>();
        Rectangle2D drawingRectangle = new Rectangle(dimension);

        for (SimulationEdge edge : graph.getAllEdges()) {
            Point2d from = positionUtil.getCanvasPosition(graph.getNode(edge.fromId));
            Point2d to = positionUtil.getCanvasPosition(graph.getNode(edge.toId));
            Line2D line2d = new Line2D.Double(from.x, from.y, to.x, to.y);

            if (line2d.intersects(drawingRectangle)) {
                edgeMapping.put(new Edge(edge.fromId, edge.toId, edge.length), edge);
            }
        }
    }

    /**
     * Create Map of edges, where one-way edges have null value, two-eay edges have its partner edge in opposite
     * direction as an value.
     */
    private void refreshTwoWayEdgesList() {
        twoWayEdges = new HashMap<>();

        for (Edge edge : edgeMapping.keySet()) {
            Edge edgeOpposite = new Edge(edge.toId, edge.fromId, edge.length);
            if (twoWayEdges.containsKey(edgeOpposite)) {
                twoWayEdges.put(edge, edgeOpposite);
            } else {
                twoWayEdges.put(edge, null);
            }
        }
    }

    /**
     * Refresh all pairs Edge-Line2D in edgePosition. It uses twoWayEdges map.
     */
    private void refreshEdgesPosition() {
        edgePosition = new HashMap<>();

        for (Edge edge : twoWayEdges.keySet()) {
            Edge edge2 = twoWayEdges.get(edge);
            if (edge2 != null && (!edgePosition.containsKey(edge) || !edgePosition.containsKey(edge2))) {
                calculateTwoWayEdgesPosition(edge, edge2);
            } else if (edge2 == null) {
                Point2d from = positionUtil.getCanvasPosition(graph.getNode(edge.fromId));
                Point2d to = positionUtil.getCanvasPosition(graph.getNode(edge.toId));

                //Debug - do not show one-way edges
                //Line2D line2d = new Line2D.Double(0, 0, 0, 0);
                Line2D line2d = new Line2D.Double(from.x, from.y, to.x, to.y);
                edgePosition.put(edge, line2d);
            }
        }
    }

    /**
     * Calculate position for two way edge.
     *
     * @param edge1 one direction
     * @param edge2 opposite direction
     */
    private void calculateTwoWayEdgesPosition(Edge edge1, Edge edge2) {
        // move
        double move = 2 * EDGE_WIDTH + 1;

        // get canvas positions
        Point2d A = positionUtil.getCanvasPosition(graph.getNode(edge1.fromId));
        Point2d B = positionUtil.getCanvasPosition(graph.getNode(edge1.toId));

        // calculate move of one of the edge
        double vectorX = B.y - A.y;
        double vectorY = -(B.x - A.x);
        double scaleToUnit = Math.sqrt(Math.pow(vectorX, 2) + Math.pow(vectorY, 2));
        vectorX = (vectorX / scaleToUnit) * move;
        vectorY = (vectorY / scaleToUnit) * move;

        // new positions in canvas
        Line2D line2DE1 = new Line2D.Double(B.x + vectorX, B.y + vectorY, A.x + vectorX, A.y + vectorY); // lane B(from)-A(to)
        Line2D line2DE2 = new Line2D.Double(A.x - vectorX, A.y - vectorY, B.x - vectorX, B.y - vectorY); // lane A(from)-B(to)

        // connect edge with its line
        edgePosition.put(edge1, line2DE2);
        edgePosition.put(edge2, line2DE1);
    }

    /**
     * Edge color depends on number of cars per length.
     *
     * @param allEdgesLoad provides data about edge load
     * @param edge         examined edge
     * @return Color based on load per length(m) or by default gray
     */
    protected Color getColorForEdge(AllEdgesLoad allEdgesLoad, SimulationEdge edge) {
        int id;
        try {
            id = edge.getUniqueId();
        } catch (Exception e) {
            id = -1;
        }
        if (id != -1) {
            double averageLoad = allEdgesLoad.getLoadPerEdge(id);
            double carsPerSecondPerLane = averageLoad / (edge.getLength() / edge.allowedMaxSpeedInMpS * edge.getLanesCount());
            return colorMap.getColor(carsPerSecondPerLane);
        } else {
            return Color.gray;
        }
    }
}
