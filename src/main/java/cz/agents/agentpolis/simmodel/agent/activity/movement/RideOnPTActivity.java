package cz.agents.agentpolis.simmodel.agent.activity.movement;

import com.google.inject.Inject;

import cz.agents.agentpolis.siminfrastructure.logger.agent.activity.PassengerActivityLogger;
import cz.agents.agentpolis.siminfrastructure.planner.trip.PTTrip;
import cz.agents.agentpolis.simmodel.environment.model.action.PassengerAction;
import cz.agents.agentpolis.simmodel.environment.model.action.PassengerTripAction;

/**
 * 
 * An agent is able to travel via this activity as a passenger a public
 * transport
 * 
 * @author Zbynek Moler
 * 
 */
public class RideOnPTActivity extends RideAsPassengerActivity<PTTrip> {

    @Inject
    public RideOnPTActivity(PassengerAction useVehicleAction,
            PassengerTripAction passengerTripAction, PassengerActivityLogger passengerActivityLogger) {
        super(useVehicleAction, passengerTripAction, passengerActivityLogger);
        // TODO Auto-generated constructor stub
    }

}
