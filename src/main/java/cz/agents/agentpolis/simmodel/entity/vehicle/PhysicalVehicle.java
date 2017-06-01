package cz.agents.agentpolis.simmodel.entity.vehicle;

import cz.agents.agentpolis.siminfrastructure.description.DescriptionImpl;
import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.basestructures.Node;

/**
 * 
 * Representation of vehicle
 * 
 * @author Zbynek Moler
 * */
public abstract class PhysicalVehicle extends Vehicle{

    private final double lengthInMeters; 
    private final int vehiclePassengerCapacity; // number of passenger, including driver
    private final EntityType vehicleType;
    private final GraphType usingGraphTypeForMoving;

    public PhysicalVehicle(String vehicleId,            
            EntityType type, 
            double lengthInMeters,
            int vehiclePassengerCapacity, 
            GraphType usingGraphTypeForMoving, Node position) {
        super(vehicleId, position);
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

    @Override
    public double getVelocity() {
        return 0;
    }

}
