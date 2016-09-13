package cz.agents.agentpolis.simmodel.environment.model.publictransport;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import cz.agents.basestructures.Node;

/**
 * Implementation of public transport station, it consists of station name and list of lines which are stopping on this
 * station. Station is linked by graph with nodeId.
 *
 * @author Libor Wagner
 * @author Ondrej Milenovsky
 * @author Zbynek Moler
 */

public class PTStop implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 5891315525509890163L;

	private final String stationId;
	private final int stationPositionByNodeId;

	private final boolean pickupAvailable;
	private final boolean dropOffAvailable;

	// TODO> Remove
	private final Node node;

	/**
	 * key is line id
	 */
	private final Map<String, StationTimeTable> timeTables;

	public PTStop(String stationId, int stationPositionByNodeId, Node node, boolean pickupAvailable,
				  boolean dropOffAvailable) {
		timeTables = new HashMap<>();

		this.stationId = stationId;
		this.stationPositionByNodeId = stationPositionByNodeId;

		this.node = node;

		this.pickupAvailable = pickupAvailable;
		this.dropOffAvailable = dropOffAvailable;

	}

	public String getStationId() {
		return stationId;
	}

	public int getStationPositionByNodeId() {
		return stationPositionByNodeId;
	}

	public Node getNode() {
		return node;
	}

	public boolean isDropOffAvailable() {
		return dropOffAvailable;
	}

	public boolean isPickupAvailable() {
		return pickupAvailable;
	}

	public StationTimeTable getTimeTable(String lineId) {
		return timeTables.get(lineId);
	}

	public void addDepartureTimeTable(String lineId, Duration departureTimeInDayRange) {
		StationTimeTable stationTimeTable = timeTables.get(lineId);
		if (stationTimeTable == null) {
			stationTimeTable = new StationTimeTable();
		}

		stationTimeTable.addDepartureTime(departureTimeInDayRange);

		timeTables.put(lineId, stationTimeTable);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		PTStop other = (PTStop) obj;
		if (stationId == null) {
			if (other.stationId != null) return false;
		} else if (!stationId.equals(other.stationId)) return false;
		return true;
	}

}
