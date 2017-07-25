package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.publictransport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import java.time.ZonedDateTime;

/**
 * 
 * The representation of time table for a particular vehicle. It is an itinerary
 * for a vehicle
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleStationTimetable implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2629926084752411981L;

    /**
     * Indexed list of expected arrival times, in the order the vehicle visits
     * them.
     */
    private final List<TimetableItem> arriveTimesPerStations = new ArrayList<>();

    /**
     * Indexed list of expected departure times, in the order the vehicle visits
     * them.
     */
    private final List<TimetableItem> departureTimesPerStations = new ArrayList<>();

    /** List of station IDs in the order the vehicle visits them. */
    private final List<String> sequenceStationIds = new ArrayList<>();

    /**
     * Map from station IDs to sequence numbers, useful for searching for when
     * the vehicle visits the station.
     * 
     * FIXME: one station can be in the timetable more than once
     */
    @Deprecated
    private final Map<String, Integer> stationIdsToSequenceNumbers = new HashMap<>();

    private final int starSequenceStationId;
    private int lastSequenceStationId;

    private final List<ZonedDateTime> validDays;
    private final String tripId;

    public VehicleStationTimetable(int startSequenceStationId, List<ZonedDateTime> validDays,
            String tripId) {
        super();
        this.starSequenceStationId = startSequenceStationId;
        lastSequenceStationId = startSequenceStationId;
        this.validDays = validDays;
        this.tripId = tripId;

        initEmptyValue(startSequenceStationId, arriveTimesPerStations);
        initEmptyValue(startSequenceStationId, departureTimesPerStations);
        initEmptyValue(startSequenceStationId, sequenceStationIds);

    }

    private <T> void initEmptyValue(int starSequenceStationId, List<T> list) {
        for (int i = 0; i <= starSequenceStationId; i++) {
            list.add(null);
        }
    }

    public void addStop(int sequenceStationId, String stationId, TimetableItem arrival,
            TimetableItem departure) {
        setLastSequenceStationId(sequenceStationId);
        sequenceStationIds.add(sequenceStationId, stationId);
        stationIdsToSequenceNumbers.put(stationId, sequenceStationId);
        arriveTimesPerStations.add(sequenceStationId, arrival);
        departureTimesPerStations.add(sequenceStationId, departure);
    }

    public TimetableItem getArriveTime(int sequenceStationId) {
        return arriveTimesPerStations.get(sequenceStationId);
    }

    public TimetableItem getDepartureTime(int sequenceStationId) {
        return departureTimesPerStations.get(sequenceStationId);
    }

    public int getStartSequenceStationId() {
        return starSequenceStationId;
    }

    public int getLastSequenceStationId() {
        return lastSequenceStationId;
    }

    public List<ZonedDateTime> getValidDays() {
        return validDays;
    }

    public String getTripId() {
        return tripId;
    }

    public Iterator<String> getStationIdBasedOnSequenceStationIdIterator() {
        final LinkedList<String> stationsByIds = new LinkedList<>();

        for (int i = starSequenceStationId; i <= lastSequenceStationId; i++) {
            stationsByIds.add(sequenceStationIds.get(i));
        }

        return new Iterator<String>() {

            @Override
            public void remove() {
                stationsByIds.removeFirst();
            }

            @Override
            public String next() {
                return stationsByIds.pollFirst();
            }

            @Override
            public boolean hasNext() {
                return !stationsByIds.isEmpty();
            }
        };

    }

    public String getStationIdBaseOnSequenceStationId(int sequenceStationId) {
        return sequenceStationIds.get(sequenceStationId);
    }

    /**
     * 
     * @deprecated station can be in the schedule more than once.
     * 
     * @param stationId
     * @return
     */
    @Deprecated
    public int getStationSequenceNumber(String stationId) {
        Integer result = stationIdsToSequenceNumbers.get(stationId);
        if (result == null)
            throw new NoSuchElementException("station " + stationId + " not found in this table");
        return result;
    }

    private void setLastSequenceStationId(int sequenceStationId) {
        if (sequenceStationId > lastSequenceStationId) {
            lastSequenceStationId = sequenceStationId;
        }
    }

    private static final long HOUR_24_IN_MS = 86400000;

    /**
     * Avg. duration - vehicle can move across stations more times.
     * 
     * @return
     */
    public long getAvgDurationBetweenStationsByNodeId(String fromStationId, String toStationId) {
        long numberOfIterations = 0;
        long durationTime = 0;

        for (int i = starSequenceStationId; i <= lastSequenceStationId; i++) {
            String fromStationIdTmp = sequenceStationIds.get(i);
            for (int j = starSequenceStationId + 1; j <= lastSequenceStationId; j++) {
                String toStationIdTmp = sequenceStationIds.get(j);
                if (fromStationId.equals(fromStationIdTmp) && toStationId.equals(toStationIdTmp)) {
                    TimetableItem departureTimes = departureTimesPerStations.get(i);
                    TimetableItem arriveTimesPer = arriveTimesPerStations.get(j);

                    if ((departureTimes.overMidnight == false && arriveTimesPer.overMidnight == false)
                            || (departureTimes.overMidnight && arriveTimesPer.overMidnight)) {
                        durationTime = arriveTimesPer.time - departureTimes.time;
                        numberOfIterations++;
                    }

                    if ((departureTimes.overMidnight == false && arriveTimesPer.overMidnight)) {
                        durationTime = (HOUR_24_IN_MS - departureTimes.time) + arriveTimesPer.time;
                        numberOfIterations++;
                    }

                    if ((departureTimes.overMidnight && arriveTimesPer.overMidnight == false)) {
                        throw new RuntimeException(
                                "It is not possible departure time midnight and arrive time over not");
                    }

                }
            }
        }

        return (durationTime / numberOfIterations);
    }
}
