package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Singleton;
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.alite.vis.VisManager;
import cz.cvut.fel.aic.alite.vis.layer.AbstractLayer;
import cz.cvut.fel.aic.alite.vis.layer.VisLayer;
import cz.cvut.fel.aic.alite.vis.layer.toggle.KeyToggleLayer;
import cz.cvut.fel.aic.alite.vis.layer.toggle.ToggleLayer;
import sun.print.PathGraphics;

import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@Singleton
public class LayerManagementLayer extends AbstractLayer {
    private boolean isVisible;
    private MouseListener mouseListener;
    private Color backgroundColor;
    private Color textColor;
    private List<ManageableLayer> manageableLayers;

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
        canvas.fillRect(1580, 955, 300, 30);

        Font oldFont = canvas.getFont();
        canvas.setFont(new Font("Arial", 1, 14));
        canvas.setColor(textColor);
        canvas.drawString(message, 1630, 975);

        canvas.setFont(oldFont);
    }

    public void paintTopRectangle(Graphics2D canvas) {
        canvas.setColor(backgroundColor);
        int height = manageableLayers.size() * 25 + 5;
        canvas.fillRect(1580, 945 - height, 300, height);

        Font oldFont = canvas.getFont();
        canvas.setFont(new Font("Arial", 1, 14));
        canvas.setColor(textColor);
        for( int i = 0; i < manageableLayers.size(); i++ ) {
            canvas.drawString(manageableLayers.get(i).getName(), 1630, 935 - 25*i);
            if (manageableLayers.get(i).isVisible()){
                canvas.fillRect(1602,921 - 25*i, 18, 18);

            } else {
                canvas.drawRect(1602,921 - 25*i, 18, 18);
            }
            paintLayerOrderArrows(canvas, 921-25*i);
        }
        canvas.setFont(oldFont);

    }

    public void paintArrowUpwards(Graphics2D canvas) {
        int[] xPoints = {1595, 1605, 1615};
        int[] yPoints = {977, 963, 977};
        canvas.fillPolygon(xPoints, yPoints, 3);
    }

    public void paintArrowDownwards(Graphics2D canvas) {
        int[] xPoints = {1595, 1605, 1615};
        int[] yPoints = {963, 977, 963};
        canvas.fillPolygon(xPoints, yPoints, 3);
    }

    public void paintLayerOrderArrows(Graphics2D canvas, int ypos) {
        int[] xPoints = {1585, 1591, 1597};
        int[] yPointsUp = {ypos+8, ypos, ypos+8};
        int[] yPointsDown = {ypos+10, ypos+18, ypos+10};
        canvas.fillPolygon(xPoints, yPointsUp, 3);
        canvas.fillPolygon(xPoints, yPointsDown, 3);
    }


    private void setDefaultColors() {
        backgroundColor = new Color(0, 0, 0, 170);
        textColor = new Color(255, 255, 255, 255);
    }

    private boolean validateLayersManagementToggleClick(Point2d click) {
        if (click.getX() > 1580 && click.getX() < 1880 && click.getY() > 955 && click.getY() < 985 ) {
            isVisible = !isVisible;
            return true;
        }
        return false;
    }

    private boolean validateLayerToggleClick(Point2d click) {
        if (click.getX() > 1602 && click.getX() < 1620 && click.getY() > 921 - 25*manageableLayers.size() && click.getY() < 939 ) {
            for (int i = 0; i < manageableLayers.size(); i++) {
                if (click.getY() > 921 - 25*i && click.getY() < 939 - 25*i ) {
                    manageableLayers.get(i).toggle();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validateLayerMoveUpClick(Point2d click) {
        if (click.getX() > 1585 && click.getX() < 1597 && click.getY() > 921 - 25*manageableLayers.size() && click.getY() < 939 ) {
            for (int i = 0; i < manageableLayers.size(); i++) {
                if (click.getY() > 921 - 25*i && click.getY() < 929 - 25*i ) {
                    System.out.println("Move Up: " +  i);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validateLayerMoveDownClick(Point2d click) {
        if (click.getX() > 1585 && click.getX() < 1597 && click.getY() > 921 - 25*manageableLayers.size() && click.getY() < 939 ) {
            for (int i = 0; i < manageableLayers.size(); i++) {
                if (click.getY() > 931 - 25*i && click.getY() < 939 - 25*i ) {
                    System.out.println("Move Down: " +  i);
                    return true;
                }
            }
        }
        return false;
    }

    public void init(Vis vis) {
        super.init(vis);
        this.mouseListener = new MouseListener() {
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
        vis.addMouseListener(this.mouseListener);
    }

    public void deinit(Vis vis) {
        super.deinit(vis);
        vis.removeMouseListener(this.mouseListener);
    }


}
