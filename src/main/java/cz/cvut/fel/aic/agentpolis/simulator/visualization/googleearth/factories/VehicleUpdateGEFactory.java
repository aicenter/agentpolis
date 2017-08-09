package cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.factories;

import com.google.inject.Injector;
import cz.agents.agentpolis.apgooglearth.data.EntityGE;
import cz.agents.agentpolis.apgooglearth.data.StyleGE;
import cz.agents.agentpolis.apgooglearth.regionbounds.RegionBounds;
import cz.agents.agentpolis.apgooglearth.updates.VehicleUpdateGE;
import cz.agents.agentpolis.apgooglearth.vehicle.IVehicleGE;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.VehicleStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.UpdateGEFactory;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.entity.movement.EntityMovementGE;
import cz.agents.alite.common.event.EventProcessor;
import cz.agents.alite.googleearth.cameraalt.visibility.CameraAltVisibility;
import cz.agents.alite.googleearth.updates.UpdateKmlView;
import cz.cvut.fel.aic.geographtools.Node;

import java.util.*;

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
		List<PhysicalVehicle> vehicles = new ArrayList<>();

		VehicleStorage vehicleStorage = injector.getInstance(VehicleStorage.class);
		for (String entityName : vehicleStorage.getEntityIds()) {
			PhysicalVehicle vehicle = vehicleStorage.getEntityById(entityName);
			if (allowedVehicleEntityType.contains(vehicle.getType())) {
				allowedVehiclesIds.add(vehicle.getId());
				vehicles.add(vehicle);
				entityData.put(vehicle.getId(), new EntityGE<>(styleGE, vehicle.getId()));
			}
		}

		Map<Integer, ? extends Node> nodesFromAllGraphs = injector.getInstance(AllNetworkNodes.class).getAllNetworkNodes();
		EntityMovementGE entityMovement = new EntityMovementGE(nodesFromAllGraphs, injector.getInstance(EventProcessor
                .class));

//		IVehicleGE vehicleGE = new VehicleGE(vehicles, injector.getInstance(VehiclePositionModel.class),
//                nodesFromAllGraphs, injector.getInstance(LinkedEntityModel.class), entityMovement, allowedVehiclesIds);

        IVehicleGE vehicleGE = null;

		return new VehicleUpdateGE(entityData, vehicleGE, regionBounds);

	}

}
