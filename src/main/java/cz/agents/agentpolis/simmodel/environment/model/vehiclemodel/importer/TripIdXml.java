package cz.agents.agentpolis.simmodel.environment.model.vehiclemodel.importer;

import javax.xml.bind.annotation.XmlElement;

/**
 * The unique trip (GTFS) id for the XML serialization/deserialization
 * 
 * @author Zbynek Moler
 * 
 */
public class TripIdXml {

	@XmlElement(required = true)
	private String tripId;

	@SuppressWarnings("unused")
    private TripIdXml() {
		super();
	}

	public TripIdXml(String tripId) {
		super();
		this.tripId = tripId;
	}

	public String getTripId() {
		return tripId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tripId == null) ? 0 : tripId.hashCode());
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
		TripIdXml other = (TripIdXml) obj;
		if (tripId == null) {
			if (other.tripId != null)
				return false;
		} else if (!tripId.equals(other.tripId))
			return false;
		return true;
	}

}
