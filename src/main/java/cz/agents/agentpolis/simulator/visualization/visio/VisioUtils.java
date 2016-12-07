/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simulator.visualization.visio;

import java.awt.Color;
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
}
