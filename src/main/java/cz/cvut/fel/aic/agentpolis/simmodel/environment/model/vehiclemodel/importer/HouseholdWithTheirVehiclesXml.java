package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * The class provides a link between a household and its assigned vehicles
 * 
 * @author Zbynek Moler
 * 
 */
public class HouseholdWithTheirVehiclesXml {

	@XmlElement(required = true)
	private HouseHoldIdXml houseHoldIdKey;

	@XmlElement(required = true)
	private List<VehicleTemplateIdXml> vehicleTemplateIdsValue;

	@SuppressWarnings("unused")
    private HouseholdWithTheirVehiclesXml() {
		super();
	}

	public HouseholdWithTheirVehiclesXml(HouseHoldIdXml houseHoldIdKey, List<VehicleTemplateIdXml> vehicleTemplateIdsValue) {
		super();
		this.houseHoldIdKey = houseHoldIdKey;
		this.vehicleTemplateIdsValue = vehicleTemplateIdsValue;
	}

	public HouseHoldIdXml getHouseHoldIdKey() {
		return houseHoldIdKey;
	}

	public List<VehicleTemplateIdXml> getVehicleTemplateIdsValue() {
		return vehicleTemplateIdsValue;
	}

}
