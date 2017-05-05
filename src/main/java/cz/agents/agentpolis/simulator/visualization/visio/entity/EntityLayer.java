/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simulator.visualization.visio.entity;

import com.google.inject.Inject;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.environment.model.EntityStorage;
import cz.agents.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.agents.alite.vis.Vis;
import cz.agents.alite.vis.layer.AbstractLayer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.vecmath.Point2d;

/**
 *
 * @author fido
 * @param <E>
 */
public abstract class EntityLayer<E extends AgentPolisEntity> extends AbstractLayer{
    
    private final EntityStorage<E> entityStorage;
    
    protected PositionUtil positionUtil;

    public EntityLayer(EntityStorage<E> entityStorage) {
        this.entityStorage = entityStorage;
    }
    
    @Inject
    public void init(PositionUtil positionUtil){
        this.positionUtil = positionUtil;
    }
    
    
    
    @Override
    public void paint(Graphics2D canvas) {
        Dimension dim = Vis.getDrawingDimension();

        EntityStorage<E>.EntityIterator entityIterator = entityStorage.new EntityIterator();
        E entity;
        while((entity = entityIterator.getNextEntity()) != null){
            Point2d entityPosition = getEntityPosition(entity);
			drawEntity(entity, entityPosition, canvas, dim);
        }
    }

    protected abstract Point2d getEntityPosition(E entity);
    
    protected void drawEntity(E entity, Point2d agentPosition, Graphics2D canvas, Dimension dim) {
        Color color = getEntityDrawColor();
        canvas.setColor(color);
        int radius = getEntityDrawRadius();
		int width = radius * 2;

        int x1 = (int) (agentPosition.getX() - radius);
        int y1 = (int) (agentPosition.getY() - radius);
        int x2 = (int) (agentPosition.getX() + radius);
        int y2 = (int) (agentPosition.getY() + radius);
        if (x2 > 0 && x1 < dim.width && y2 > 0 && y1 < dim.height) {
            canvas.fillOval(x1, y1, width, width);
        }
    }

    protected abstract Color getEntityDrawColor();

    protected abstract int getEntityDrawRadius();
    
}
