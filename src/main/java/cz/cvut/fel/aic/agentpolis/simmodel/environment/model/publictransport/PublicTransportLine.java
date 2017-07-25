package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.publictransport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.utils.InitAndGetterUtil;

/**
 * PublicTransportLine is implementation of one line such a bus, metro or tram.
 * Has list of stations.
 * 
 * @author Libor Wagner
 * 
 */
public class PublicTransportLine implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -9204692401235379226L;

    private String routeId;

    private final String lineId;
    private final GraphType graphType;

    /** Stations on line. */
    private List<PTStop> stations;
    private Map<String, Set<Integer>> stationAndInds = new HashMap<String, Set<Integer>>();

    public PublicTransportLine(String lineId, List<PTStop> stations, final GraphType graphType) {

        this.graphType = graphType;
        this.lineId = lineId;

        this.stations = stations;

        int i = 0;
        for (PTStop station : stations) {
            Set<Integer> stationInts = InitAndGetterUtil.getDataOrInitFromMap(stationAndInds,
                    station.getStationId(), new HashSet<Integer>());
            stationInts.add(i++);
            stationAndInds.put(station.getStationId(), stationInts);
        }

    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getLineId() {
        return lineId;
    }

    public GraphType getGraphType() {
        return graphType;
    }

    public List<PTStop> getAllStations() {
        return stations;
    }

    public List<PTStop> getNextStations(String stationId) {
        return getNextStationsInner(stationId, stations);
    }

    private List<PTStop> getNextStationsInner(String stationId, List<PTStop> stations) {
        List<PTStop> pts = new ArrayList<PTStop>();

        for (Integer indForStationId : stationAndInds.get(stationId)) {
            int indOfNextStation = indForStationId + 1;
            if (indOfNextStation < stations.size()) {
                pts.add(stations.get(indOfNextStation));
            }

        }

        return pts;
    }

    @Override
    public String toString() {
        return lineId + ", " + routeId + " (" + graphType + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lineId == null) ? 0 : lineId.hashCode());
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
        PublicTransportLine other = (PublicTransportLine) obj;
        if (lineId == null) {
            if (other.lineId != null)
                return false;
        } else if (!lineId.equals(other.lineId))
            return false;
        return true;
    }
}
