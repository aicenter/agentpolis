/*
 * Copyright (c) 2021 Czech Technical University in Prague.
 *
 * This file is part of Agentpolis project.
 * (see https://github.com/aicenter/agentpolis).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Singleton;
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.alite.vis.VisManager;
import cz.cvut.fel.aic.alite.vis.layer.AbstractLayer;
import cz.cvut.fel.aic.alite.vis.layer.VisLayer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.vecmath.Point2d;


@Singleton
public class LayerManagementLayer extends AbstractLayer {
	private boolean isVisible;
	private MouseListener mouseListener;
	private Color backgroundColor;
	private Color textColor;
	private List<ManageableLayer> manageableLayers;
	private int uixposition;
	private int uiyposition;

	public LayerManagementLayer() {
		isVisible = false;
		setDefaultColors();
		manageableLayers = new ArrayList<>();
	}

	public ManageableLayer createManageableLayer(String name, VisLayer layer) {
		ManageableLayer x = new ManageableLayer(name, layer);
		manageableLayers.add(x);
		return x;
	}

	@Override
	public void paint(Graphics2D canvas) {
		uixposition = Vis.getDrawingDimension().width - 330;
		uiyposition = Vis.getDrawingDimension().height - 60;
		canvas.setStroke(new BasicStroke(1));
		if (isVisible) {
			paintBottomRectangle(canvas, "Hide layers management");
			paintArrowDownwards(canvas);
			paintTopRectangle(canvas);
		} else {
			paintBottomRectangle(canvas, "Show layers management");
			paintArrowUpwards(canvas);
		}
	}

	public void paintBottomRectangle(Graphics2D canvas, String message) {
		canvas.setColor(backgroundColor);
		canvas.fillRect(uixposition, uiyposition, 300, 30);

		Font oldFont = canvas.getFont();
		canvas.setFont(new Font("Arial", 1, 14));
		canvas.setColor(textColor);
		canvas.drawString(message, uixposition+50, uiyposition+20);

		canvas.setFont(oldFont);
	}

	public void paintTopRectangle(Graphics2D canvas) {
		canvas.setColor(backgroundColor);
		int height = manageableLayers.size() * 25 + 5;
		canvas.fillRect(uixposition, uiyposition - 10 - height, 300, height);

		Font oldFont = canvas.getFont();
		canvas.setFont(new Font("Arial", 1, 14));
		canvas.setColor(textColor);
		for( int i = 0; i < manageableLayers.size(); i++ ) {
			canvas.drawString(manageableLayers.get(i).getName(), uixposition + 50, uiyposition - 20 - 25*i);
			if (manageableLayers.get(i).isVisible()){
				canvas.fillRect(uixposition + 22,uiyposition - 34 - 25*i, 18, 18);

			} else {
				canvas.drawRect(uixposition + 22,uiyposition - 34 - 25*i, 18, 18);
			}
			if (i == 0) {
				paintLayerOrderUpArrow(canvas, uiyposition - 34);
				continue;
			}
			if (i == manageableLayers.size() - 1) {
				paintLayerOrderDownArrow(canvas, uiyposition - 34 - 25*i);
				continue;
			}
			paintLayerOrderBothArrows(canvas, uiyposition - 34 - 25*i);
		}
		canvas.setFont(oldFont);

	}

	public void paintArrowUpwards(Graphics2D canvas) {
		int[] xPoints = {uixposition+15, uixposition+25, uixposition+35};
		int[] yPoints = {uiyposition+22, uiyposition+8, uiyposition+22};
		canvas.fillPolygon(xPoints, yPoints, 3);
	}

	public void paintArrowDownwards(Graphics2D canvas) {
		int[] xPoints = {uixposition+15, uixposition+25, uixposition+35};
		int[] yPoints = {uiyposition+8, uiyposition+22, uiyposition+8};
		canvas.fillPolygon(xPoints, yPoints, 3);
	}

	public void paintLayerOrderBothArrows(Graphics2D canvas, int ypos) {
		int[] xPoints = {uixposition+5, uixposition+11, uixposition+17};
		int[] yPointsUp = {ypos+8, ypos, ypos+8};
		int[] yPointsDown = {ypos+10, ypos+18, ypos+10};
		canvas.fillPolygon(xPoints, yPointsUp, 3);
		canvas.fillPolygon(xPoints, yPointsDown, 3);
	}

	public void paintLayerOrderUpArrow(Graphics2D canvas, int ypos) {
		int[] xPoints = {uixposition+5, uixposition+11, uixposition+17};
		int[] yPointsUp = {ypos+8, ypos, ypos+8};
		canvas.fillPolygon(xPoints, yPointsUp, 3);
	}

	public void paintLayerOrderDownArrow(Graphics2D canvas, int ypos) {
		int[] xPoints = {uixposition+5, uixposition+11, uixposition+17};
		int[] yPointsDown = {ypos+10, ypos+18, ypos+10};
		canvas.fillPolygon(xPoints, yPointsDown, 3);
	}


	private void setDefaultColors() {
		backgroundColor = new Color(0, 0, 0, 170);
		textColor = new Color(255, 255, 255, 255);
	}

	private boolean validateLayersManagementToggleClick(Point2d click) {
		if (click.getX() > uixposition && click.getX() < uixposition+300 && click.getY() > uiyposition && click.getY() < uiyposition+30 ) {
			isVisible = !isVisible;
			return true;
		}
		return false;
	}

	private boolean validateLayerToggleClick(Point2d click) {
		if (click.getX() > uixposition+22 && click.getX() < uixposition+40 && click.getY() > uiyposition - 34 - 25*manageableLayers.size() && click.getY() < uiyposition - 16 ) {
			for (int i = 0; i < manageableLayers.size(); i++) {
				if (click.getY() > uiyposition - 34 - 25*i && click.getY() < uiyposition - 16 - 25*i ) {
					manageableLayers.get(i).toggle();
					return true;
				}
			}
		}
		return false;
	}

	private boolean validateLayerMoveUpClick(Point2d click) {
		if (click.getX() > uixposition + 5 && click.getX() < uixposition + 17 && click.getY() > uiyposition - 34 - 25*manageableLayers.size() && click.getY() < uiyposition - 16 ) {
			for (int i = 0; i < manageableLayers.size(); i++) {
				if (click.getY() > uiyposition - 34 - 25*i && click.getY() < uiyposition - 26 - 25*i ) {
					if (i != manageableLayers.size() - 1) {
						swapLayers(i, i+1);
					}
					return true;
				}
			}
		}
		return false;
	}

	private boolean validateLayerMoveDownClick(Point2d click) {
		if (click.getX() > uixposition + 5 && click.getX() < uixposition + 17 && click.getY() > uiyposition - 34 - 25*manageableLayers.size() && click.getY() < uiyposition - 16 ) {
			for (int i = 0; i < manageableLayers.size(); i++) {
				if (click.getY() > uiyposition - 24 - 25*i && click.getY() < uiyposition - 16 - 25*i ) {
					if (i != 0) {
						swapLayers(i, i-1);
					}
					return true;
				}
			}
		}
		return false;
	}

	private void swapLayers(int x, int y) {
		VisManager.swapLayers((VisLayer) manageableLayers.get(x), (VisLayer)manageableLayers.get(y));
		Collections.swap(manageableLayers, x, y);
	}


	public void init(Vis vis) {
		super.init(vis);
		this.mouseListener = getMouseListener();
		vis.addMouseListener(this.mouseListener);
	}

	public void deinit(Vis vis) {
		super.deinit(vis);
		vis.removeMouseListener(this.mouseListener);
	}

	public MouseListener getMouseListener() {
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
					Point2d click = new Point2d(mouseEvent.getX(), mouseEvent.getY());

					if (validateLayersManagementToggleClick(click)){
						return;
					}
					if (validateLayerToggleClick(click)){
						return;
					}
					if(validateLayerMoveUpClick(click)){
						return;
					}
					validateLayerMoveDownClick(click);
				}
			}

			@Override
			public void mouseEntered(MouseEvent me) {

			}

			@Override
			public void mouseExited(MouseEvent me) {

			}

			@Override
			public void mousePressed(MouseEvent me) {

			}

			@Override
			public void mouseReleased(MouseEvent me) {

			}
		};
	}
}
