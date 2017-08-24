/*
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;

import cz.agents.alite.vis.Vis;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
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
    
    private Path2D CAR_REPRESENTATION_SHAPE;
    
    
    
	
	@Inject
    public VehicleLayer(EntityStorage<V> driverStorage) {
        super(driverStorage);
    }

    @Override
    protected Point2d getEntityPosition(V vehicle) {
        return positionUtil.getCanvasPositionInterpolatedForVehicle(vehicle);
    }
    
    //not used
    @Override
    protected int getEntityDrawRadius(V vehicle) {
        return 0;
    }
    
    protected abstract float getVehicleWidth(V vehicle);
    
    protected abstract float getVehicleLength(V vehicle);

    @Override
    protected void drawEntities(ArrayList<V> entities, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
        V representative = entities.get(0);
        
        if(CAR_REPRESENTATION_SHAPE == null){
            CAR_REPRESENTATION_SHAPE = createCarShape(getVehicleLength(representative), getVehicleWidth(representative));
        }
        
        Color color = getEntityDrawColor(entities.get(0));
        canvas.setColor(color);

        int x1 = (int) (entityPosition.getX());
        int y1 = (int) (entityPosition.getY());
        int x2 = (int) (entityPosition.getX());
        int y2 = (int) (entityPosition.getY());
        
        if (x2 > 0 && x1 < dim.width && y2 > 0 && y1 < dim.height) {
            
            double angle = 0;
            Driver driver = representative.getDriver();
            if(driver != null){
                SimulationNode target = driver.getTargetNode();

                if(target != null){
                    SimulationNode position = representative.getPosition();
                    angle = getAngle(position, target);
                }
            }
            
            double centerShift = getVehicleWidth(representative) / 2;
//            double centerShiftTrans = Vis.transW(centerShift)

            /* desired order of the transformations */
            
            /* roate against center of the shape */
            AffineTransform rotate = AffineTransform.getRotateInstance(-angle, centerShift, centerShift);
            
            /* scale according to zoom factor */
            AffineTransform scale = AffineTransform.getScaleInstance(Vis.getZoomFactor(),Vis.getZoomFactor());
            
            /* translate the center of the shape to entity position */
            AffineTransform translate = 
                    AffineTransform.getTranslateInstance(entityPosition.getX() - Vis.transW(centerShift), 
                            entityPosition.getY() - Vis.transH(centerShift));
            
            /* transformaitions are applied in inverse order */
            scale.concatenate(rotate);
            translate.concatenate(scale);

            Shape s = CAR_REPRESENTATION_SHAPE.createTransformedShape(translate);
            canvas.fill(s);
        }
        
        if(entities.size() > 1){
            VisioUtils.printTextWithBackgroud(canvas, Integer.toString(entities.size()), 
                new Point((int) (x1 - DEFAULT_TEXT_MARGIN_BOTTOM), y1 - (y2 - y1) / 2), color, 
                DEFAULT_TEXT_BACKGROUND_COLOR);
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

    private double getAngle(SimulationNode position, SimulationNode target) {
        double dy = target.getLatitudeProjected1E2() - position.getLatitudeProjected1E2();
        double dx = target.getLongitudeProjected1E2() - position.getLongitudeProjected1E2();
        return Math.atan2(dy,dx);
    }

	
}
