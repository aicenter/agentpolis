package cz.agents.agentpolis.simmodel.environment.model.vehiclemodel.importer;

import javax.xml.bind.annotation.XmlElement;

/**
 * The unique route (GTFS) id for the XML serialization/deserialization
 * 
 * @author Zbynek Moler
 * 
 */
public class RouteIdXml {

	@XmlElement(required = true)
	private String routeId;

	@SuppressWarnings("unused")
    private RouteIdXml() {
		super();
	}

	public RouteIdXml(String routeId) {
		super();
		this.routeId = routeId;
	}

	public String getRouteId() {
		return routeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((routeId == null) ? 0 : routeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RouteIdXml other = (RouteIdXml) obj;
		if (routeId == null) {
			if (other.routeId != null)
				return false;
		} else if (!routeId.equals(other.routeId))
			return false;
		return true;
	}

}
