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
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
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




    protected final EntityStorage<E> entityStorage;

    private final boolean showStackedEntitiesCount;
    ;

    private HashMap<Point2d,ArrayList<E>> entityPositionMap;

    protected PositionUtil positionUtil;

    protected TimeProvider timeProvider;

    protected Dimension dim;

	protected boolean transformSize;



	@Inject
    public EntityLayer(EntityStorage<E> entityStorage, AgentpolisConfig agentpolisConfig) {
        this(entityStorage, agentpolisConfig.showStackedEntities, true);
    }

    public EntityLayer(EntityStorage<E> entityStorage, boolean showStackedEntitiesCount) {
        this(entityStorage, showStackedEntitiesCount, true);
    }

	public EntityLayer(EntityStorage<E> entityStorage, boolean showStackedEntitiesCount, boolean transformSize) {
		this.entityStorage = entityStorage;
        this.showStackedEntitiesCount = showStackedEntitiesCount;
		this.transformSize = transformSize;
    }

    @Inject
    public void init(PositionUtil positionUtil, TimeProvider timeProvider){
        this.positionUtil = positionUtil;
        this.timeProvider = timeProvider;
    }



    @Override
    public void paint(Graphics2D canvas) {
        dim = Vis.getDrawingDimension();

        EntityStorage<E>.EntityIterator entityIterator = entityStorage.new EntityIterator();
        E entity;
        long time = timeProvider.getCurrentSimTime();

        if(showStackedEntitiesCount){
            entityPositionMap = new HashMap<>();
        }
        while((entity = entityIterator.getNextEntity()) != null){
            if(skipDrawing(entity)){
                continue;
            }
            Point2d entityPosition = getEntityPositionInTime(entity, time);

            if(showStackedEntitiesCount && !(entity instanceof Vehicle) && Vis.getZoomFactor() > 999){
                if(!entityPositionMap.containsKey(entityPosition)){
                    entityPositionMap.put(entityPosition, new ArrayList<>());
                }
                entityPositionMap.get(entityPosition).add(entity);
            } else {
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

	protected abstract Point2d getEntityPositionInTime(E entity, long time);

    protected void drawEntity(E entity, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
        Color color = getEntityDrawColor(entity);
        canvas.setColor(color);

        double radius = getRadius(entity);

        int width = Math.max(2, (int) Math.round(radius * 2));

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
        double radius = getRadius(entities.get(0));
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

    protected abstract int getEntityTransformableRadius(E entity);

	protected abstract double getEntityStaticRadius(E entity);

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



	protected double getRadius(E entity) {
		if(transformSize){
			return Vis.transW(getEntityTransformableRadius(entity));
		}
		else{
			return getEntityStaticRadius(entity);
		}
	}

}
