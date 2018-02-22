package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.LaneTurnDirection;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Zdenek Bousa
 */
public class PrepareDummyLanes {

    public static List<LinkedList<Lane>> getLanes(int edgeCount, int lanesCount) {
        LinkedList<LinkedList<Lane>> lanes = new LinkedList<>();
        int lanesC = 0;
        for (int i = 0; i < edgeCount; i++) {
            LinkedList<Lane> lanesInE = new LinkedList<>();
            for (int x = 0; x < lanesCount; x++) {
                Lane lane = new Lane(lanesC++, i);
                lane.addDirection(-1, LaneTurnDirection.unknown);
                lanesInE.add(lane);
            }
            lanes.add(lanesInE);
        }
        return lanes;
    }
}
