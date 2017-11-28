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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.EntityLayer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.PositionUtil;

import java.awt.Color;
import javax.vecmath.Point2d;

/**
 *
 * @author fido
 */
@Singleton
public class TestVehicleLayer extends EntityLayer<DriveAgent>{

    @Inject
    public TestVehicleLayer(DriveAgentStorage driveAgentStorage) {
        super(driveAgentStorage);
    }

    @Override
    protected Point2d getEntityPosition(DriveAgent entity) {
        return positionUtil.getCanvasPositionInterpolated(entity, EGraphType.HIGHWAY);
    }

    @Override
    protected Color getEntityDrawColor(DriveAgent driveAgent) {
        return Color.RED;
    }

    @Override
    protected int getEntityDrawRadius(DriveAgent driveAgent) {
        return (int) driveAgent.getVehicle().getLength() / 2;
    }

//    @Override
//    protected void drawEntity(DriveAgent entity, Point2d agentPosition, Graphics2D canvas, Dimension dim) {
//        super.drawEntity(entity, agentPosition, canvas, dim); 
//        canvas.fillOval(0, 0, 10, 10);
//    }
    
    
    
}
