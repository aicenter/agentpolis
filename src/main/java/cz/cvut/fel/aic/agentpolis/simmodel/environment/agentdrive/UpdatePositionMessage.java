package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.Junction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.RoadNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RadarData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RoadObject;

import javax.vecmath.Point3f;
import java.util.*;

public class UpdatePositionMessage {
    private RadarData radarData;

    public UpdatePositionMessage(RadarData radarData) {
        this.radarData = radarData;
    }

    public RadarData getRadarData() {
        return radarData;
    }

    public Map<Integer, Point3f> getCarPositions() {
        Map<Integer, Point3f> map = new HashMap<>();
        for (RoadObject ro : radarData.getCars()) {
            map.put(ro.getId(), ro.getPosition());
        }
        return map;
    }
}
