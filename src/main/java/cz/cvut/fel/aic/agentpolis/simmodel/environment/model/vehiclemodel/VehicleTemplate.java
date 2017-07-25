package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.VehicleType;

/**
 * 
 * It is description of a particular vehicle including information both for
 * setting up a vehicle in a simulation model and for calculation data for
 * simulation output.
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleTemplate {

    // Limits for various vehicle properties.
    // Note that these ranges are intentionally absurdly wide:
    // they are meant to catch computational errors
    // and corrupted vehicle databases (i.e. mathematical nonsense,
    // not physical).

    public static final double MIN_LENGTH_IN_METERS = 0.1;
    public static final double MAX_LENGTH_IN_METERS = 10000.0;
    public static final int MIN_PASSENGER_CAPACITY = 1;
    public static final int MAX_PASSENGER_CAPACITY = 10000;
    public static final int MIN_SPEED_KMH = 1;
    public static final int MAX_SPEED_KMH = 10000;
    
    public final VehicleTemplateId vehicleTemplateId;
    public final int passengerCapacity;
    public final double lengthInMeter;

    public final TransportType transportType;
    public final VehicleType vehicleType;

    public final double produceCO2InGramPerKm;
    public final double produceCOInGramPerKm;
    public final double produceNOxInGramPerKm;
    public final double produceSOxInGramPerKm;
    public final double producePM10InGramPerKm;

    public final double averageVehicleSpeedInKmPerHour;

    public final double consumeFuelInLitersPer100Km;
    public final double consumeElectricityInkWhourPer100Km;

    public VehicleTemplate(VehicleTemplateId vehicleTemplateId, int passengerCapacity,
            double lengthInMeter, TransportType transportType, VehicleType vehicleType,
            double produceCO2InGramPerKm, double produceCOInGramPerKm,
            double produceNOxInGramPerKm, double produceSOxInGramPerKm,
            double producePM10InGramPerKm, double averageVehicleSpeedInKmPerHour,
            double consumeFuelInLitersPer100Km, double consumeElectricityInkWhourPer100Km) {
        super();
        
        if (lengthInMeter < MIN_LENGTH_IN_METERS || lengthInMeter > MAX_LENGTH_IN_METERS) {
            throw new IllegalArgumentException("Invalid vehicle length ("
                    + lengthInMeter + ") for " + vehicleTemplateId);
        }
        if (passengerCapacity < MIN_PASSENGER_CAPACITY || passengerCapacity > MAX_PASSENGER_CAPACITY) {
            throw new IllegalArgumentException("Invalid passenger capacity ("
                    + passengerCapacity + ") for " + vehicleTemplateId);            
        }
        if (averageVehicleSpeedInKmPerHour < MIN_SPEED_KMH || averageVehicleSpeedInKmPerHour > MAX_SPEED_KMH) {
            throw new IllegalArgumentException("Invalid average vehicle speed ("
                    + averageVehicleSpeedInKmPerHour + " km/h) for " + vehicleTemplateId);
        }
        if (consumeFuelInLitersPer100Km < 0 || consumeElectricityInkWhourPer100Km < 0) {
            
            // NOTE: If there is ever need for a vehicle that really produces power,
            // please check if the rest of the code behaves reasonably under these
            // circumstances, and then remove this error condition.
            throw new IllegalArgumentException("Negative fuel/electricity consumption specified for "
                    + vehicleTemplateId);
        }
        if (produceCO2InGramPerKm < 0 || produceCOInGramPerKm < 0 || produceNOxInGramPerKm < 0
                || produceSOxInGramPerKm < 0 || producePM10InGramPerKm < 0) {
            throw new IllegalArgumentException("Negative gas production specified for "
                    + vehicleTemplateId);
        }

        this.vehicleTemplateId = vehicleTemplateId;
        this.passengerCapacity = passengerCapacity;
        this.lengthInMeter = lengthInMeter;
        this.transportType = transportType;
        this.vehicleType = vehicleType;
        this.produceCO2InGramPerKm = produceCO2InGramPerKm;
        this.produceCOInGramPerKm = produceCOInGramPerKm;
        this.produceNOxInGramPerKm = produceNOxInGramPerKm;
        this.produceSOxInGramPerKm = produceSOxInGramPerKm;
        this.producePM10InGramPerKm = producePM10InGramPerKm;
        this.averageVehicleSpeedInKmPerHour = averageVehicleSpeedInKmPerHour;
        this.consumeFuelInLitersPer100Km = consumeFuelInLitersPer100Km;
        this.consumeElectricityInkWhourPer100Km = consumeElectricityInkWhourPer100Km;
    }

}
