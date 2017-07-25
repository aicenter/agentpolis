package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer;

import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * The class provides an association between {@code RouteId} and
 * {@code VehicleTemplateId}
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleTempleteForSpecificRouteIdXml {

	@XmlElement(required = true)
	private RouteIdXml routeId;
	@XmlElement(required = true)
	private VehicleTemplateIdXml vehicleTemplateId;

	@SuppressWarnings("unused")
    private VehicleTempleteForSpecificRouteIdXml() {
		super();
	}

	public VehicleTempleteForSpecificRouteIdXml(RouteIdXml routeId, VehicleTemplateIdXml vehicleTemplateId) {
		super();
		this.routeId = routeId;
		this.vehicleTemplateId = vehicleTemplateId;
	}

	public RouteIdXml getRouteId() {
		return routeId;
	}

	public VehicleTemplateIdXml getVehicleTemplateId() {
		return vehicleTemplateId;
	}

}
