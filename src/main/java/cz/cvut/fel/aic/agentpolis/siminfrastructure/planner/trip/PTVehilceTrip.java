/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
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

import org.apache.commons.lang.NotImplementedException;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;

/**
 * 
 * The public transport locations representation wrapping needed the information for
 driver to be able to execute his/her plan
 * 
 * @author Zbynek Moler
 * 
 */
public class PTVehilceTrip extends GraphTrip<DepartureTripItem> {

    /**
     * 
     */
    private static final long serialVersionUID = -5087844407678432807L;

    public PTVehilceTrip(LinkedList<DepartureTripItem> trip, GraphType graphType){
        super(trip, graphType);
    }

    @Override
    public void visit(TripVisitior tripVisitior) {
        throw new NotImplementedException();

    }

    @Override
    public PTVehilceTrip clone() {
        LinkedList<DepartureTripItem> clonedTrip = new LinkedList<DepartureTripItem>();
        DepartureTripItemCloneVisotr cloneVisotr = new DepartureTripItemCloneVisotr();

        for (DepartureTripItem tripItem : locations) {
            tripItem.visitDepartureTripItem(cloneVisotr);
            clonedTrip.addLast(cloneVisotr.departureTripItemClone);

        }
		return new PTVehilceTrip(clonedTrip, graphType);
    }

    private static class DepartureTripItemCloneVisotr implements DepartureTripItemVisitor {

        public DepartureTripItem departureTripItemClone;

        @Override
        public void visitDepartureTripItem(NotWaitingDepartureTripItem notWaitingDepartureTripItem) {
            departureTripItemClone = new NotWaitingDepartureTripItem(
                    notWaitingDepartureTripItem.tripPositionByNodeId);

        }

        @Override
        public void visitDepartureTripItem(WaitingDepartureTripItem waitingDepartureTripItem) {
            departureTripItemClone = new WaitingDepartureTripItem(
                    waitingDepartureTripItem.tripPositionByNodeId,
                    waitingDepartureTripItem.departureTime, waitingDepartureTripItem.overMidnight);
        }

    }

}
