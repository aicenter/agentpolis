/* 
 * Copyright (C) 2017 fido.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip;

import java.util.LinkedList;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;

/**
 * 
 * The public transport locations representation wrapping needed the information for
 passenger to be able to execute his/her plan
 * 
 * @author Zbynek Moler
 * 
 */
public class PTTrip extends GraphTrip<TripItem> {

    /**
     * 
     */
    private static final long serialVersionUID = 4585012422536028959L;
    
    private final String lineId; // lineId = groupId
    private final LinkedList<String> stations;

    public PTTrip(LinkedList<TripItem> trip, GraphType graphType, String lineId,
            LinkedList<String> stations) {
        super(trip, graphType);

        this.lineId = lineId;
        this.stations = stations;
    }

    public String getLineId() {
        return lineId;
    }

    public LinkedList<String> getStations() {
        return stations;
    }

    public String showCurrentStationId() {
        return stations.get(stations.size() - locations.size());
    }

    public void addTripItemBeforeCurrentFirstAndChangeStation(String stationId, TripItem tripItem) {
        addTripItemBeforeCurrentFirst(tripItem);
        stations.set(stations.size() - locations.size(), stationId);
    }

    @Override
    public PTTrip clone() {
        LinkedList<TripItem> clonedTrip = new LinkedList<TripItem>();
        for (TripItem tripItem : locations) {
            clonedTrip.addLast(new TripItem(tripItem.tripPositionByNodeId));
        }

        LinkedList<String> stationsClone = new LinkedList<String>();
        for (String stationId : stations) {
            stationsClone.add(stationId);
        }

		return new PTTrip(clonedTrip, graphType, lineId, stationsClone);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("PTTrip: ");
        stringBuilder.append(lineId);
        stringBuilder.append(" -> ");
        stringBuilder.append(super.toString());
        return stringBuilder.toString();
    }

    @Override
    public void visit(TripVisitior tripVisitior) {
        tripVisitior.visitTrip(this);

    }

}
