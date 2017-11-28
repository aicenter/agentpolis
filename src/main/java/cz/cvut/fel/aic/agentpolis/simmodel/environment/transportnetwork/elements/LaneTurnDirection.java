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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Lane´s direction
 *
 * @author Zdenek Bousa
 * @see <a href="http://wiki.openstreetmap.org/wiki/Key:turn">Lanes:trun on OSM wiki</a>
 * <p>
 * Enum "unknown" stands for all possible directions.
 */
public enum LaneTurnDirection implements Serializable {
    slight_left, left, sharp_left, through, slight_right, right, sharp_right, reverse, merge_to_left, merge_to_right, unknown;

    private static Set<String> values = new HashSet<>();

    static {
        for (LaneTurnDirection direction : LaneTurnDirection.values()) {
            values.add(direction.name());
        }
    }

    public static boolean contains(String key) {
        return values.contains(key);
    }

    public static LaneTurnDirection getEnumForKey(String key) {
        return valueOf(key);
    }
}

