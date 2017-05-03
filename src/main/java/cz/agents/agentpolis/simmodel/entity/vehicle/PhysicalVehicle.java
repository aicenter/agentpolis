package cz.agents.agentpolis.simmodel.entity.vehicle;

import cz.agents.agentpolis.siminfrastructure.description.DescriptionImpl;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;

/**
 * 
 * Representation of vehicle
 * 
 * @author Zbynek Moler
 * */
public class PhysicalVehicle extends AgentPolisEntity implements Vehicle{

    private final double lengthInMeters; 
    private final int vehiclePassengerCapacity; // number of passenger, included driver
    private final EntityType vehicleType;
    private final GraphType usingGraphTypeForMoving;

    public PhysicalVehicle(String vehicleId,            
            EntityType type, 
            double lengthInMeters,
            int vehiclePassengerCapacity, 
            GraphType usingGraphTypeForMoving) {
        super(vehicleId);
        this.lengthInMeters = lengthInMeters;
        this.vehiclePassengerCapacity = vehiclePassengerCapacity;
        this.vehicleType = type;
        this.usingGraphTypeForMoving = usingGraphTypeForMoving;
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

}
