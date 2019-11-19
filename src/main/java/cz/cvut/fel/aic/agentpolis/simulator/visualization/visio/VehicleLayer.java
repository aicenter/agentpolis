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

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.util.List;

import javax.vecmath.Point2d;

import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.MovingEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.EntityStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.alite.vis.Vis;

/**
 * //TODO: maybe rename to MovingEntityLayer + rename *vehicle* methods
 *
 * @author F-I-D-O
 * @param <V> Vehicle type
 */
public abstract class VehicleLayer<V extends AgentPolisEntity & MovingEntity> extends EntityLayer<V> {

	private Path2D representativeShape;
	
	private Path2D representativeShapeStatic;

	public VehicleLayer(EntityStorage<V> driverStorage, AgentpolisConfig agentpolisConfig) {
		this(driverStorage, agentpolisConfig, agentpolisConfig.visio.showStackedEntities, true);
	}

	public VehicleLayer(EntityStorage<V> driverStorage, AgentpolisConfig agentpolisConfig,
			boolean showStackedEntitiesCount, boolean transformSize) {
		super(driverStorage, agentpolisConfig, showStackedEntitiesCount, transformSize);
	}

	@Override
	protected Point2d getEntityPosition(V vehicle) {
		return positionUtil.getCanvasPositionInterpolated(vehicle);
	}

	@Override
	public Point2d getEntityPositionInTime(V vehicle, long time) {
		return positionUtil.getCanvasPositionInterpolatedInTime(vehicle, time);
	}

	// not used
	@Override
	protected int getEntityTransformableRadius(V vehicle) {
		return 0;
	}

	// not used
	@Override
	protected double getEntityStaticRadius(V vehicle) {
		return 0;
	}

	protected abstract float getVehicleWidth(V vehicle);

	protected abstract float getVehicleLength(V vehicle);

	protected abstract float getVehicleStaticWidth(V vehicle);

	protected abstract float getVehicleStaticLength(V vehicle);

	@Override
	protected void drawEntities(List<V> entities, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
		if (pointInside(entityPosition, dim)) {
			V representative = entities.get(0);
			Color color = getEntityDrawColor(representative);
			canvas.setColor(color);
			boolean transformSizeForEntity = checkIfTransformSize(representative);
			
			drawEntityCount(entities,entityPosition, canvas, color);
			drawEntityShape(representative, entityPosition, canvas, transformSizeForEntity);
		}
	}

	protected void drawEntityCount(List<V> entities, Point2d entityPosition, Graphics2D canvas, Color color) {
		int x = (int) (entityPosition.getX());
		int y = (int) (entityPosition.getY());
		if (entities.size() > 1) {
			VisioUtils.printTextWithBackgroud(canvas, Integer.toString(entities.size()),
					new Point((int) (x - getTextMarginTransX()), (int) (y - getTextMarginTransY())), color,
					getTextBackgroundColor());
		}
	}

	public void drawEntityShape(V representative, Point2d entityPosition, Graphics2D canvas, 
			boolean transformSizeForEntity) {
		double angle = getAngle(representative);
		/* desired order of the transformations */
		/* rotate against center of the shape */
		AffineTransform rotate = AffineTransform.getRotateInstance(-angle, 0, 0);
		/* translate the center of the shape to entity position */
		AffineTransform translate = AffineTransform.getTranslateInstance(entityPosition.getX(),
				entityPosition.getY());

		/* transformations are applied in inverse order */
		Shape s;
		if (transformSizeForEntity) {
			double zoomFactor = Math.max(Vis.getZoomFactor(), agentpolisConfig.visio.minEntityZoom);
			/* scale according to zoom factor */
			AffineTransform scale = AffineTransform.getScaleInstance(zoomFactor, zoomFactor);

			scale.concatenate(rotate);
			translate.concatenate(scale);
			
			s = getShape(representative).createTransformedShape(translate);
		} else {
			translate.concatenate(rotate);
			
			s = getStaticShape(representative).createTransformedShape(translate);
		}
		
		canvas.fill(s);
	}

	protected double getAngle(V representative) {
		SimulationNode target = getTargetNode(representative);
		double angle = 0;
		if (target != null) {
			angle = positionUtil.getAngle(representative);
		}
		return angle;
	}

	protected SimulationNode getTargetNode(V representative) {
		if (representative != null && representative.getTargetNode() != null) {
			return representative.getTargetNode();
		} else if (representative instanceof TransportableEntity
				&& ((TransportableEntity) representative).getTransportingEntity() != null) {
			return ((TransportableEntity) representative).getTransportingEntity().getDriver().getTargetNode();
		} else {
			return representative.getPosition();
		}
	}

	protected Path2D getShape(V representative) {
		if (representativeShape == null) {
			representativeShape = createCarShape(getVehicleLength(representative), getVehicleWidth(representative));
		}
		return representativeShape;
	}
	
	protected Path2D getStaticShape(V representative) {
		if (representativeShapeStatic == null) {
			representativeShapeStatic = createCarShape(getVehicleStaticLength(representative),
					getVehicleStaticWidth(representative));
		}
		return representativeShapeStatic;
	}

	public static Path2D createCarShape(final float length, final float width) {
		final GeneralPath p0 = new GeneralPath();
		p0.moveTo(-length / 2, -width / 2);
		p0.lineTo(length / 2, 0);
		p0.lineTo(-length / 2, width / 2);
		p0.closePath();
		return p0;
	}

	public boolean checkIfTransformSize(V representative) {
		return transformSize;
	}

}
