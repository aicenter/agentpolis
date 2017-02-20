package cz.agents.agentpolis.simmodel.agent.activity.movement;

import com.google.inject.Inject;

import cz.agents.agentpolis.siminfrastructure.logger.agent.activity.PassengerActivityLogger;
import cz.agents.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.agents.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.agents.agentpolis.simmodel.environment.model.action.PassengerAction;
import cz.agents.agentpolis.simmodel.environment.model.action.PassengerTripAction;

/**
 * 
 * An agent is able to travel via this activity as a passenger a particular
 * vehicle
 * 
 * @author Zbynek Moler
 * 
 */
public class RideInVehicleActivity extends RideAsPassengerActivity<VehicleTrip<TripItem>> {

    @Inject
    public RideInVehicleActivity(PassengerAction useVehicleAction,
            PassengerTripAction passengerTripAction, PassengerActivityLogger passengerActivityLogger) {
        super(useVehicleAction, passengerTripAction, passengerActivityLogger);
        // TODO Auto-generated constructor stub
    }

}
