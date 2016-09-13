package cz.agents.agentpolis.simmodel.environment.model.vehiclemodel;

/**
 * 
 * The trip Id representation
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleTemplateId {

    private final String vehicleTemplateId;

    public VehicleTemplateId(String vehicleTemplateId) {
        super();
        this.vehicleTemplateId = vehicleTemplateId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((vehicleTemplateId == null) ? 0 : vehicleTemplateId.hashCode());
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
        VehicleTemplateId other = (VehicleTemplateId) obj;
        if (vehicleTemplateId == null) {
            if (other.vehicleTemplateId != null)
                return false;
        } else if (!vehicleTemplateId.equals(other.vehicleTemplateId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return vehicleTemplateId;
    }
}
