package cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.agents.agentpolis.apgooglearth.regionbounds.RegionBounds;
import cz.agents.agentpolis.apgooglearth.vehicle.IVehicleGE;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.linkedentitymodel.LinkedEntityModel;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.entity.movement.EntityMovementGE;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.utils.DescriptionGEUtil;
import cz.cvut.fel.aic.geographtools.Node;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;

/**
 * The vehicle representation for GE visualization
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleGE implements IVehicleGE {

    private final LinkedEntityModel linkedEntityStorage;

    private final List<PhysicalVehicle> vehicles;

    private static final String END_LINE_HTML = "<br>";
    private static final String PASSENGER_ON_BOARD = "On-board passangers / capacity: ";
    private static final String CURRENT_VEHICLE_POSITION = "Current vehicle position: ";

    private final Map<Integer, ? extends Node> nodesFromAllGraphs;
    private final EntityMovementGE entityMovement;

    private final Set<String> allowedVehiclesIds;

    public VehicleGE(List<PhysicalVehicle> vehicles, final Map<Integer, ? extends Node> nodesFromAllGraphs,
            LinkedEntityModel linkedEntityStorage, EntityMovementGE entityMovement,
            Set<String> allowedVehiclesIds) {
        super();
        this.vehicles = vehicles;
        this.nodesFromAllGraphs = nodesFromAllGraphs;
        this.linkedEntityStorage = linkedEntityStorage;
        this.entityMovement = entityMovement;
        this.allowedVehiclesIds = allowedVehiclesIds;
    }

    public Map<String, String> getDescription() {

        Map<String, String> vehicleDescription = new HashMap<>();

        for (PhysicalVehicle vehicle : vehicles) {
            StringBuilder html = new StringBuilder(
                    DescriptionGEUtil.transformDescriptionToHTML(vehicle.getDescription()));

//            Integer currentVehiclePositionById = vehiclePositionStorage
//                    .getEntityPositionByNodeId(vehicle.getId());
            Integer currentVehiclePositionById = 0;
            Node currentVehiclePosition = nodesFromAllGraphs.get(currentVehiclePositionById);
            int numberOfPassengerInVehicle = linkedEntityStorage
                    .numOfLinkedEntites(vehicle.getId());
            int capacityOfVehicle = vehicle.getCapacity();

            if (currentVehiclePosition != null) {
                html.append(CURRENT_VEHICLE_POSITION);
                html.append(END_LINE_HTML);
                html.append(END_LINE_HTML);
                html.append(PASSENGER_ON_BOARD);
                html.append(numberOfPassengerInVehicle);
                html.append('/');
                html.append(capacityOfVehicle);

            }

            vehicleDescription.put(vehicle.getId(), html.toString());

        }

        return vehicleDescription;
    }

    public Map<String, Coordinate> getEntityPosition(RegionBounds activeRegionBounds) {
        // Map<String, Coordinate> map = new HashMap<String, Coordinate>();
        //
        // for(String id: vehiclePositionStorage.getIDs()){
        // Long l = vehiclePositionStorage.getEntityPositionByNodeId(id);
        //
        //
        //
        // map.put(id,new
        // Coordinate(nodesFromAllGraphs.get(l).getLatLon().lon(),
        // nodesFromAllGraphs.get(l).getLatLon().lat()) );
        // }
        // return map;
//        return entityMovement.getEntityPosition(allowedVehiclesIds, vehiclePositionStorage,
//                activeRegionBounds);

        return entityMovement.getEntityPosition(allowedVehiclesIds, activeRegionBounds);

    }

    public Map<String, Long> getNumOfAgentInVehicle() {
        return new HashMap<>();
    }

}
