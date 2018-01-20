package cz.cvut.fel.aic.agentpolis.simmodel.environment.ctm;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.VehicleQueueData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.VehicleTripData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.ConnectionEvent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.VehicleEndData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CTMConnection extends EventHandlerAdapter {

    private final SimulationNode position;
    long deltaT = 10 * 1000;
    private SimulationProvider simulationProvider;
    private List<Segment> inSegments;
    private Map<SimulationNode, Segment> outSegments;

    public CTMConnection(SimulationNode position, SimulationProvider simulationProvider, List<Segment> inSegments, Map<SimulationNode, Segment> outSegments) {
        this.simulationProvider = simulationProvider;
        this.inSegments = inSegments;
        this.outSegments = outSegments;
        simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, this, null, null, deltaT);
        this.position = position;
    }


    @Override
    public void handleEvent(Event event) {
        if (event.getType().equals(ConnectionEvent.TICK)) {
            // timestep
            update();
            simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, this, null, null, deltaT);
        } else if (event.getType().equals(ConnectionEvent.SCHEDULED_EVENT)) {
            if (event.getContent() instanceof VehicleEndData) {
                finnishCar((VehicleCTMEndData) event.getContent());
            } else if (event.getContent() instanceof VehicleCTMTransferData) {
                // car transfer
                transferCar((VehicleCTMTransferData) event.getContent());
            }
        }
    }

    private void transferCar(VehicleCTMTransferData transferData) {
        Segment fromSegment = transferData.fromSegment;
        Segment toSegment = transferData.toSegment;
        VehicleQueueData car = fromSegment.carQueue.pollCar();
        addCarToNextSegment(toSegment, car);
    }

    private void finnishCar(VehicleCTMEndData data) {
        Segment segment = data.segment;
        VehicleQueueData car = segment.carQueue.pollCar();

    }

    private void update() {
        inSegments.forEach(fromSegment -> {
            int carsToTransfer = computeFlow(fromSegment);
            scheduleTransfers(carsToTransfer, fromSegment);
        });
    }

    private void scheduleTransfers(int carsToTransfer, Segment fromSegment) {
        long currentTime = simulationProvider.getSimulation().getCurrentTime();
        long firstStartTime = currentTime + 1;
        long endTime = currentTime + deltaT;
        double period = endTime - firstStartTime;
        double headwayInS = period / carsToTransfer;
        Iterator carIterator = fromSegment.carQueue.carQueue.iterator();
        for (long t = firstStartTime; t < endTime && carIterator.hasNext(); t += headwayInS) {
            VehicleQueueData car = (VehicleQueueData) carIterator.next();
            VehicleTripData tripData = car.getVehicleTripData();
            if (tripData.isTripFinished()) {
                scheduleFinish(fromSegment, t);
            } else {
                Segment toSegment = getNextSegment(tripData);
                scheduleTransfer(fromSegment, toSegment, t);
            }
        }
    }

    private void scheduleFinish(Segment fromSegment, long t) {
        long currentTime = simulationProvider.getSimulation().getCurrentTime();
        VehicleCTMEndData content = new VehicleCTMEndData(fromSegment, t);
        long delay = t - currentTime;
        simulationProvider.getSimulation().addEvent(ConnectionEvent.SCHEDULED_EVENT, this, null, content, delay != 0 ? delay : 1);

    }

    private void scheduleTransfer(Segment fromSegment, Segment toSegment, long t) {
        long currentTime = simulationProvider.getSimulation().getCurrentTime();
        if (fromSegment == null || toSegment == null || t <= currentTime) {
            Log.error(this, "Cannot schedule transfer from {0} to {1} at {2}", fromSegment, toSegment, t);
            System.exit(1);
        }
        VehicleCTMTransferData content = new VehicleCTMTransferData(fromSegment, toSegment, t);
        long delay = t - currentTime;
        simulationProvider.getSimulation().addEvent(ConnectionEvent.SCHEDULED_EVENT, this, null, content, delay != 0 ? delay : 1);
    }

    private int computeFlow(Segment fromSegment) {
        if (outSegments.size() == 1) {
            Segment toSegment = outSegments.values().iterator().next();
            int supply = computeSupply(fromSegment);
            int demand = computeDemand(toSegment);
            return Math.min(supply, demand);
        } else {
            return 0;
        }
    }

    private int computeDemand(Segment segment) {
        double density = segment.carQueue.carQueue.size() / segment.length;
        double criticalFlow = segment.criticalDensity * segment.v;
        double jamDensity = segment.criticalDensity + criticalFlow / segment.w;
        double flow = 0;
        if (density < segment.criticalDensity) {
            flow = criticalFlow;
        } else {
            flow = Math.max(0, -segment.w * (density - jamDensity));
        }
        int cars = (int) (flow * deltaT/1000);
        return cars;

    }

    private int computeSupply(Segment segment) {
        double density = segment.carQueue.carQueue.size() / segment.length;
        double criticalFlow = segment.criticalDensity * segment.v;
        double flow = 0;
        if (density < segment.criticalDensity) {
            flow = segment.v * density;
        } else {
            flow = criticalFlow;
        }
        int cars = (int) (flow * deltaT/1000);
        return cars;
    }

    public void startDriving(VehicleTripData vehicleData) {
        Segment segmentToGo = getNextSegment(vehicleData);
        long currentTime = simulationProvider.getSimulation().getCurrentTime();
        VehicleQueueData vehicleQueueData = new VehicleQueueData(vehicleData, currentTime);
        addCarToNextSegment(segmentToGo, vehicleQueueData);
    }

    private void addCarToNextSegment(Segment segmentToGo, VehicleQueueData vehicleQueueData) {
        segmentToGo.carQueue.insertCar(vehicleQueueData);
        Trip trip = vehicleQueueData.getVehicleTripData().getTrip();
        trip.removeFirstLocation();
        if (trip.isEmpty()) {
            vehicleQueueData.getVehicleTripData().setTripFinished(true);
        }
        vehicleQueueData.getVehicleTripData().getVehicle().setPosition(position);

    }

    private Segment getNextSegment(VehicleTripData vehicleData) {
        Trip<SimulationNode> trip = vehicleData.getTrip();
        SimulationNode nextNode = trip.getFirstLocation();
        if (!outSegments.containsKey(nextNode)) {
            Log.error(this, "Inconsistent trip(nextNode={0}) with the current location(outSegment={1})!", nextNode, outSegments);
            System.exit(1);
        }
        return outSegments.get(nextNode);
    }
}
