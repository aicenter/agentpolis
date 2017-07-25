package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.publictransport;

/**
 * 
 * The type of public transport
 * 
 * @author Zbynek Moler
 * 
 */
public enum EPublicTransportType implements PublicTransportType {
    TRAM("Tram"), METRO("Metro"), BUS("Bus"), TRAIN("Train");

    private final String publicTransportTypeName;

    private EPublicTransportType(String publicTransportTypeName) {
        this.publicTransportTypeName = publicTransportTypeName;
    }

    @Override
    public String getPublicTransportTypeName() {
        return publicTransportTypeName;
    }
}
