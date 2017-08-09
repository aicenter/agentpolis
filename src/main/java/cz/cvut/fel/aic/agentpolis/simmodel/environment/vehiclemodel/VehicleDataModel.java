package cz.cvut.fel.aic.agentpolis.simmodel.environment.vehiclemodel;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.VehicleType;

/**
 * 
 * The main structure holding/providing the vehicle data model. The vehicle data
 * model holds the statistical information provided as the input from user. It
 * could used for generating the vehicle distribution in a simulation model.
 * 
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleDataModel {

    private final Map<VehicleTemplateId, VehicleTemplate> vehicleTemplates;
    private final Map<VehicleType, Double> numberOfVehiclesForEachVehicleType;
    private final Map<HouseholdId, List<VehicleTemplateId>> householdsWithTheirVehicles;
    private final Map<RouteId, VehicleTemplateId> specificVehicleTempleteForRouteId;
    private final Map<TripId, VehicleTemplateId> specificVehicleTempleteForTripId;
    private final Map<VehicleType, TreeMap<Double, VehicleTemplateId>> distributionForEachVehicleType;

    private final Map<String, VehicleTemplateId> assignedVehicleTempletedToVehicleId;

    public VehicleDataModel(Map<VehicleTemplateId, VehicleTemplate> vehicleTemplates,
            Map<VehicleType, Double> numberOfVehiclesForEachVehicleType,
            Map<HouseholdId, List<VehicleTemplateId>> householdsWithTheirVehicles,
            Map<RouteId, VehicleTemplateId> specificVehicleTempleteForRouteId,
            Map<TripId, VehicleTemplateId> specificVehicleTempleteForTripId,
            Map<VehicleType, TreeMap<Double, VehicleTemplateId>> distributionForEachVehicleType,
            Map<String, VehicleTemplateId> assignedVehicleTempletedToVehicleId) {
        super();
        this.vehicleTemplates = vehicleTemplates;
        this.numberOfVehiclesForEachVehicleType = numberOfVehiclesForEachVehicleType;
        this.householdsWithTheirVehicles = householdsWithTheirVehicles;
        this.specificVehicleTempleteForRouteId = specificVehicleTempleteForRouteId;
        this.specificVehicleTempleteForTripId = specificVehicleTempleteForTripId;
        this.distributionForEachVehicleType = distributionForEachVehicleType;
        this.assignedVehicleTempletedToVehicleId = assignedVehicleTempletedToVehicleId;
    }

    public void assignVehicleTemplate(String vehicleId, VehicleTemplateId vehicleTemplateId) {
        assignedVehicleTempletedToVehicleId.put(vehicleId, vehicleTemplateId);
    }

    /**
     * Returns a vehicle template for the specified vehicle.
     * 
     * @param vehicleId
     *            Identifier of the vehicle.
     * @return A VehicleTemplate object.
     * @exception NoSuchElementException
     *                There is no template for the given vehicle; (this usually
     *                means either that the vehicle ID is wrong, or that the
     *                database of vehicle templates is incomplete).
     */
    public VehicleTemplate getVehicleTemplate(String vehicleId) {
        VehicleTemplate template = vehicleTemplates.get(assignedVehicleTempletedToVehicleId
                .get(vehicleId));
        if (template == null) {
            throw new NoSuchElementException("Could not find vehicle template for vehicleId='"
                    + vehicleId + "' (probable cause: incomplete vehicle model data)");
        }
        return template;
    }

    /**
     * Returns a vehicle template.
     * 
     * @param vehicleTemplateId
     *            Identifier of the template.
     * @return A VehicleTemplate object.
     * @exception NoSuchElementException
     *                There is no template with this ID (this usually means that
     *                either the ID is wrong, or that the database of vehicle
     *                templates is incomplete).
     */
    public VehicleTemplate getVehicleTemplate(VehicleTemplateId vehicleTemplateId) {
        VehicleTemplate template = vehicleTemplates.get(vehicleTemplateId);
        if (template == null) {
            throw new NoSuchElementException(
                    "Could not find vehicle template for vehicleTemplateId='" + vehicleTemplateId
                            + "' (probable cause: incomplete vehicle model data)");
        }
        return template;
    }

    public TreeMap<Double, VehicleTemplateId> getDistributionForVehicleType(VehicleType vehicleType) {
        return distributionForEachVehicleType.get(vehicleType);
    }

    public VehicleTemplateId getVehicleTemplateIdForGivenTripId(String tripId) {
        return specificVehicleTempleteForTripId.get(new TripId(tripId));
    }

    public VehicleTemplateId getVehicleTemplateIdForGivenRouteId(String routeId) {
        return specificVehicleTempleteForRouteId.get(new RouteId(routeId));
    }

    public Double getNumberOfVehicles(VehicleType vehicleType) {
        return numberOfVehiclesForEachVehicleType.get(vehicleType);
    }

    public List<VehicleTemplateId> getHouseholdVehicles(HouseholdId householdId) {
        return householdsWithTheirVehicles.get(householdId);
    }

}
