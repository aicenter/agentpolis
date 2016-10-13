package cz.agents.agentpolis.simmodel.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import java.time.ZonedDateTime;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;
import cz.agents.agentpolis.simmodel.agent.activity.movement.callback.PassengerActivityCallback;
import cz.agents.agentpolis.simmodel.environment.model.sensor.UsingPublicTransportActivityCallback;
import cz.agents.alite.common.event.EventProcessor;

/**
 * Implementation of Guice module, which support creation of follow models including in AgentPolis environment
 * <p>
 * <ul> <li>{@code UsingPassengerTransportModel}</li> <li>{@code PublicTransportModel}</li> <li>{@code
 * VehicleTimeModel}</li> <li>{@code BeforePlanNotifyModel}</li> <li>{@code VehiclePlanNotificationModel}</li>
 * <li>{@code VehicleGroupModel}</li> <li>{@code DelayModel}</li> <li>{@code VehicleGroupModel}</li> <li>{@code
 * LinkedEntityModel}</li> <li>{@code VehiclePositionModel}</li> <li>{@code AgentPositionModel}</li> <li>{@code
 * AgentStorage}</li> <li>{@code VehicleStorage}</li> <li>{@code EntityVelocityModel}</li> <li>{@code
 * PublicTransportModel}</li> </ul>
 *
 * @author Zbynek Moler
 */
public class AgentPolisEnvironmentModule extends AbstractModule {

	private final Random random;
	

	public AgentPolisEnvironmentModule(Random random, ZonedDateTime initDate) {
		super();
		this.random = random;
	}

	@Override
	protected void configure() {

		
	}

	@Provides
	@Singleton
	Random provideRandom() {
		return random;
	}

}
