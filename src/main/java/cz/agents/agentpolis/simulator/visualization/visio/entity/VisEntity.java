package cz.agents.agentpolis.simulator.visualization.visio.entity;

import java.awt.Color;

/**
 * 
 * Entity for visio
 * 
 * @author Zbynek Moler
 *
 */
public class VisEntity {

    private final Color entityColor;
    private final int widht;

    public VisEntity(Color entityColor, int widht) {
        super();
        this.entityColor = entityColor;
        this.widht = widht;
    }

    public Color getEntityColor() {
        return entityColor;
    }

    public int getWidht() {
        return widht;
    }

}
