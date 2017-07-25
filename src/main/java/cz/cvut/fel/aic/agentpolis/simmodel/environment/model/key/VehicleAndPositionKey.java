package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.key;


public class VehicleAndPositionKey {
	private final long positionByNodeId;
	private final String vehicleId;

	public VehicleAndPositionKey(long positionByNodeId, String vehicleId) {
		super();
		this.positionByNodeId = positionByNodeId;
		this.vehicleId = vehicleId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (positionByNodeId ^ (positionByNodeId >>> 32));
		result = prime * result + ((vehicleId == null) ? 0 : vehicleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		VehicleAndPositionKey other = (VehicleAndPositionKey) obj;
		if (positionByNodeId != other.positionByNodeId) {
			return false;
		}
		if (vehicleId.equals(other.vehicleId) == false) {
			return false;
		}
		return true;
	}

}
