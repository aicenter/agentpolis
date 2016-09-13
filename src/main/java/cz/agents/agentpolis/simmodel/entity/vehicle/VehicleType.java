package cz.agents.agentpolis.simmodel.entity.vehicle;

import cz.agents.agentpolis.simmodel.entity.EntityType;

/**
 * Vehicle types
 * 
 * @author Zbynek Moler
 * */
public enum VehicleType implements EntityType {
    CAR("Car"), BICYCLE("Bicycle"), MOTORBIKE("Motorbike"), TAXI("Taxi"), METRO("Metro"), BUS("Bus"), TRAM(
            "Tram"), TRAIN("Train");

    private final String vehicleName;

    private VehicleType(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getDescriptionEntityType() {
        return vehicleName;
    }

}
