/*
 * Copyright (c) 2021 Czech Technical University in Prague.
 *
 * This file is part of Agentpolis project.
 * (see https://github.com/aicenter/agentpolis).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.fel.aic.agentpolis.siminfrastructure.time;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;

import java.time.*;
import java.time.temporal.ChronoUnit;
import org.slf4j.LoggerFactory;

/**
 * This class provides a simulation time from simulation model and includes method for working with time.
 * <p>
 * Use this class for getting a simulation time instead of using event processor
 *
 * @author Zbynek Moler
 */
@Singleton
public class StandardTimeProvider implements TimeProvider{

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(StandardTimeProvider.class);

	private final static long HOUR_24 = Duration.ofHours(24).toMillis();
	private final static long NEXT_DAY_FLAG = 1;
	private final static long NO_SHIFT_TIME = 0;

	private final EventProcessor eventProcessor;
	private final EDayInWeek initDayInWeek;

	/**
	 * The date when the simulation was started.
	 */
	private final ZonedDateTime initDateTime;

	@Override
	public ZonedDateTime getInitDateTime() {
		return initDateTime;
	}

	/**
	 * The default date will be set to MONDAY {@code 2014-09-08}.
	 *
	 * @param eventProcessor
	 */
	@Inject
	public StandardTimeProvider(EventProcessor eventProcessor) {
		this(eventProcessor, LocalDate.parse("2014-09-08"));
		LOGGER.warn("Init date was set to default ({}). "
						+ "Some features may not work properly - e.g. Public transport", initDateTime);
	}

	public StandardTimeProvider(EventProcessor eventProcessor, LocalDate initDate) {
		this(eventProcessor, initDate.atStartOfDay(ZoneId.systemDefault()));
	}

	public StandardTimeProvider(EventProcessor eventProcessor, LocalDateTime initDateTime) {
		this(eventProcessor, ZonedDateTime.of(initDateTime, ZoneId.systemDefault()));
	}

	public StandardTimeProvider(EventProcessor eventProcessor, ZonedDateTime initDate) {
		super();
		this.eventProcessor = eventProcessor;
//		if (initDate.get(ChronoField.MILLI_OF_DAY) != 0) {
//			LOGGER.warn("Init day is not day start (00:00). It will be set to the start of the day.");
//			initDate = initDate.with(ChronoField.MILLI_OF_DAY, 0);
//		}
		this.initDateTime = initDate;
		this.initDayInWeek = EDayInWeek.getDayInWeekBasedOnIndex(initDate.getDayOfWeek().ordinal());
	}

	public long getCurrentDayFlag() {
		return getCurrentSimTime() / HOUR_24;
	}

	public long computeWaitingTimeToStartNextDay() {
		return (HOUR_24 * (getCurrentDayFlag() + NEXT_DAY_FLAG)) - getCurrentSimTime();
	}

	public long computeWaitingTime(long startDayFlag, long waitToTimeInDayRange) {

		long currentSimTime = getCurrentSimTime();
		long dayFlag = getCurrentDayFlag();

		return computeWaitingTime(startDayFlag, dayFlag, currentSimTime, waitToTimeInDayRange);
	}

	public long computeWaitingTimeOverMidnight(long startDayFlag, long waitToTimeInDayRange) {

		long currentSimTime = getCurrentSimTime();
		long dayFlag = getCurrentDayFlag();

		long waitingTime = computeWaitingTime(startDayFlag, dayFlag, currentSimTime, waitToTimeInDayRange);
		if (startDayFlag == dayFlag) {
			waitingTime = ((HOUR_24 * (dayFlag + NEXT_DAY_FLAG)) + waitToTimeInDayRange) - currentSimTime;
		}

		return waitingTime;
	}

	private long computeWaitingTime(long startDayFlag, long dayFlag, long currentSimTime, long waitToTimeInDayRange) {

		if (startDayFlag < 0) {
			throw new IllegalArgumentException("startDayFlag is negative");
		}
		if (waitToTimeInDayRange < 0 || waitToTimeInDayRange >= HOUR_24) {
			throw new IllegalArgumentException("Departure time is out of day range");
		}

		long waitingTime = ((HOUR_24 * dayFlag) + waitToTimeInDayRange) - currentSimTime;

		return waitingTime;
	}

	public long computeDepartureTime(long previousDayFlag, long departureTimeInDayRange) {

		long simTimeInDayRange = getSimTimeInDayRange();
		long currentDayFlag = getCurrentDayFlag();

		return computeDepartureTime(previousDayFlag, currentDayFlag, simTimeInDayRange, departureTimeInDayRange,
									NO_SHIFT_TIME);
	}

