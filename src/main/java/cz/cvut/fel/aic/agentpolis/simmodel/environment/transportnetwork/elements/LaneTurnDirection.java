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

