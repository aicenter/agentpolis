package cz.cvut.fel.aic.agentpolis.simmodel.environment.vehiclemodel.importer;

import javax.xml.bind.annotation.XmlElement;

/**
 * The unique household id for the XML serialization/deserialization
 * 
 * @author Zbynek Moler
 * 
 */
public class HouseHoldIdXml {

	@XmlElement(required = true)
	private String houseHoldId;

	@SuppressWarnings("unused")
    private HouseHoldIdXml() {
		super();
	}

	public HouseHoldIdXml(String houseHoldId) {
		super();
		this.houseHoldId = houseHoldId;

	}

	public String getHouseHoldId() {
		return houseHoldId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((houseHoldId == null) ? 0 : houseHoldId.hashCode());
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
		HouseHoldIdXml other = (HouseHoldIdXml) obj;
		if (houseHoldId == null) {
			if (other.houseHoldId != null)
				return false;
		} else if (!houseHoldId.equals(other.houseHoldId))
			return false;
		return true;
	}

}
