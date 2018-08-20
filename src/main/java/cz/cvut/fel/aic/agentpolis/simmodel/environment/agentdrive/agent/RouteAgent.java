package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.agent;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.maneuver.CarManeuver;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.VehicleSensor;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.Action;
import org.apache.log4j.Logger;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import java.util.List;

/**
 * Created by martin on 9.7.14.
 */
public class RouteAgent extends Agent {

    private final static Logger logger = Logger.getLogger(RouteAgent.class);
    protected ManeuverTranslator maneuverTranslator;

    @Override
    public Point3f getInitialPosition() {

        Point2f p = navigator.getInitialPosition();
        return new Point3f(p.x, p.y, 0);
    }

    public RouteAgent(int id) {
        super(id);
        maneuverTranslator = new ManeuverTranslator(id, navigator);
    }

    public void addSensor(final VehicleSensor sensor) {
        this.sensor = sensor;
        maneuverTranslator.setSensor(this.sensor);
    }

    /**
     * Generate an action as a reaction
     * Rout agent is adjusting speed according to the degrees of the curves and how many waypoints before himself will calculate.
     *
     * @return
     */
    public List<Action> agentReact() {
        maneuverTranslator.setSensor(sensor);
        maneuverTranslator.setNavigator(navigator);
        return maneuverTranslator.prepare();
    }

    protected List<Action> agentReact(CarManeuver maneuver) {
        maneuverTranslator.setSensor(sensor);
        maneuverTranslator.setNavigator(navigator);
        return maneuverTranslator.translate(maneuver);
    }
}
