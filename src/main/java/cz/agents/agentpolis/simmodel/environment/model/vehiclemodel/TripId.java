package cz.agents.agentpolis.simmodel.environment.model.vehiclemodel;

/**
 * 
 * The trip Id representation
 * 
 * @author Zbynek Moler
 * 
 */
public class TripId {

    private final String tripId;

    public TripId(String tripId) {
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
        TripId other = (TripId) obj;
        if (tripId == null) {
            if (other.tripId != null)
                return false;
        } else if (!tripId.equals(other.tripId))
            return false;
        return true;
    }

}
