package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import java.io.Serializable;

/**
 * Lane´s direction
 *
 * @author Zdenek Bousa
 * @see <a href="http://wiki.openstreetmap.org/wiki/Key:turn">Lanes:trun on OSM wiki</a>
 */
public enum LaneTurnDirection implements Serializable {
    slight_left, left, sharp_left, through, slight_right, right, sharp_right, reverse, merge_to_left, merge_to_right, none
}

