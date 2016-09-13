package cz.agents.agentpolis.simmodel.environment.model.vehiclemodel.importer;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * The class is a holder for the percentage value for a particular vehicle.
 * 
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleTempletIdWithPercentageXml {

	@XmlElement(required = true)
	public VehicleTemplateIdXml vehicleTemplateId;
	@XmlElement(required = true)
	public BigDecimal percentageOfVehicleInVehicleType;

	@SuppressWarnings("unused")
    private VehicleTempletIdWithPercentageXml() {
		super();
	}

	public VehicleTempletIdWithPercentageXml(VehicleTemplateIdXml vehicleTemplateId,
											 BigDecimal percentageOfVehicleInVehicleType) {
		super();
		this.vehicleTemplateId = vehicleTemplateId;
		this.percentageOfVehicleInVehicleType = percentageOfVehicleInVehicleType;
	}

}
