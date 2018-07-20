/*
 * Copyright (C) 2018 Czech Technical University in Prague.
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

import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.EntityStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Comparator;
import javax.vecmath.Point2d;

/**
 *
 * @author F.I.D.O.
 * @param <E>
 */
public abstract class ClickableEntityLayer<E extends AgentPolisEntity> extends EntityLayer<E> implements MouseListener{
	
	public static final int DEFAULT_MAX_CLICK_ERROR = 15;
	
	public ClickableEntityLayer(EntityStorage<E> entityStorage) {
		super(entityStorage);
	}
	
	public ClickableEntityLayer(EntityStorage<E> entityStorage, boolean showStackedEntitiesCount) {
        super(entityStorage, showStackedEntitiesCount);
    }
	
	public ClickableEntityLayer(EntityStorage<E> entityStorage, boolean showStackedEntitiesCount, boolean transformSize) {
		super(entityStorage, showStackedEntitiesCount, transformSize);
    }
	
	@Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            Point2d click = new Point2d(mouseEvent.getX(), mouseEvent.getY());
			
			E nearestEntity = getNearestEntity(click);
			if(nearestEntity != null){
				if (getDistanceFromClick(nearestEntity, click) <= getMaxClickError()) {
					processClick(nearestEntity);
				}
			}
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
	
	protected E getNearestEntity(Point2d point){
		if (entityStorage.isEmpty() == false) {
			Comparator<E> nearestEntityComparator = getNearestEntityComparator(point);
			E closestEntity = Collections.min(entityStorage.getEntities(), nearestEntityComparator);
			return closestEntity;
		}
		return null;
	}
	
	protected Comparator<E> getNearestEntityComparator(Point2d from) {
		return new NearestEntityComparator<>(this, from);
	}

	protected abstract void processClick(E nearestEntity);

	private double getDistanceFromClick(E nearestEntity, Point2d click) {
		return getEntityPosition(nearestEntity).distance(click);
	}

	private int getMaxClickError() {
		return DEFAULT_MAX_CLICK_ERROR;
	}
	
}