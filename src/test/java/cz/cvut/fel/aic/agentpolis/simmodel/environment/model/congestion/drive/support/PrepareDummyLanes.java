package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.LaneTurnDirection;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Zdenek Bousa
 */
public class PrepareDummyLanes {
    public static List<LinkedList<Lane>> getLanesOne() {
        //Lanes
        LinkedList<Lane> lanes1 = new LinkedList<>();
        Lane lane1 = new Lane(0, 0);
        lane1.addDirection(-1, LaneTurnDirection.unknown);
        lanes1.add(lane1);

        LinkedList<Lane> lanes2 = new LinkedList<>();
        Lane lane2 = new Lane(2, 1);
        lane2.addDirection(-1, LaneTurnDirection.unknown);
        lanes2.add(lane2);

        LinkedList<Lane> lanes3 = new LinkedList<>();
        Lane lane3 = new Lane(4, 2);
        lane3.addDirection(-1, LaneTurnDirection.unknown);
        lanes3.add(lane3);

        LinkedList<Lane> lanes4 = new LinkedList<>();
        Lane lane4 = new Lane(6, 3);
        lane4.addDirection(-1, LaneTurnDirection.unknown);
        lanes4.add(lane4);

        LinkedList<Lane> lanes5 = new LinkedList<>();
        Lane lane5 = new Lane(8, 4);
        lane5.addDirection(-1, LaneTurnDirection.unknown);
        lanes5.add(lane5);

        LinkedList<Lane> lanes6 = new LinkedList<>();
        Lane lane6 = new Lane(10, 5);
        lane6.addDirection(-1, LaneTurnDirection.unknown);
        lanes6.add(lane6);

        List<LinkedList<Lane>> laneList = new LinkedList<>();
        laneList.add(lanes1);
        laneList.add(lanes2);
        laneList.add(lanes3);
        laneList.add(lanes4);
        laneList.add(lanes5);
        laneList.add(lanes6);
        return laneList;
    }

    public static List<LinkedList<Lane>> getLanesTwo() {
        //Lanes
        LinkedList<Lane> lanes1 = new LinkedList<>();
        Lane lane1 = new Lane(0, 0);
        lane1.addDirection(-1, LaneTurnDirection.unknown);
        lanes1.add(lane1);
        Lane lane12 = new Lane(1, 0);
        lane12.addDirection(-1, LaneTurnDirection.unknown);
        lanes1.add(lane12);

        LinkedList<Lane> lanes2 = new LinkedList<>();
        Lane lane2 = new Lane(2, 1);
        lane2.addDirection(-1, LaneTurnDirection.unknown);
        lanes2.add(lane2);
        Lane lane21 = new Lane(3, 1);
        lane21.addDirection(-1, LaneTurnDirection.unknown);
        lanes2.add(lane21);


        LinkedList<Lane> lanes3 = new LinkedList<>();
        Lane lane3 = new Lane(4, 2);
        lane3.addDirection(-1, LaneTurnDirection.unknown);
        lanes3.add(lane3);
        Lane lane31 = new Lane(5, 2);
        lane31.addDirection(-1, LaneTurnDirection.unknown);
        lanes3.add(lane31);


        LinkedList<Lane> lanes4 = new LinkedList<>();
        Lane lane4 = new Lane(6, 3);
        lane4.addDirection(-1, LaneTurnDirection.unknown);
        lanes4.add(lane4);
        Lane lane41 = new Lane(7, 3);
        lane41.addDirection(-1, LaneTurnDirection.unknown);
        lanes4.add(lane41);

        LinkedList<Lane> lanes5 = new LinkedList<>();
        Lane lane5 = new Lane(8, 4);
        lane5.addDirection(-1, LaneTurnDirection.unknown);
        lanes5.add(lane5);
        Lane lane51 = new Lane(9, 4);
        lane51.addDirection(-1, LaneTurnDirection.unknown);
        lanes5.add(lane51);

        LinkedList<Lane> lanes6 = new LinkedList<>();
        Lane lane6 = new Lane(10, 5);
        lane6.addDirection(-1, LaneTurnDirection.unknown);
        lanes6.add(lane6);
        Lane lane61 = new Lane(11, 5);
        lane61.addDirection(-1, LaneTurnDirection.unknown);
        lanes6.add(lane61);

        List<LinkedList<Lane>> laneList = new LinkedList<>();
        laneList.add(lanes1);
        laneList.add(lanes2);
        laneList.add(lanes3);
        laneList.add(lanes4);
        laneList.add(lanes5);
        laneList.add(lanes6);
        return laneList;
    }
}
