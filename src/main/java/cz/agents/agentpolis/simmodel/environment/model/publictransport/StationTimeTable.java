package cz.agents.agentpolis.simmodel.environment.model.publictransport;

import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.TreeSet;

/**
 * Immutable timetable for station, is related to line id (bus 45 from A to B, bus 45 from B to A, tram 16 from C to D,
 * ...).
 * <p>
 * Contains only leave times.
 *
 * @author Ondrej Milenovsky
 */
public class StationTimeTable implements Serializable {

	private static final long serialVersionUID = 7066939670697260573L;

	private static final long DAY_IN_MILLIS = Duration.ofDays(1).toMillis();

	private final TreeSet<Long> leaveTimesInMillis;

	/**
	 * constructor created tree set and copies all items from input to it, accepts longs and durations
	 */
	public StationTimeTable(Collection<Duration> leaveTimes) {
		this.leaveTimesInMillis = new TreeSet<>();
		for (Duration departureTime : leaveTimes) {
			addTime((departureTime).toMillis());
		}
	}

	public StationTimeTable() {
		this.leaveTimesInMillis = new TreeSet<>();
	}

	public void addDepartureTime(Duration departureTimeInDayRange) {
		leaveTimesInMillis.add(departureTimeInDayRange.toMillis());
	}

	private void addTime(long time) {
		long day = DAY_IN_MILLIS;
		while (time >= day) {
			time -= day;
		}
		while (time < 0) {
			time += day;
		}
		leaveTimesInMillis.add(time);
	}

	public long getNextLeaveTime(long time) {
		long timeInDayRange = time % DAY_IN_MILLIS;
		long overDayRange = time - timeInDayRange;

		Long leaveTime = leaveTimesInMillis.ceiling(timeInDayRange);

		if (leaveTime == null) {
			leaveTime = leaveTimesInMillis.first() + DAY_IN_MILLIS;
		}

		return overDayRange + leaveTime;

	}

	public long getNearestLeaveTime(long time) {
		long timeInDayRange = time % DAY_IN_MILLIS;
		long overDayRange = time - timeInDayRange;

		Long leaveTime = leaveTimesInMillis.ceiling(timeInDayRange);

		if (leaveTime == null) {
			leaveTime = leaveTimesInMillis.first();
		}

		return overDayRange + leaveTime;
	}

}
