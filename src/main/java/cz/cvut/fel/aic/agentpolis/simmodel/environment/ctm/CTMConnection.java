package cz.cvut.fel.aic.agentpolis.simmodel.environment.ctm;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.Message;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CongestionMessage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.VehicleQueueData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.VehicleTripData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.ConnectionEvent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;

import java.util.*;


public class CTMConnection extends EventHandlerAdapter {

    private final SimulationNode position;
    private long deltaT = 5 * 1000;
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
            if (event.getContent() instanceof VehicleCTMEndData) {
                finishCar((VehicleCTMEndData) event.getContent());
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

    private void finishCar(VehicleCTMEndData data) {
        Segment segment = data.segment;
        VehicleQueueData car = segment.carQueue.pollCar();

        car.getVehicleTripData().getVehicle().setPosition(position);
        ((Agent) car.getVehicleTripData().getVehicle().getDriver()).processMessage(new Message(
                CongestionMessage.DRIVING_FINISHED, null));

    }

    private void update() {
        HashMap<Segment, Integer> carsToSend = new HashMap<>();
        HashMap<Segment, Integer> carsToReceive = new HashMap<>();

        inSegments.forEach(fromSegment -> {
            carsToSend.put(fromSegment, computeSupply(fromSegment));
        });
        outSegments.values().forEach(toSegment -> {
            carsToReceive.put(toSegment, computeDemand(toSegment));
        });
        int carTransfered = computeFlow(carsToSend, carsToReceive);
    }

    private void scheduleTransfers(int carsToTransfer, Segment fromSegment) {
        if (carsToTransfer > 0) {
            long currentTime = simulationProvider.getSimulation().getCurrentTime();
            long endTime = currentTime + deltaT;
            long period = endTime - currentTime;
            long headwayInMS = period / carsToTransfer;
            long firstStartTime = currentTime + headwayInMS / 2;
            Iterator carIterator = fromSegment.carQueue.carQueue.iterator();
            for (long t = firstStartTime; t < endTime && carIterator.hasNext(); t += headwayInMS) {
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

    private int computeFlow(HashMap<Segment, Integer> supply, HashMap<Segment, Integer> demand) {
        // according to connection type decide which inSegments are active(green lights),
        //schedule tranfers while demnad capacities are respected
        Map.Entry<Segment, Integer> minDemand = Collections.min(demand.entrySet(),
                Comparator.comparingInt(Map.Entry::getValue));

        for (Segment fromSegment : supply.keySet()) {
            int carsToTransfer = Math.min(supply.get(fromSegment), minDemand.getValue());
            scheduleTransfers(carsToTransfer, fromSegment);
        }
        return 0;


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
        int cars = (int) (flow * deltaT / 1000);
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
        int cars = (int) (flow * deltaT / 1000);
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
        SimulationNode nextNode = (SimulationNode) trip.getFirstLocation();
        trip.removeFirstLocation();
        if (trip.isEmpty()) {
            vehicleQueueData.getVehicleTripData().setTripFinished(true);
        }
        vehicleQueueData.getVehicleTripData().getVehicle().setPosition(position);
        long delay = 15000;
        DelayData delayData = new DelayData(delay, simulationProvider.getSimulation().getCurrentTime(), segmentToGo.length);

        Driver driver = vehicleQueueData.getVehicleTripData().getVehicle().getDriver();
        driver.setTargetNode(nextNode);
        driver.setDelayData(delayData);

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
