package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.graph;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.vecmath.Point3d;

import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.Projection;
import cz.agents.agentpolis.utils.key.Key;
import cz.agents.alite.vis.element.Line;
import cz.agents.alite.vis.element.Point;
import cz.agents.alite.vis.element.aggregation.LineElements;
import cz.agents.alite.vis.element.aggregation.PointElements;
import cz.agents.alite.vis.element.implemetation.LineImpl;
import cz.agents.alite.vis.element.implemetation.PointImpl;
import cz.agents.alite.vis.layer.AbstractLayer;
import cz.agents.alite.vis.layer.GroupLayer;
import cz.agents.alite.vis.layer.VisLayer;
import cz.agents.alite.vis.layer.terminal.LineLayer;
import cz.agents.alite.vis.layer.terminal.PointLayer;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Node;

/**
 * The GraphLayer draws edges (points) and nodes (lines) of a graph defined by
 * the {@link VisGraph} interface.
 * 
 * @author Libor Wagner
 * @author Antonin Komenda
 */
public class VisGraphLayer extends AbstractLayer {

    private VisGraphLayer() {
    }

    public static VisLayer create(final VisGraph graph, final Color edgeColor, final int edgeWidth,
            final Color nodeColor, final int nodeSize, final Projection projection) {

        GroupLayer group = GroupLayer.create();
        group.setHelpOverrideString("Graph layer");

        group.addSubLayer(LineLayer.create(new LineElements() {
            private Collection<LineImpl> lines = VisGraphLayer.createLines(graph, projection);

            public Iterable<? extends Line> getLines() {
                return lines;
            }

            public Color getColor() {
                return edgeColor;
            }

            public int getStrokeWidth() {
                return edgeWidth;
            }
        }));

        group.addSubLayer(PointLayer.create(new PointElements() {
            private Collection<PointImpl> points = VisGraphLayer.createPoints(graph, projection);

            public int getStrokeWidth() {
                return nodeSize;
            }

            public Color getColor() {
                return nodeColor;
            }

            public Iterable<? extends Point> getPoints() {
                return points;
            }
        }));

        return group;
    }

    private static Collection<LineImpl> createLines(VisGraph graph, Projection projection) {
        Map<Key, Boolean> drawnEdgeMap = new HashMap<Key, Boolean>();
        LinkedList<LineImpl> lines = new LinkedList<LineImpl>();
        for (Edge edge : graph.getEdges()) {
            Key key = new Key(edge.fromId, edge.toId);
            if (drawnEdgeMap.containsKey(key)) {
                continue;
            }

            drawnEdgeMap.put(key, true);

            Node fromL = graph.getNode(edge.fromId);
            Node toL = graph.getNode(edge.toId);

            Node[] points = new Node[] { fromL, toL };
            for (int i = 1; i < points.length; i++) {
                Point3d from = projection.project(points[i - 1]);
                Point3d to = projection.project(points[i]);
                lines.add(new LineImpl(from, to));
            }

        }
        return lines;
    }

    private static Collection<PointImpl> createPoints(VisGraph graph, Projection projection) {
        LinkedList<PointImpl> points = new LinkedList<PointImpl>();
        for (Node node : graph.getNodes()) {
            points.add(new PointImpl(projection.project(node)));
        }
        return points;
    }

}
