package cz.agents.agentpolis.simmodel.entity.vehicle;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.simmodel.environment.model.VehiclePositionModel;
import cz.agents.agentpolis.simmodel.environment.model.VehicleStorage;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.agentpolis.simmodel.environment.model.entityvelocitymodel.EntityVelocityModel;
import cz.agents.agentpolis.simmodel.environment.model.vehiclemodel.VehicleDataModel;
import cz.agents.agentpolis.simmodel.environment.model.vehiclemodel.VehicleTemplate;
import cz.agents.agentpolis.simmodel.environment.model.vehiclemodel.VehicleTemplateId;
import cz.agents.agentpolis.utils.VelocityConverter;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.TreeMap;

/**
 * @author Marek Cuchy
 */
@Singleton
public class VehicleFactory {

	private static final Logger logger = Logger.getLogger(VehicleFactory.class);

	private final VehicleDataModel vehicleDataModel;
	private final VehicleStorage vehicleStorage;
	private final VehiclePositionModel vehiclePositionModel;
	private final EntityVelocityModel velocityModel;

	private final TransportNetworks graphs;

	private final Random random;

	@Inject
	public VehicleFactory(VehicleDataModel vehicleDataModel, VehicleStorage vehicleStorage,
						  VehiclePositionModel vehiclePositionModel, EntityVelocityModel velocityModel,
						  TransportNetworks graphs, Random random) {
		super();
		this.vehicleDataModel = vehicleDataModel;
		this.vehicleStorage = vehicleStorage;
		this.vehiclePositionModel = vehiclePositionModel;
		this.velocityModel = velocityModel;
		this.graphs = graphs;
		this.random = random;
	}

	public PhysicalVehicle createAndAddCarToModels(int id, int initialPosition) {
		return createAndAddVehicleToModels(id, VehicleType.CAR, EGraphType.HIGHWAY, initialPosition);
	}

	public PhysicalVehicle createAndAddBikeToModels(int id, int initialPosition) {
		return createAndAddVehicleToModels(id, VehicleType.BICYCLE, EGraphType.BIKEWAY, initialPosition);
	}

	public PhysicalVehicle createAndAddVehicleToModels(int id, VehicleType vehicleType, GraphType graphType,
											   int initialPosition) {
		VehicleTemplate vehicleTemplate = selectVehicleTemplate(vehicleType);
		PhysicalVehicle vehicle = new PhysicalVehicle(vehicleType.getDescriptionEntityType() + id, vehicleType, vehicleTemplate
				.lengthInMeter, vehicleTemplate.passengerCapacity, graphType);

		checkInitialPosition(vehicle.getId(), vehicleType, graphType, initialPosition);

		vehicleDataModel.assignVehicleTemplate(vehicle.getId(), vehicleTemplate.vehicleTemplateId);

		vehicleStorage.addEntity(vehicle);

		velocityModel.addEntityMaxVelocity(vehicle.getId(), VelocityConverter.kmph2mps(vehicleTemplate
				.averageVehicleSpeedInKmPerHour));

		vehiclePositionModel.setNewEntityPosition(vehicle.getId(), initialPosition);
		return vehicle;
	}

	private void checkInitialPosition(String id, VehicleType vehicleType, GraphType graphType, int initialPosition) {
		if (!graphs.getGraph(graphType).containsNode(initialPosition)) {
			logger.warn("Vehicle's (" + id + ") initial position is not on allowed graph (" + graphType + ")");
		}
	}

	private VehicleTemplate selectVehicleTemplate(VehicleType vehicleType) {
		TreeMap<Double, VehicleTemplateId> dist = vehicleDataModel.getDistributionForVehicleType(vehicleType);
		Double key = dist.ceilingKey(random.nextDouble());
		VehicleTemplateId vehicleTemplateId = dist.get(key);

		return vehicleDataModel.getVehicleTemplate(vehicleTemplateId);

	}

}
