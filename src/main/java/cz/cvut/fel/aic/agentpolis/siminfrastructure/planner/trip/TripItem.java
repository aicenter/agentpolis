package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip;

/**
 * The atomic representation of position in a trip
 *
 * @author Zbynek Moler
 */
public class TripItem {

	public final int tripPositionByNodeId;

	public TripItem(int tripPositionByNodeId) {

		this.tripPositionByNodeId = tripPositionByNodeId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TripItem tripItem = (TripItem) o;

		return tripPositionByNodeId == tripItem.tripPositionByNodeId;

	}

	@Override
	public int hashCode() {
		return tripPositionByNodeId;
	}

	public String toString() {
		return Long.toString(tripPositionByNodeId);
	}

}
