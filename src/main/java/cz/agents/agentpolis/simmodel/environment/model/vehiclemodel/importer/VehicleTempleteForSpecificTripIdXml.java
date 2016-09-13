package cz.agents.agentpolis.simmodel.environment.model.vehiclemodel.importer;

import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * The class provides an association between {@code TripId} and
 * {@code VehicleTemplateId}
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleTempleteForSpecificTripIdXml {

	@XmlElement(required = true)
	private TripIdXml tripId;
	@XmlElement(required = true)
	private VehicleTemplateIdXml vehicleTemplateId;

	@SuppressWarnings("unused")
    private VehicleTempleteForSpecificTripIdXml() {
		super();
	}

	public VehicleTempleteForSpecificTripIdXml(TripIdXml tripId, VehicleTemplateIdXml vehicleTemplateId) {
		super();
		this.tripId = tripId;
		this.vehicleTemplateId = vehicleTemplateId;
	}

	public TripIdXml getTripId() {
		return tripId;
	}

	public VehicleTemplateIdXml getVehicleTemplateId() {
		return vehicleTemplateId;
	}

}
