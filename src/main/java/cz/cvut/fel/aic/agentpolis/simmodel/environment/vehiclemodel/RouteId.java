package cz.cvut.fel.aic.agentpolis.simmodel.environment.vehiclemodel;

/**
 * 
 * The route Id representation
 * 
 * @author Zbynek Moler
 * 
 */
public class RouteId {

    private final String routeId;

    public RouteId(String routeId) {
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
        RouteId other = (RouteId) obj;
        if (routeId == null) {
            if (other.routeId != null)
                return false;
        } else if (!routeId.equals(other.routeId))
            return false;
        return true;
    }

}
