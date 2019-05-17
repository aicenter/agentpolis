package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.alite.vis.VisManager;
import cz.cvut.fel.aic.alite.vis.layer.AbstractLayer;

import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ScreenCaputreLayer extends AbstractLayer {

	private int uixposition;
	private int uiyposition;
	private long lastPressed;
	private boolean isPressed;
	private Color backgroundColor;
	private Color textColor;
	private Graphics2D lastCanvas;
	private MouseListener mouseListener;

	private static final int WIDTH = 66;
	private static final int HEIGHT = 30;

	public ScreenCaputreLayer(){
		backgroundColor = new Color(0, 0, 0, 170);
		textColor = new Color(255, 255, 255, 255);
	}

	@Override
	public void paint(Graphics2D canvas) {
		lastCanvas = canvas;

		uixposition = Vis.getDrawingDimension().width - WIDTH - 345;
		uiyposition = Vis.getDrawingDimension().height - HEIGHT - 30;

		if(isPressed){
			if(System.currentTimeMillis() - lastPressed > 500){
				isPressed = false;
				paint(canvas, backgroundColor, textColor);
			}
			paint(canvas, textColor, backgroundColor);
		} else {
			paint(canvas, backgroundColor, textColor);
		}
	}

	private void paint(Graphics2D canvas, Color backgroundColor, Color textColor){
		canvas.setColor(backgroundColor);
		canvas.fillRect(uixposition, uiyposition, WIDTH, HEIGHT);

		Font oldFont = canvas.getFont();
		canvas.setFont(new Font("Arial", Font.BOLD, 14));
		canvas.setColor(textColor);
		canvas.drawString("Prt sc", uixposition + 10, uiyposition + 20);

		canvas.setFont(oldFont);
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

	public MouseListener getMouseListener() {
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
					Point2d click = new Point2d(mouseEvent.getX(), mouseEvent.getY());
					if(click.getX() > uixposition && click.getX() < uixposition + WIDTH &&
					   click.getY() > uiyposition && click.getY() < uiyposition + HEIGHT && !isPressed){

						Dimension dimension = Vis.getDrawingDimension();
						VisManager.saveToFile(dimension.width, dimension.height);

						isPressed = true;
						lastPressed = System.currentTimeMillis();
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
}
