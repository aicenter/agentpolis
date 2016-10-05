/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.alite.vis.Vis;
import cz.agents.basestructures.GPSLocation;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

/**
 *
 * @author fido
 */
@Singleton
public class PositionUtil {
    
    private final Projection projection;

    
    
    @Inject
    public PositionUtil(Projection projection) {
        this.projection = projection;
    }
    
    
    
    
    public Point2d getCanvasPosition(GPSLocation position){
        Point3d projectedPoint = projection.project(position);
        return new Point2d(Vis.transX(projectedPoint.x), Vis.transY(projectedPoint.y));
    }
}
