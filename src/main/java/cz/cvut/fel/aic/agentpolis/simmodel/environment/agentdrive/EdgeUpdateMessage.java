package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.Junction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.RoadNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RadarData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RoadObject;

import java.util.HashMap;
import java.util.Map;

public class EdgeUpdateMessage {

    private Map<Integer, Long> lastJunctions = new HashMap<>();

    public EdgeUpdateMessage(RadarData radarData, RoadNetwork roadNetwork) {
        for (RoadObject ro : radarData.getCars()) {
            Junction j = roadNetwork.getJunctions().get(roadNetwork.getActualPosition(ro.getPosition()).getEdge().getFrom());
            lastJunctions.put(ro.getId(), j.getAgentpolsId());
        }

    }

    public Map<Integer, Long> getLastJunctions() {
        return lastJunctions;
    }
}
