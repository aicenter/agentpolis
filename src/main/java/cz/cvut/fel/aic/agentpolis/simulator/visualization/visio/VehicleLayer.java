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
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.EntityStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import javax.vecmath.Point2d;

/**
 *
 * @author F-I-D-O
 */
public abstract class VehicleLayer<V extends Vehicle>  extends EntityLayer<V>{
    
    private Path2D CarRepresentationShape;
    
   
    public VehicleLayer(EntityStorage<V> driverStorage, AgentpolisConfig agentpolisConfig) {
        this(driverStorage, agentpolisConfig.showStackedEntities, true);
    }
	
    public VehicleLayer(EntityStorage<V> driverStorage, boolean showStackedEntitiesCount, boolean transformSize) {
        super(driverStorage, showStackedEntitiesCount, transformSize);
    }

    @Override
    protected Point2d getEntityPosition(V vehicle) {
        return positionUtil.getCanvasPositionInterpolatedForVehicle(vehicle);
    }
    
    //not used
    @Override
    protected int getEntityTransformableRadius(V vehicle) {
        return 0;
    }
	
	//not used
	@Override
	protected double getEntityStaticRadius(V vehicle) {
		return  0;
	}
    
    protected abstract float getVehicleWidth(V vehicle);
    
    protected abstract float getVehicleLength(V vehicle);
	
	protected abstract float getVehicleStaticWidth(V vehicle);
    
    protected abstract float getVehicleStaticLength(V vehicle);

    @Override
    protected void drawEntities(ArrayList<V> entities, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
        V representative = entities.get(0);
        
        if(CarRepresentationShape == null){
			if(transformSize){
				CarRepresentationShape
						= createCarShape(getVehicleLength(representative), getVehicleWidth(representative));
			}
			else{
				CarRepresentationShape
						= createCarShape(getVehicleStaticLength(representative), getVehicleStaticWidth(representative));
			}
        }
        
        Color color = getEntityDrawColor(entities.get(0));
        canvas.setColor(color);

        int x = (int) (entityPosition.getX());
        int y = (int) (entityPosition.getY());
        
        if (x > 0 && x < dim.width && y > 0 && y < dim.height) {
            
            /* entity count text */
            if(entities.size() > 1){
                VisioUtils.printTextWithBackgroud(canvas, Integer.toString(entities.size()), 
                    new Point((int) (x - getTextMarginTransX()), (int) (y - getTextMarginTransY())), color, 
                    getTextBackgroundColor());
            }
            
            double angle = 0;
            SimulationNode target = null;
            SimulationNode position = representative.getPosition();
            Driver driver = representative.getDriver();
            if(driver != null && driver.getTargetNode() != null){
                target = driver.getTargetNode();
            }
            else if(representative instanceof TransportableEntity 
                    && ((TransportableEntity) representative).getTransportingEntity() != null){
                target = ((TransportableEntity) representative).getTransportingEntity().getDriver().getTargetNode();
            }
            else{
                position = representative.getLastFromPosition();
                target = representative.getPosition();
            }
            
            if(target != null && position != null){
                angle = positionUtil.getAngle(driver);
            }
            
            double centerShift = getVehicleWidth(representative) / 2;
//            double centerShiftTrans = Vis.transW(centerShift)

            /* desired order of the transformations */
            
            /* rotate against center of the shape */
            AffineTransform rotate = AffineTransform.getRotateInstance(-angle, centerShift, centerShift);
            
            /* translate the center of the shape to entity position */
            AffineTransform translate = 
                    AffineTransform.getTranslateInstance(entityPosition.getX() - Vis.transW(centerShift), 
                            entityPosition.getY() - Vis.transH(centerShift));
            
            /* transformations are applied in inverse order */
			if(transformSize){
				
				/* scale according to zoom factor */
                //System.out.println("Zoom factor: " + Vis.getZoomFactor());
				AffineTransform scale = AffineTransform.getScaleInstance(Math.max(1,Vis.getZoomFactor()),Math.max(1,Vis.getZoomFactor()));
                //AffineTransform scale = AffineTransform.getScaleInstance(Vis.getZoomFactor(),Vis.getZoomFactor());

				scale.concatenate(rotate);
				translate.concatenate(scale);
			}
			else{
				translate.concatenate(rotate);
			}
			
            Shape s = CarRepresentationShape.createTransformedShape(translate);
            canvas.fill(s);
        }
    }

    @Override
    protected void drawEntity(V representative, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
        if(CarRepresentationShape == null){
            if(transformSize){
                CarRepresentationShape
                        = createCarShape(getVehicleLength(representative), getVehicleWidth(representative));
            }
            else{
                CarRepresentationShape
                        = createCarShape(getVehicleStaticLength(representative), getVehicleStaticWidth(representative));
            }
        }

        Color color = getEntityDrawColor(representative);
        canvas.setColor(color);

        int x = (int) (entityPosition.getX());
        int y = (int) (entityPosition.getY());

        if (x > 0 && x < dim.width && y > 0 && y < dim.height) {

            double angle = 0;
            SimulationNode target = null;
            SimulationNode position = representative.getPosition();
            Driver driver = representative.getDriver();
            if(driver != null && driver.getTargetNode() != null){
                target = driver.getTargetNode();
            }
            else if(representative instanceof TransportableEntity
                    && ((TransportableEntity) representative).getTransportingEntity() != null){
                target = ((TransportableEntity) representative).getTransportingEntity().getDriver().getTargetNode();
            }
            else{
                position = representative.getLastFromPosition();
                target = representative.getPosition();
            }

            if(target != null && position != null){
                angle = positionUtil.getAngle(driver);
            }

            double centerShift = getVehicleWidth(representative) / 2;
//            double centerShiftTrans = Vis.transW(centerShift)

            /* desired order of the transformations */

            /* rotate against center of the shape */
            AffineTransform rotate = AffineTransform.getRotateInstance(-angle, centerShift, centerShift);

            /* translate the center of the shape to entity position */
            AffineTransform translate =
                    AffineTransform.getTranslateInstance(entityPosition.getX() - Vis.transW(centerShift),
                            entityPosition.getY() - Vis.transH(centerShift));

            /* transformations are applied in inverse order */
            if(transformSize){

                /* scale according to zoom factor */
                //System.out.println("Zoom factor: " + Vis.getZoomFactor());
                AffineTransform scale = AffineTransform.getScaleInstance(Math.max(1,Vis.getZoomFactor()),Math.max(1,Vis.getZoomFactor()));
                //AffineTransform scale = AffineTransform.getScaleInstance(Vis.getZoomFactor(),Vis.getZoomFactor());

                scale.concatenate(rotate);
                translate.concatenate(scale);
            }
            else{
                translate.concatenate(rotate);
            }

            Shape s = CarRepresentationShape.createTransformedShape(translate);
            canvas.fill(s);
        }
    }
    
    private static Path2D createCarShape(final float length, final float width) {
        final GeneralPath p0 = new GeneralPath();
        p0.moveTo(0,0);
        p0.lineTo(length, width / 2);
        p0.lineTo(0, width);
        p0.closePath();
        return p0;
    }

	
}
