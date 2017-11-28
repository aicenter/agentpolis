/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
