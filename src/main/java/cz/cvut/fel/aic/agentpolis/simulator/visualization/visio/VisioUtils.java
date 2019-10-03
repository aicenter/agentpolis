/* 
 * Copyright (C) 2019 Czech Technical University in Prague.
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author fido
 */
public class VisioUtils {
	
	public static void printTextWithBackgroud(Graphics2D canvas, String text, Point position, Color textColor, 
			Color backgroundColor){
		
		// background
		FontMetrics fm = canvas.getFontMetrics();
		Rectangle2D rect = fm.getStringBounds(text, canvas);
		canvas.setColor(backgroundColor);
		canvas.fillRect(position.x, position.y - fm.getAscent(), (int) rect.getWidth(), 
				(int) rect.getHeight());

		// text
		canvas.setColor(textColor);
		canvas.drawString(text, position.x, position.y);
	}
	
	/**
	 * Returns {@code true} iff the rectangle represented by given coordinates
	 * {@code x1,y1,x2,y2} overlaps the given {@code dimension}
	 * 
	 * @param x1
	 *			x coordinate of one corner
	 * @param y1
	 *			y coordinate of one corner
	 * @param x2
	 *			x coordinate of the opposite corner
	 * @param y2
	 *			y coordinate of the opposite corner
	 * @param dimension
	 * @return
	 */
	public static boolean rectangleOverlaps(int x1, int y1, int x2, int y2, Dimension dimension) {
		assert x1 <= x2 && y1 <= y2;
		return x2 > 0 && x1 < dimension.width && y2 > 0 && y1 < dimension.height;
	}
}
