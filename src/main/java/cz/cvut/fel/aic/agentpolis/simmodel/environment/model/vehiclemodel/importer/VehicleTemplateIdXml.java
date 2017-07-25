package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer;

import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * Each {@code VehicleTemplate} should be associated with
 * {@code VehicleTemplateId}, which should identifies a particular vehicle.
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleTemplateIdXml {

	@XmlElement(required = true)
	private String vehicleTemplateId;

	public VehicleTemplateIdXml() {

	}

	public VehicleTemplateIdXml(String vehicleTemplateId) {
		super();
		this.vehicleTemplateId = vehicleTemplateId;
	}

	public String getVehicleTemplateId() {
		return vehicleTemplateId;
	}

}
