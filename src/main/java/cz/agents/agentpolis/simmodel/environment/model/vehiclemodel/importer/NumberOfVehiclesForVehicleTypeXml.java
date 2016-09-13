package cz.agents.agentpolis.simmodel.environment.model.vehiclemodel.importer;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

import cz.agents.agentpolis.simmodel.entity.vehicle.VehicleType;

/**
 * 
 * The statistical information about the number of vehicle per a
 * {@code VehicleType}
 * 
 * @author Zbynek Moler
 * 
 */
public class NumberOfVehiclesForVehicleTypeXml {

	@XmlElement(required = true)
	public VehicleType vehicleTypeKey;
	@XmlElement(required = true)
	public BigDecimal numberOfVehiclesValue;

	@SuppressWarnings("unused")
    private NumberOfVehiclesForVehicleTypeXml() {
	}

	public NumberOfVehiclesForVehicleTypeXml(VehicleType vehicleType, BigDecimal numberOfVehicles) {
		super();
		this.vehicleTypeKey = vehicleType;
		this.numberOfVehiclesValue = numberOfVehicles;
	}

}
