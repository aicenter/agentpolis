package cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.description.DescriptionImpl;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;

/**
 * Representation of vehicle
 *
 * @author Zbynek Moler
 */
public class PhysicalVehicle extends Vehicle {

    private final double lengthInMeters;
    private final int vehiclePassengerCapacity; // number of passenger, including driver
    private final EntityType vehicleType;
    private final GraphType usingGraphTypeForMoving;

    private final double maxVelocity;

    public PhysicalVehicle(String vehicleId, EntityType type, double lengthInMeters, int vehiclePassengerCapacity,
                           GraphType usingGraphTypeForMoving, SimulationNode position, double maxVelocity) {
        super(vehicleId, position);
        this.lengthInMeters = lengthInMeters;
        this.vehiclePassengerCapacity = vehiclePassengerCapacity;
        this.vehicleType = type;
        this.usingGraphTypeForMoving = usingGraphTypeForMoving;
        this.maxVelocity = maxVelocity;
    }

    @Override
    public EntityType getType() {
        return vehicleType;
    }

    public int getCapacity() {
        return vehiclePassengerCapacity;
    }

    public double getLength() {
        return lengthInMeters;
    }

    public GraphType getGraphForMovingBaseOnType() {
        return usingGraphTypeForMoving;
    }

    @Override
    public DescriptionImpl getDescription() {
        return new DescriptionImpl();
    }

    @Override
    public double getVelocity() {
        return maxVelocity;
    }

}