	public long computeDepartureTimeOverMidnight(long previousDayFlag, long departureTimeInDayRange) {

		long simTimeInDayRange = getSimTimeInDayRange();
		long currentDayFlag = getCurrentDayFlag();

		long waitTime = computeDepartureTime(previousDayFlag, currentDayFlag, simTimeInDayRange,
											 departureTimeInDayRange, HOUR_24);

		if (previousDayFlag + NEXT_DAY_FLAG == currentDayFlag) {
			waitTime = computeWaitTime(simTimeInDayRange, departureTimeInDayRange, NO_SHIFT_TIME);
		}

		return waitTime;

	}

	private long computeDepartureTime(long previousDayFlag, long currentDayFlag, long simTimeInDayRange,
									  long departureTimeInDayRange, long shiftTime) {

		assert currentDayFlag >= previousDayFlag &&
			   currentDayFlag <= previousDayFlag + NEXT_DAY_FLAG : "Day flag is not in range";
		assert previousDayFlag >= 0 : "Day flag has to be greater or equal then zero";
		assert departureTimeInDayRange >= 0 &&
			   departureTimeInDayRange < HOUR_24 : "Departure time has to be in day range";

		long waitTime = 0;

		if (previousDayFlag == currentDayFlag) {
			waitTime = computeWaitTime(simTimeInDayRange, departureTimeInDayRange, shiftTime);
		}

		return waitTime;
	}

	public long getSimTimeInDayRange() {
		return getCurrentSimTime() % HOUR_24;
	}

	private long computeWaitTime(long simTimeInDayRange, long departureTimeInDayRange, long shiftTime) {
		return departureTimeInDayRange + shiftTime - simTimeInDayRange;
	}

	@Override
	public long getCurrentSimTime() {
		return eventProcessor.getCurrentTime();
	}

	// ----------------- Day In Week ---------------------------------

	/**
	 * Provide current day in week, which is in simulation.
	 *
	 * @return
	 */
	public EDayInWeek getCurrentDayInWeek() {
		int indexOfDayInWeek = getIndexOfCurrentDayInWeek();
		return EDayInWeek.getDayInWeekBasedOnIndex(indexOfDayInWeek);

	}

	public long computeWaitTimeForDayInWeek(EDayInWeek dayInWeek) {
		int indexOfCurrentDayInWeek = getIndexOfCurrentDayInWeek();
		long waitingTimeToStartNextDay = computeWaitingTimeToStartNextDay();

		if (dayInWeek.weekDayShiftIndex > indexOfCurrentDayInWeek) {
			int daysTillStartDayInWeek = dayInWeek.weekDayShiftIndex - indexOfCurrentDayInWeek - 1;
			return (daysTillStartDayInWeek * HOUR_24) + waitingTimeToStartNextDay;

		}

		int numberDayTillEndOfWeek = EDayInWeek.numberOfDayInWeek - indexOfCurrentDayInWeek - 1;
		long timeTillEndOfWeek = (numberDayTillEndOfWeek * HOUR_24) + waitingTimeToStartNextDay;
		long timeSinceNewWeekTillStartDayInWeek = dayInWeek.weekDayShiftIndex * HOUR_24;
		return timeTillEndOfWeek + timeSinceNewWeekTillStartDayInWeek;

	}

	private int getIndexOfCurrentDayInWeek() {
		int index = (int) ((getCurrentDayFlag() + initDayInWeek.weekDayShiftIndex) % EDayInWeek.numberOfDayInWeek);
		if (index < 0 || index > 6) {
			throw new RuntimeException("The current day flag (" + getCurrentDayFlag() + ") with weekday shift (" +
									   initDayInWeek.weekDayShiftIndex + ") give day index of " + index +
									   " which is out of valid range <0,6>");
		}
		return index;
	}

	// ----------------- Date ---------------------------------

	/**
	 * It provides current date with time set to start of the day.
	 *
	 * @return
	 */
	public ZonedDateTime getCurrentDate() {
		return initDateTime.plusDays((int) getCurrentDayFlag());
	}

	@Override
	public ZonedDateTime getCurrentSimDateTime() {
		return initDateTime.plus(getCurrentSimTime(), ChronoUnit.MILLIS);
	}

	@Override
	public ZonedDateTime getDateTimeFromSimTime(long simTime) {
		return initDateTime.plus(simTime, ChronoUnit.MILLIS);
	}

	@Override
	public long getSimTimeFromDateTime(ZonedDateTime dateTime) {
		return initDateTime.until(dateTime, ChronoUnit.MILLIS);
	}

	/**
	 * Calculate number of millis between {@code date + millisInDay} and current date time.
	 *
	 * @param date
	 * @param millisInDay
	 *
	 * @return
	 */
	public long computeWaitTime(ZonedDateTime date, long millisInDay) {
		return getCurrentSimDateTime().until(date, ChronoUnit.MILLIS) + millisInDay;
	}

}
