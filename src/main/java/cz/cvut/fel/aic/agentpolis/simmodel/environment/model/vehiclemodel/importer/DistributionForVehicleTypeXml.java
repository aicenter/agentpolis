package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.VehicleType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * The distribution over the {@code VehicleType}
 * 
 * @author Zbynek Moler
 * 
 */
@XmlType
public class DistributionForVehicleTypeXml {

	@XmlElement(required = true)
	public VehicleType vehicleType;
	@XmlElement(required = true)
	public VehicleTempletIdWithPercentageXml vehicleTempletIdWithPercentage;

	@SuppressWarnings("unused")
    private DistributionForVehicleTypeXml() {
	}

	public DistributionForVehicleTypeXml(VehicleType vehicleType,
										 VehicleTempletIdWithPercentageXml vehicleTempletIdWithPercentage) {
		super();
		this.vehicleType = vehicleType;
		this.vehicleTempletIdWithPercentage = vehicleTempletIdWithPercentage;
	}

}
