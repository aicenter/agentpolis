package cz.agents.agentpolis.simmodel.environment.model.vehiclemodel;

/**
 * 
 * The Household ID representation
 * 
 * @author Zbynek Moler
 * 
 */
public class HouseholdId {

    private final String houseHoldId;

    public HouseholdId(String houseHoldId) {
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
        HouseholdId other = (HouseholdId) obj;
        if (houseHoldId == null) {
            if (other.houseHoldId != null)
                return false;
        } else if (!houseHoldId.equals(other.houseHoldId))
            return false;
        return true;
    }

}
