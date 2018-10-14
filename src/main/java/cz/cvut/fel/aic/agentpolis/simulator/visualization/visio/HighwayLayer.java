/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.alite.vis.layer.AbstractLayer;
import cz.cvut.fel.aic.geographtools.Graph;

import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

/**
 * @author fido
 */
@Singleton
public class HighwayLayer extends AbstractLayer {

    private static final int DEFAULT_EDGE_WIDTH = 8;


    protected final PositionUtil positionUtil;

    protected final Graph<SimulationNode, SimulationEdge> graph;


    private int edgeWidth = DEFAULT_EDGE_WIDTH;


    @Inject(optional = true)
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
            GeneralPath path = new GeneralPath();
            Point2d location = positionUtil.getCanvasPosition(edge.shape.from());
            path.moveTo(location.x, location.y);
            edge.shape.stream().skip(1).map(positionUtil::getCanvasPosition).forEach(d -> path.lineTo(d.x, d.y));
            if (path.intersects(drawingRectangle)) {
                canvas.draw(path);
            }
        }
    }
}
