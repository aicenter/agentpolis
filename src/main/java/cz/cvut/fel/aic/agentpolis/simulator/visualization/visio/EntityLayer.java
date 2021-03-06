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
import java.awt.RenderingHints;
import java.util.*;
import javax.vecmath.Point2d;

/**
 *
 * @author fido
 * @param <E>
 */
public abstract class EntityLayer<E extends AgentPolisEntity> extends AbstractLayer {

	public static final Double DEFAULT_TEXT_MARGIN_BOTTOM = 5.0;

	public static final Color DEFAULT_TEXT_BACKGROUND_COLOR = Color.WHITE;

	protected final EntityStorage<E> entityStorage;

	private final boolean showStackedEntitiesCount;
	
	protected final AgentpolisConfig agentpolisConfig;
	

	protected VisioPositionUtil positionUtil;

	protected TimeProvider timeProvider;

	protected boolean transformSize;

	@Inject
	public EntityLayer(EntityStorage<E> entityStorage, AgentpolisConfig agentpolisConfig) {
		this(entityStorage, agentpolisConfig, agentpolisConfig.visio.showStackedEntities, true);
	}

	public EntityLayer(EntityStorage<E> entityStorage, AgentpolisConfig agentpolisConfig, 
			boolean showStackedEntitiesCount) {
		this(entityStorage, agentpolisConfig, showStackedEntitiesCount, true);
	}

	public EntityLayer(EntityStorage<E> entityStorage, AgentpolisConfig agentpolisConfig, 
			boolean showStackedEntitiesCount, boolean transformSize) {
		this.entityStorage = entityStorage;
		this.showStackedEntitiesCount = showStackedEntitiesCount;
		this.transformSize = transformSize;
		this.agentpolisConfig = agentpolisConfig;
	}

	@Inject
	public void init(VisioPositionUtil positionUtil, TimeProvider timeProvider){
		this.positionUtil = positionUtil;
		this.timeProvider = timeProvider;
	}

	@Override
	public void paint(Graphics2D canvas) {
		canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Dimension dim = Vis.getDrawingDimension();

		EntityStorage<E>.EntityIterator entityIterator = entityStorage.new EntityIterator();
		E entity;
		long time = timeProvider.getCurrentSimTime();
		
		Map<Point2d, List<E>> entityPositionMap = null;
		if (showStackedEntitiesCount) {
			entityPositionMap = new HashMap<>();
		}

		while ((entity = entityIterator.getNextEntity()) != null) {
			if (skipDrawing(entity)) {
				continue;
			}
			Point2d entityPosition = getEntityPositionInTime(entity, time);

			if (showStackedEntitiesCount && !(entity instanceof Vehicle)) {
				entityPositionMap.computeIfAbsent(entityPosition, k -> new ArrayList<>()).add(entity);
			} else {
				drawEntity(entity, entityPosition, canvas, dim);
			}
		}

		if (showStackedEntitiesCount) {
			for (Map.Entry<Point2d, List<E>> entry : entityPositionMap.entrySet()) {
				Point2d entityPosition = entry.getKey();
				List<E> entities = entry.getValue();
				drawEntities(entities, entityPosition, canvas, dim);
			}
		}
	}

	protected abstract Point2d getEntityPosition(E entity);

	protected abstract Point2d getEntityPositionInTime(E entity, long time);

	protected void drawEntity(E entity, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
		drawEntities(Collections.singletonList(entity), entityPosition, canvas, dim);
	}

	protected void drawEntities(List<E> entities, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
		E representative = entities.get(0);
		Color color = getEntityDrawColor(representative);
		canvas.setColor(color);
		double radius = getRadius(representative);
		int width = Math.max(2, (int) Math.round(radius * 2));

		int x1 = (int) (entityPosition.getX() - radius);
		int y1 = (int) (entityPosition.getY() - radius);
		int x2 = (int) (entityPosition.getX() + radius);
		int y2 = (int) (entityPosition.getY() + radius);
		if (VisioUtils.rectangleOverlaps(x1, y1, x2, y2, dim)) {
			canvas.fillOval(x1, y1, width, width);

			if (entities.size() > 1 && Vis.getZoomFactor() > agentpolisConfig.visio.minZoomToShowStackEntitiesCount) {
				VisioUtils.printTextWithBackgroud(canvas, Integer.toString(entities.size()),
						new Point((int) (x1 - getTextMargin()), y1 - (y2 - y1) / 2), color, getTextBackgroundColor());
			}
		}
	}

	protected abstract Color getEntityDrawColor(E entity);

	protected abstract int getEntityTransformableRadius(E entity);

	protected abstract double getEntityStaticRadius(E entity);

	protected boolean skipDrawing(E entity) {
		return false;
	}

	protected Color getTextBackgroundColor() {
		return DEFAULT_TEXT_BACKGROUND_COLOR;
	}

	protected double getTextMargin() {
		return DEFAULT_TEXT_MARGIN_BOTTOM;
	}

	protected double getTextMarginTransX() {
		return Vis.transW(getTextMargin());
	}

	protected double getTextMarginTransY() {
		return Vis.transH(getTextMargin());
	}

	protected double getRadius(E entity) {
		if (transformSize) {
			double radius = Vis.transW(getEntityTransformableRadius(entity), 
					Math.max(Vis.getZoomFactor(), agentpolisConfig.visio.minEntityZoom));
			return radius;
		}
		else{
			return getEntityStaticRadius(entity);
		}
	}

	/**
	 * Returns {@code true} iff the point represented by given coordinates
	 * {@code x,y} is within the given {@code dimension}
	 * 
	 * @param x
	 * @param y
	 * @param dimension
	 * @return
	 */
	protected static boolean pointInside(int x, int y, Dimension dimension) {
		return x > 0 && x < dimension.width && y > 0 && y < dimension.height;
	}

	/**
	 * Returns {@code true} iff the {@code point} is within the given
	 * {@code dimension}
	 * 
	 * @param point
	 * @param dimension
	 * @return
	 */
	protected static boolean pointInside(Point2d point, Dimension dimension) {
		return point.getX() > 0 && point.getX() < dimension.width && point.getY() > 0
				&& point.getY() < dimension.height;
	}

	

}
