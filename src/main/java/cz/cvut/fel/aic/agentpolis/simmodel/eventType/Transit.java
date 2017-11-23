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
package cz.cvut.fel.aic.agentpolis.simmodel.eventType;

/**
 *
 * @author fido
 */
public class Transit {
    private final long time;
    
    private final long osmId;
    
    private final int tripId;

    public long getTime() {
        return time;
    }

    public long getId() {
        return osmId;
    }

    public int getTripId() {
        return tripId;
    }
    
    

    
    
    public Transit(long time, long osmId, int tripId) {
        this.time = time;
        this.osmId = osmId;
        this.tripId = tripId;
    }


}
