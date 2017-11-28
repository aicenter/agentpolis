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
import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.EntityStorage;
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.alite.vis.layer.AbstractLayer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.vecmath.Point2d;

/**
 *
 * @author fido
 * @param <E>
 */
public abstract class EntityLayer<E extends AgentPolisEntity> extends AbstractLayer{
    
    public static final Double DEFAULT_TEXT_MARGIN_BOTTOM = 5.0;
    
    public static final Color DEFAULT_TEXT_BACKGROUND_COLOR = Color.WHITE;

    
    
    
    private final EntityStorage<E> entityStorage;
    
    private final boolean showStackedEntitiesCount;
    
    
    private HashMap<Point2d,ArrayList<E>> entityPositionMap;
    
    protected PositionUtil positionUtil;
    
    protected Dimension dim;

    public EntityLayer(EntityStorage<E> entityStorage) {
        this(entityStorage, true);
    }
    
    public EntityLayer(EntityStorage<E> entityStorage, boolean showStackedEntitiesCount) {
        this.entityStorage = entityStorage;
        this.showStackedEntitiesCount = showStackedEntitiesCount;
    }
    
    @Inject
    public void init(PositionUtil positionUtil){
        this.positionUtil = positionUtil;
    }
    
    
    
    @Override
    public void paint(Graphics2D canvas) {
        dim = Vis.getDrawingDimension();

        EntityStorage<E>.EntityIterator entityIterator = entityStorage.new EntityIterator();
        E entity;
        if(showStackedEntitiesCount){
            entityPositionMap = new HashMap<>();
        }
        while((entity = entityIterator.getNextEntity()) != null){
            if(skipDrawing(entity)){
                continue;
            }
            Point2d entityPosition = getEntityPosition(entity);
            
            if(showStackedEntitiesCount){
                if(!entityPositionMap.containsKey(entityPosition)){
                    entityPositionMap.put(entityPosition, new ArrayList<>());
                }
                entityPositionMap.get(entityPosition).add(entity);
                }
            else{
                drawEntity(entity, entityPosition, canvas, dim);
            }
        }
        
        if(showStackedEntitiesCount){
            for (Map.Entry<Point2d, ArrayList<E>> entry : entityPositionMap.entrySet()) {
                Point2d entityPosition = entry.getKey();
                ArrayList<E> entities = entry.getValue();
                drawEntities(entities, entityPosition, canvas, dim);
            }
        }
    }

    protected abstract Point2d getEntityPosition(E entity);
    
    protected void drawEntity(E entity, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
        Color color = getEntityDrawColor(entity);
        canvas.setColor(color);
        double radius = Vis.transW(getEntityDrawRadius(entity));
        int width = (int) Math.round(radius * 2);

        int x1 = (int) (entityPosition.getX() - radius);
        int y1 = (int) (entityPosition.getY() - radius);
        int x2 = (int) (entityPosition.getX() + radius);
        int y2 = (int) (entityPosition.getY() + radius);
        if (x2 > 0 && x1 < dim.width && y2 > 0 && y1 < dim.height) {
            canvas.fillOval(x1, y1, width, width);
        }
    }
    
    protected void drawEntities(ArrayList<E> entities, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
        Color color = getEntityDrawColor(entities.get(0));
        canvas.setColor(color);
        double radius = Vis.transW(getEntityDrawRadius(entities.get(0)));
        int width = (int) Math.round(radius * 2);

        int x1 = (int) (entityPosition.getX() - radius);
        int y1 = (int) (entityPosition.getY() - radius);
        int x2 = (int) (entityPosition.getX() + radius);
        int y2 = (int) (entityPosition.getY() + radius);
        if (x2 > 0 && x1 < dim.width && y2 > 0 && y1 < dim.height) {
            canvas.fillOval(x1, y1, width, width);
            
            if(entities.size() > 1){
                VisioUtils.printTextWithBackgroud(canvas, Integer.toString(entities.size()), 
                    new Point((int) (x1 - getTextMargin()), y1 - (y2 - y1) / 2), color, 
                    getTextBackgroundColor());
            }
        }
    }

    protected abstract Color getEntityDrawColor(E entity);

    protected abstract int getEntityDrawRadius(E entity);

    protected boolean skipDrawing(E entity) {
        return false;
    }
    
    protected Color getTextBackgroundColor(){
        return DEFAULT_TEXT_BACKGROUND_COLOR;
    }
    
    protected double getTextMargin(){
        return DEFAULT_TEXT_MARGIN_BOTTOM;
    }
    
    protected double getTextMarginTransX(){
        return Vis.transW(getTextMargin());
    }
    
    protected double getTextMarginTransY(){
        return Vis.transH(getTextMargin());
    }
    
}