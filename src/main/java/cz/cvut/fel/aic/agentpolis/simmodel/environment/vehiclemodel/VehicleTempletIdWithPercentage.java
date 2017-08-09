package cz.cvut.fel.aic.agentpolis.simmodel.environment.vehiclemodel;

import java.math.BigDecimal;

/**
 * 
 * The percentage of a vehicle template of a particular vehicle type over all
 * vehicle template in an user input data
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleTempletIdWithPercentage {

    public final VehicleTemplateId vehicleTemplateId;
    public final BigDecimal percentageOfVehicleInVehicleType;

    public VehicleTempletIdWithPercentage(VehicleTemplateId vehicleTemplateId,
            BigDecimal percentageOfVehicleInVehicleType) {
        super();
        this.vehicleTemplateId = vehicleTemplateId;
        this.percentageOfVehicleInVehicleType = percentageOfVehicleInVehicleType;
    }

}
