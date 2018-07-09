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

import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.alite.vis.layer.AbstractLayer;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.EntityLayer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioUtils;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.vecmath.Point2d;

/**
 *
 * @author F-I-D-O
 */
public class CarLayer  extends EntityLayer<DriveAgent>{
    
    private Path2D CAR_REPRESENTATION_SHAPE;
    
    
    
	
	@Inject
    public CarLayer(DriveAgentStorage driveAgentStorage) {
        super(driveAgentStorage);
    }

    @Override
    protected Point2d getEntityPosition(DriveAgent entity) {
        Vehicle vehicle = entity.getVehicle();
        if(vehicle == null){
            return positionUtil.getCanvasPosition(entity);
        }
        return positionUtil.getCanvasPositionInterpolatedForVehicle(vehicle);
    }

    @Override
    protected Color getEntityDrawColor(DriveAgent driveAgent) {
        return Color.CYAN;
    }

    // not used
    @Override
    protected int getEntityTransformableRadius(DriveAgent driveAgent) {
        return 0;
    }

    @Override
    protected void drawEntities(ArrayList<DriveAgent> entities, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
        DriveAgent representative = entities.get(0);
        
        if(CAR_REPRESENTATION_SHAPE == null){
            CAR_REPRESENTATION_SHAPE = createCarShape((float) representative.getVehicle().getLength());
        }
        
        Color color = getEntityDrawColor(entities.get(0));
        canvas.setColor(color);
        double radius = Vis.transW(getEntityTransformableRadius(entities.get(0)));
        int width = (int) Math.round(radius * 2);

        int x1 = (int) (entityPosition.getX() - radius);
        int y1 = (int) (entityPosition.getY() - radius);
        int x2 = (int) (entityPosition.getX() + radius);
        int y2 = (int) (entityPosition.getY() + radius);
        
        if (x2 > 0 && x1 < dim.width && y2 > 0 && y1 < dim.height) {
            SimulationNode target = representative.getTargetNode();
            
            double angle = 0;
            if(target != null){
                SimulationNode position = representative.getPosition();
                angle = getAngle(position, target);
            }

            AffineTransform trans =
                    AffineTransform.getTranslateInstance(entityPosition.getX(),entityPosition.getY());
            trans.rotate(-angle);
            trans.scale(Vis.getZoomFactor(),Vis.getZoomFactor());
            Shape s = CAR_REPRESENTATION_SHAPE.createTransformedShape(trans);
            canvas.fill(s);
        }
        
        if(entities.size() > 1){
            VisioUtils.printTextWithBackgroud(canvas, Integer.toString(entities.size()), 
                new Point((int) (x1 - DEFAULT_TEXT_MARGIN_BOTTOM), y1 - (y2 - y1) / 2), color, 
                DEFAULT_TEXT_BACKGROUND_COLOR);
        }
    }
    
//    private static Path2D createCarShape(final float s) {
//        final GeneralPath p0 = new GeneralPath();
//        p0.moveTo(-s,0f);
//        p0.lineTo(s*0.5f, s*0.5f);
//        p0.lineTo(s/4,0f);
//        p0.lineTo(s*0.5f, -s*0.5f);
//        p0.closePath();
//        return p0;
//    }
    
    private static Path2D createCarShape(final float s) {
        final GeneralPath p0 = new GeneralPath();
        p0.moveTo(-s,0f);
        p0.lineTo(0, 1f);
        p0.lineTo(-s,2f);
        p0.closePath();
        return p0;
    }

    private double getAngle(SimulationNode position, SimulationNode target) {
        double dy = target.getLatitudeProjected1E2() - position.getLatitudeProjected1E2();
        double dx = target.getLongitudeProjected1E2() - position.getLongitudeProjected1E2();
        return Math.atan2(dy,dx);
    }

	@Override
	protected double getEntityStaticRadius(DriveAgent entity) {
		return 5;
	}

	
}
