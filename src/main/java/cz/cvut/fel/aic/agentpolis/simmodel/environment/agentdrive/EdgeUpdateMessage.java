package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive;

public class EdgeUpdateMessage {
    private Integer carId;

    public EdgeUpdateMessage(Integer carId) {
        this.carId = carId;
    }

    public Integer getCarId() {
        return carId;
    }
}
