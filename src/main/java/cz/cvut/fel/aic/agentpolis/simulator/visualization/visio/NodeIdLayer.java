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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.alite.vis.layer.AbstractLayer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;
import javax.vecmath.Point2d;

@Singleton
public class NodeIdLayer extends AbstractLayer{

	private static final int UI_WIDTH = 300;
	private static final int UI_HEIGHT = 120;

	private final VisioPositionUtil positionUtil;
	private final HighwayNetwork highwayNetwork;
	private final LinkedList<Integer> highLightedNodes;

	private int uixposition;
	private int uiyposition;
	private boolean showAllNodes;
	private boolean acceptingInput;

	private Vis vis;
	private Color textColor;
	private Color backgroundColor;
	private Color acceptingTextColor;
	private String input = "";
	private String status;
	private KeyListener keyListener;
	private MouseListener mouseListener;


	@Inject
	public NodeIdLayer(HighwayNetwork highwayNetwork, VisioPositionUtil positionUtil) {
		this.highwayNetwork = highwayNetwork;
		this.positionUtil = positionUtil;

		setDefaultColors();
		highLightedNodes = new LinkedList<>();

		status = "No node highlighted.";
	}

	@Override
	public void init(Vis vis) {
		super.init(vis);
		this.vis = vis;
		this.mouseListener = getMouseListener();
		this.keyListener = getKeyListener();
		vis.addMouseListener(this.mouseListener);
	}

	@Override
	public void deinit(Vis vis) {
		super.deinit(vis);
		vis.removeMouseListener(mouseListener);
	}

	@Override
	public void paint(Graphics2D canvas) {
		Dimension dimension = Vis.getDrawingDimension();
		uixposition = dimension.width - UI_WIDTH - 30;
		uiyposition = dimension.height - UI_HEIGHT - 530;

		canvas.setColor(backgroundColor);
		canvas.fillRect(uixposition, uiyposition, UI_WIDTH, UI_HEIGHT);

		Font oldFont = canvas.getFont();

		canvas.setFont(new Font("Arial", Font.BOLD, 14));
		canvas.setColor(textColor);
		canvas.drawString("Node ID to highlight:", uixposition + 10, uiyposition + 20);
		canvas.drawString("Status: " + status, uixposition + 10, uiyposition + 90);
		canvas.drawString("Show all nodes:", uixposition + 10, uiyposition + 110);

		canvas.setStroke(new BasicStroke(2));
		canvas.drawRect(uixposition + 10, uiyposition + 30, 135, 40);
		canvas.drawRect(uixposition + 155, uiyposition + 30, 135, 40);
		if(showAllNodes) {
			canvas.fillRect(uixposition + 130, uiyposition + 101, 10, 10);
		} else {
			canvas.drawRect(uixposition + 130, uiyposition + 101, 10, 10);
		}

		if (acceptingInput) {
			canvas.setColor(acceptingTextColor);
			canvas.setStroke(new BasicStroke(1));
			canvas.fillRect(uixposition + 11, uiyposition + 31, 133,38);
			canvas.setColor(textColor);
		}

		canvas.setFont(new Font("Arial", 1, 18));
		canvas.drawString("HIGHLIGHT", uixposition + 165, uiyposition + 57);
		canvas.drawString(input, uixposition + 20, uiyposition + 57);



		canvas.setFont(oldFont);

		canvas.setColor(Color.BLUE);
		List<SimulationNode> highlightedNodes = new LinkedList<>();
		for (SimulationNode node : highwayNetwork.getNetwork().getAllNodes()) {
			if(highLightedNodes.contains(node.getId())){
				highlightedNodes.add(node);
				continue;
			}

			if(showAllNodes){
				Point2d nodePoint = positionUtil.getCanvasPosition(node);
				canvas.drawString(Integer.toString(node.getId()), (int) nodePoint.x, (int) nodePoint.y);
			}
		}
		
		canvas.setColor(Color.RED);
		for (SimulationNode highlightedNode : highlightedNodes) {
			Point2d nodePoint = positionUtil.getCanvasPosition(highlightedNode);
			canvas.drawString(Integer.toString(highlightedNode.getId()), (int) nodePoint.x, (int) nodePoint.y);
		}
		
		canvas.setColor(Color.BLUE);
	}

	private MouseListener getMouseListener(){
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
					Point2d click = new Point2d(mouseEvent.getX(), mouseEvent.getY());

					if (click.getX() > uixposition + 155 && click.getX() < uixposition + 290 && click.getY() > uiyposition + 30 && click.getY() < uiyposition + 70) {
						if(highLightedNodes.size() != 0) {
							highLightedNodes.remove(0);
						}

						int highlight;
						try {
							highlight = Integer.parseInt(input);
						} catch (NumberFormatException e){
							input = "";
							status = "Invalid input.";
							return;
						}

						status = "Highlighting node '" + input + "'.";

						highLightedNodes.add(highlight);
					}

					if (click.getX() > uixposition + 10 && click.getX() < uixposition + 145 && click.getY() > uiyposition + 30 && click.getY() < uiyposition + 70) {
						if (acceptingInput == false ) {
							vis.addKeyListener(keyListener);
							acceptingInput = true;
						}
						return;
					}

					if (click.getX() > uixposition + 130 && click.getX() < uixposition + 140 && click.getY() > uiyposition + 101 && click.getY() < uiyposition + 111) {
						showAllNodes = !showAllNodes;
						return;
					}

					if (acceptingInput == true ) {
						vis.removeKeyListener(keyListener);
						acceptingInput = false;
					}

				}
			}

			@Override
			public void mouseEntered(MouseEvent me) {}

			@Override
			public void mouseExited(MouseEvent me) {}

			@Override
			public void mousePressed(MouseEvent me) {}

			@Override
			public void mouseReleased(MouseEvent me) {}
		};
	}

	private KeyListener getKeyListener(){
		return new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				int pressedKey = e.getKeyCode();
				if (pressedKey == KeyEvent.VK_BACK_SPACE) {
					if (input.length() > 0 ) {
						input = input.substring(0, input.length()-1);
					}
				} else if (pressedKey != KeyEvent.VK_ALT && pressedKey != KeyEvent.VK_SHIFT && pressedKey != KeyEvent.VK_ENTER ) {
					input += e.getKeyChar();
				}

			}
		};
	}

	private void setDefaultColors() {
		backgroundColor = new Color(0, 0, 0, 170);
		textColor = new Color(255, 255, 255, 255);
		acceptingTextColor = new Color(173, 173, 173, 170);
	}
	
}
