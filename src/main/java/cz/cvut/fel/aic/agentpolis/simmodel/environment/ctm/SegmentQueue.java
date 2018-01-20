package cz.cvut.fel.aic.agentpolis.simmodel.environment.ctm;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.VehicleQueueData;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by martin on 1/18/18.
 */
public class SegmentQueue {
    Queue<VehicleQueueData> carQueue = new LinkedList<>();

    public VehicleQueueData pollCar() {
        return carQueue.poll();
    }

    public void insertCar(VehicleQueueData car) {
        carQueue.offer(car);
    }
}
