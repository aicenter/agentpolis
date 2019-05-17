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
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.alite.vis.layer.AbstractLayer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author fido
 */
@Singleton
public class GridLayer extends AbstractLayer{
	
	private static final int GRID_WIDTH = 1;
	
	private static final int ZOOM_CHANGE_FACTOR = 10;
	
	private static final int MIN_SQUARES_PER_HEIGHT = 3;
	
	private int distance;

	@Inject
	public GridLayer() {
//		this.distance = 100;
	}
	
	
	
//	private final Grap
	
	@Override
	public void paint(Graphics2D canvas) {
		canvas.setStroke(new BasicStroke(GRID_WIDTH));
		canvas.setColor(Color.GREEN);

		Dimension dim = Vis.getDrawingDimension();
		Rectangle2D drawingRectangle = new Rectangle(dim);
		
		distance = computeDistance(drawingRectangle);
		
		drawXLines(canvas, drawingRectangle);
		drawYLines(canvas, drawingRectangle);
	}

	private void drawXLines(Graphics2D canvas, Rectangle2D drawingRectangle) {
		double leftBorderLongitudeProjected = Vis.transInvX(0);
		double rightBorderLongitudeProjected = Vis.transInvX((int) drawingRectangle.getMaxX());
		
		double minLongitudeProjected = Math.min(leftBorderLongitudeProjected, rightBorderLongitudeProjected);
		double maxLongitudeProjected = Math.max(leftBorderLongitudeProjected, rightBorderLongitudeProjected);
		
		int x = getStartCoordinate((int) (minLongitudeProjected * 100));
		
		while(x < maxLongitudeProjected * 100){
			drawXLine(x, drawingRectangle, canvas);
			x += distance;
		}
	}
	
	private void drawYLines(Graphics2D canvas, Rectangle2D drawingRectangle) {
		double topBorderLatidudeProjected = Vis.transInvY(0);
		double bottomBorderLatidudeProjected = Vis.transInvY((int) drawingRectangle.getMaxY());
		
		double minLatitudeProjected = Math.min(topBorderLatidudeProjected, bottomBorderLatidudeProjected);
		double maxLatitudeProjected = Math.max(topBorderLatidudeProjected, bottomBorderLatidudeProjected);
		
		int y = getStartCoordinate((int) (minLatitudeProjected * 100));
		
		while(y < maxLatitudeProjected * 100){
			drawYLine(y, drawingRectangle, canvas);
			y += distance;
		}
	}

	private int getStartCoordinate(int minCoordinateProjected) {
		while (true) {			
			if(minCoordinateProjected % (distance) == 0){
				return minCoordinateProjected;
			}
			minCoordinateProjected++;
		}
	}

	private void drawXLine(int x,Rectangle2D drawingRectangle, Graphics2D canvas) {
		double finalX = Vis.transX((double) x / 100);
		double minY = 0;
		double maxY = drawingRectangle.getMaxY();
		Line2D line2d = new Line2D.Double(finalX, minY, finalX, maxY);
		canvas.draw(line2d);
	}
	
	private void drawYLine(int y,Rectangle2D drawingRectangle, Graphics2D canvas) {
		double finalY = Vis.transY((double) y / 100);
		double minX = 0;
		double maxX = drawingRectangle.getMaxX();
		Line2D line2d = new Line2D.Double(minX, finalY, maxX, finalY);
		canvas.draw(line2d);
	}

	private int computeDistance(Rectangle2D drawingRectangle) {
		double topBorderLatidudeProjected = Vis.transInvY(0);
		double bottomBorderLatidudeProjected = Vis.transInvY((int) drawingRectangle.getMaxY());
		
		int heigh = (int) (Math.abs(topBorderLatidudeProjected - bottomBorderLatidudeProjected) * 100);
		
		int maxDistance = heigh / MIN_SQUARES_PER_HEIGHT;
		
		int finalDistance = (int) Math.pow(ZOOM_CHANGE_FACTOR, Math.floor(Math.log(maxDistance) / Math.log(ZOOM_CHANGE_FACTOR)));
//			   10**floor(log(x,10))
		
		return finalDistance;
	}
}
