package cz.cvut.fel.aic.agentpolis.simmodel.agent.activity.movement;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.MovementActivityLogger;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.AgentPositionAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.delaymodel.action.DelayAction;

/**
 * The movement activity where executed trip does not depends on a time
 * constrains on a particular trip item
 * 
 * @author Zbynek Moler
 * 
 */
public class MovementActivityTripItem extends MovementActivity<TripItem> {

    @Inject
    public MovementActivityTripItem(DelayAction queueAction,
            AgentPositionAction agentPositionAction, TransportNetworks transportNetworks,
            MovementActivityLogger movementActivityLogger) {
        super(queueAction, agentPositionAction, transportNetworks, movementActivityLogger);
        // TODO Auto-generated constructor stub
    }

}
