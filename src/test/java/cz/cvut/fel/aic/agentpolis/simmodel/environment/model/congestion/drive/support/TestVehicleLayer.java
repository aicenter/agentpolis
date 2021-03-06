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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.EntityLayer;
import java.awt.Color;
import javax.vecmath.Point2d;

/**
 *
 * @author fido
 */
@Singleton
public class TestVehicleLayer extends EntityLayer<DriveAgent>{

	@Inject
	public TestVehicleLayer(DriveAgentStorage driveAgentStorage, AgentpolisConfig agentpolisConfig) {
		super(driveAgentStorage, agentpolisConfig);
	}

	@Override
	protected Point2d getEntityPosition(DriveAgent entity) {
		return positionUtil.getCanvasPositionInterpolated(entity, EGraphType.HIGHWAY);
	}

	@Override
	protected Point2d getEntityPositionInTime(DriveAgent entity, long time) {
		return positionUtil.getCanvasPositionInterpolated(entity, EGraphType.HIGHWAY);
	}

	@Override
	protected Color getEntityDrawColor(DriveAgent driveAgent) {
		return Color.BLUE;
	}

	@Override
	protected int getEntityTransformableRadius(DriveAgent driveAgent) {
		return (int) driveAgent.getVehicle().getLengthCm() / 2;
	}

//	@Override
//	protected void drawEntity(DriveAgent entity, Point2d agentPosition, Graphics2D canvas, Dimension dim) {
//		super.drawEntity(entity, agentPosition, canvas, dim); 
//		canvas.fillOval(0, 0, 10, 10);
//	}

	@Override
	protected double getEntityStaticRadius(DriveAgent entity) {
		return  5;
	}
	
	
	
}
