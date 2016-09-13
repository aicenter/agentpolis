package cz.agents.agentpolis.simulator.visualization.googleearth.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Injector;

import cz.agents.agentpolis.apgooglearth.data.EntityGE;
import cz.agents.agentpolis.apgooglearth.data.StyleGE;
import cz.agents.agentpolis.apgooglearth.regionbounds.RegionBounds;
import cz.agents.agentpolis.apgooglearth.updates.VehicleUpdateGE;
import cz.agents.agentpolis.apgooglearth.vehicle.IVehicleGE;
import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.agents.agentpolis.simmodel.environment.model.VehiclePositionModel;
import cz.agents.agentpolis.simmodel.environment.model.VehicleStorage;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.AllNetworkNodes;
import cz.agents.agentpolis.simmodel.environment.model.entityvelocitymodel.EntityVelocityModel;
import cz.agents.agentpolis.simmodel.environment.model.linkedentitymodel.LinkedEntityModel;
import cz.agents.agentpolis.simulator.visualization.googleearth.UpdateGEFactory;
import cz.agents.agentpolis.simulator.visualization.googleearth.entity.VehicleGE;
import cz.agents.agentpolis.simulator.visualization.googleearth.entity.movement.EntityMovementGE;
import cz.agents.alite.common.event.EventProcessor;
import cz.agents.alite.googleearth.cameraalt.visibility.CameraAltVisibility;
import cz.agents.alite.googleearth.updates.UpdateKmlView;
import cz.agents.basestructures.Node;

/**
 * The factory for the initialization of vehicle visualization for GE
 *
 * @author Zbynek Moler
 */
public class VehicleUpdateGEFactory extends UpdateGEFactory {

	private final StyleGE styleGE;
	private final Set<EntityType> allowedVehicleEntityType;

	public VehicleUpdateGEFactory(StyleGE styleGE, CameraAltVisibility cameraAltVisibility, String nameOfUpdateKmlView,
								  Set<EntityType> allowedVehicleEntityType) {
		super(cameraAltVisibility, nameOfUpdateKmlView);
		this.styleGE = styleGE;
		this.allowedVehicleEntityType = allowedVehicleEntityType;

	}

	public UpdateKmlView createUpdateKmlView(Injector injector, RegionBounds regionBounds) {

		Map<String, EntityGE<StyleGE>> entityData = new HashMap<>();
		Set<String> allowedVehiclesIds = new HashSet<>();
		List<Vehicle> vehicles = new ArrayList<>();

		VehicleStorage vehicleStorage = injector.getInstance(VehicleStorage.class);
		for (String entityName : vehicleStorage.getEntityIds()) {
			Vehicle vehicle = vehicleStorage.getEntityById(entityName);
			if (allowedVehicleEntityType.contains(vehicle.getType())) {
				allowedVehiclesIds.add(vehicle.getId());
				vehicles.add(vehicle);
				entityData.put(vehicle.getId(), new EntityGE<>(styleGE, vehicle.getId()));
			}
		}

		Map<Integer, ? extends Node> nodesFromAllGraphs = injector.getInstance(AllNetworkNodes.class).getAllNetworkNodes();
		EntityMovementGE entityMovement = new EntityMovementGE(nodesFromAllGraphs, injector.getInstance(EventProcessor
                .class), injector.getInstance(EntityVelocityModel.class));

		IVehicleGE vehicleGE = new VehicleGE(vehicles, injector.getInstance(VehiclePositionModel.class),
                nodesFromAllGraphs, injector.getInstance(LinkedEntityModel.class), entityMovement, allowedVehiclesIds);

		return new VehicleUpdateGE(entityData, vehicleGE, regionBounds);

	}

}
