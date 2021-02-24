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

import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.alite.vis.VisManager;
import cz.cvut.fel.aic.alite.vis.layer.AbstractLayer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ScreenRecordingLayer extends AbstractLayer {

	private static final int BTN_WIDTH = 75;
	private static final int BTN_HEIGHT = 30;

	private int uixposition, uiyposition;
	private boolean recording = false;
	private Color backgroundColor;
	private Color textColor;
	private Dimension dimension;
	private Graphics2D currentCanvas;
	private MouseListener mouseListener;

	public ScreenRecordingLayer(){
		setDefaultColors();
	}

	@Override
	public void init(Vis vis) {
		super.init(vis);
		mouseListener = getMouseListener();
		vis.addMouseListener(mouseListener);
	}

	@Override
	public void deinit(Vis vis) {
		super.deinit(vis);
		vis.removeMouseListener(mouseListener);
	}

	@Override
	public void paint(Graphics2D canvas) {
		currentCanvas = canvas;
		dimension = Vis.getDrawingDimension();

		canvas.setStroke(new BasicStroke(1));

		if(recording){
			redrawButton("Stop");
		} else {
			redrawButton("Record");
		}
	}

	private void redrawButton(String text){
		uixposition = dimension.width - BTN_WIDTH - 425;
		uiyposition = dimension.height - BTN_HEIGHT - 30;

		Color previousColor = currentCanvas.getColor();
		Font previousFont = currentCanvas.getFont();

		currentCanvas.setFont(new Font("Arial", Font.BOLD, 14));
		currentCanvas.setColor(backgroundColor);
		currentCanvas.fillRect(uixposition, uiyposition, BTN_WIDTH, BTN_HEIGHT);
		currentCanvas.setColor(textColor);
		currentCanvas.drawString(text, uixposition + 10, uiyposition + 20);

		currentCanvas.setColor(previousColor);
		currentCanvas.setFont(previousFont);
	}

	public MouseListener getMouseListener(){
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getX() > uixposition && e.getX() < uixposition + BTN_WIDTH && e.getY() > uiyposition && e.getY() < uiyposition + BTN_HEIGHT) {
					if (recording) {
						recording = false;
						VisManager.stopVideoRecording();
					} else {
						recording = true;
						int width = Vis.getDrawingDimension().width - Vis.getDrawingDimension().width % 8;
						int height = Vis.getDrawingDimension().height - Vis.getDrawingDimension().height % 8;
						VisManager.startVideoRecording(width, height);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		};
	}

	private void setDefaultColors() {
		backgroundColor = new Color(0, 0, 0, 170);
		textColor = new Color(255, 255, 255, 255);
	}

}
