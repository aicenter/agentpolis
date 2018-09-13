package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.vehicle;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Class representing "ghost" vehicle e.g. a vehicle updated only by AgentDrive
 * and not handled by simulator
 * Created by wmatex on 10.7.14.
 */
public class Ghost extends Vehicle {
    public Ghost(int id, Point3f position) {
        super(id, 0, position, new Vector3f(0,0,0), 0);
    }

    /**
     * Ghost updates it's position automatically, no need to implement anything
     */
    @Override
    public void update(long deltaTime) {

    }
}
