package cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

/**
 * Simple implementation of transport vehicle with homogeneous transport slots
 */
public class SimpleTransportVehicle<T extends TransportableEntity> extends PhysicalTransportVehicle<T> {

    private final int vehiclePassengerCapacity; // number of passenger, including driver



    public int getCapacity() {
        return vehiclePassengerCapacity;
    }


    public SimpleTransportVehicle(
        int vehiclePassengerCapacity,
        String vehicleId,
        EntityType type,
        float lengthInMeters,
        GraphType usingGraphTypeForMoving,
        SimulationNode position,
        int maxVelocity
    ) {
        super(vehicleId, type, lengthInMeters, usingGraphTypeForMoving, position, maxVelocity);
        this.vehiclePassengerCapacity = vehiclePassengerCapacity;
    }

    @Override
    public boolean canTransport(T entity) {
        return true;
    }

    @Override
    public boolean hasCapacityFor(T entity) {
        return getCapacity() > getTransportedEntities().size();
    }

    @Override
    public void runPostPickUpActions(T entity) {
    }

    @Override
    public void runPostDropOffActions(T entity) {
    }
}
