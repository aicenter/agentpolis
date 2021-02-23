/* 
 * Copyright (C) 2019 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.siminfrastructure.util;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.EDayInWeek;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;
import java.time.*;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TimeEndUtilTest {

	private EventProcessor eventProcessor;
	private StandardTimeProvider timeEnvUtil;

	@Before
	public void setUp() {
		eventProcessor = mock(EventProcessor.class);
		timeEnvUtil = new StandardTimeProvider(eventProcessor);
	}

	@Test
	public void timeEnvUtilTest() {

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(23).toMillis());
		assertEquals(0, timeEnvUtil.getCurrentDayFlag());

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(24).toMillis());
		assertEquals(1, timeEnvUtil.getCurrentDayFlag());

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(25).toMillis());
		assertEquals(1, timeEnvUtil.getCurrentDayFlag());

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(47).toMillis());
		assertEquals(1, timeEnvUtil.getCurrentDayFlag());

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(48).toMillis());
		assertEquals(2, timeEnvUtil.getCurrentDayFlag());

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(49).toMillis());
		assertEquals(2, timeEnvUtil.getCurrentDayFlag());

	}

	@Test
	public void timeEnvUtilTest1() {

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(1).toMillis());
		assertEquals(Duration.ofHours(23).toMillis(), timeEnvUtil.computeWaitingTimeToStartNextDay());

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(49).toMillis());
		assertEquals(Duration.ofHours(23).toMillis(), timeEnvUtil.computeWaitingTimeToStartNextDay());
	}

	@Test
	public void timeEnvUtilTest2() {

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(23).toMillis());
		assertEquals(Duration.ofHours(1).toMillis(), timeEnvUtil.computeWaitingTimeToStartNextDay());

	}

	@Test
	public void timeEnvUtilTest3() {

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(20).toMillis());
		long previousDayFlag = timeEnvUtil.getCurrentDayFlag();
		long departureTimeInDayRange = Duration.ofHours(22).toMillis();

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(21).toMillis());
		long waitTime = timeEnvUtil.computeDepartureTime(previousDayFlag, departureTimeInDayRange);

		assertEquals(Duration.ofHours(1).toMillis(), waitTime);

	}

	@Test
	public void timeEnvUtilTest4() {

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(20).toMillis());
		long previousDayFlag = timeEnvUtil.getCurrentDayFlag();
		long departureTimeInDayRange = Duration.ofHours(21).toMillis();

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(22).toMillis());
		long waitTime = timeEnvUtil.computeDepartureTime(previousDayFlag, departureTimeInDayRange);

		assertEquals(-Duration.ofHours(1).toMillis(), waitTime);

	}

	@Test
	public void timeEnvUtilTest5() {

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(20).toMillis());
		long previousDayFlag = timeEnvUtil.getCurrentDayFlag();
		long departureTimeInDayRange = Duration.ofHours(22).toMillis();

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(21 + 24).toMillis());
		long waitTime = timeEnvUtil.computeDepartureTime(previousDayFlag, departureTimeInDayRange);

		assertEquals(0, waitTime);

	}

	@Test
	public void timeEnvUtilTest6() {
		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(20).toMillis());
		long previousDayFlag = timeEnvUtil.getCurrentDayFlag();
		long departureTimeInDayRange = Duration.ofHours(1).toMillis();

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(21).toMillis());
		long waitTime = timeEnvUtil.computeDepartureTimeOverMidnight(previousDayFlag, departureTimeInDayRange);

		assertEquals(Duration.ofHours(4).toMillis(), waitTime);

	}

	@Test
	public void timeEnvUtilTest7() {
		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(20).toMillis());
		long previousDayFlag = timeEnvUtil.getCurrentDayFlag();
		long departureTimeInDayRange = Duration.ofHours(2).toMillis();

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(1 + 24).toMillis());
		long waitTime = timeEnvUtil.computeDepartureTimeOverMidnight(previousDayFlag, departureTimeInDayRange);

		assertEquals(Duration.ofHours(1).toMillis(), waitTime);

	}

	@Test
	public void timeEnvUtilTest8() {
		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(20).toMillis());
		long previousDayFlag = timeEnvUtil.getCurrentDayFlag();
		long departureTimeInDayRange = Duration.ofHours(1).toMillis();

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(22 + 24).toMillis());
		long waitTime = timeEnvUtil.computeDepartureTimeOverMidnight(previousDayFlag, departureTimeInDayRange);

		assertEquals(-Duration.ofHours(21).toMillis(), waitTime);

	}

	// --------

	public void timeEnvUtilTest9() {

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(20).toMillis());
		long previousDayFlag = timeEnvUtil.getCurrentDayFlag();
		long departureTimeInDayRange = Duration.ofHours(22).toMillis();

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(21).toMillis());
		long waitTime = timeEnvUtil.computeWaitingTime(previousDayFlag, departureTimeInDayRange);

		assertEquals(Duration.ofHours(1).toMillis(), waitTime);

	}

	@Test
	public void timeEnvUtilTest10() {

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(20).toMillis());
		long previousDayFlag = timeEnvUtil.getCurrentDayFlag();
		long departureTimeInDayRange = Duration.ofHours(21).toMillis();

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(22).toMillis());
		long waitTime = timeEnvUtil.computeWaitingTime(previousDayFlag, departureTimeInDayRange);

		assertEquals(-Duration.ofHours(1).toMillis(), waitTime);

	}

	@Test
	public void timeEnvUtilTest11() {

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(20).toMillis());
		long previousDayFlag = timeEnvUtil.getCurrentDayFlag();
		long departureTimeInDayRange = Duration.ofHours(22).toMillis();

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(21 + 24).toMillis());
		long waitTime = timeEnvUtil.computeWaitingTime(previousDayFlag, departureTimeInDayRange);

		assertEquals(Duration.ofHours(1).toMillis(), waitTime);

	}

	@Test
	public void timeEnvUtilTest12() {
		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(20).toMillis());
		long previousDayFlag = timeEnvUtil.getCurrentDayFlag();
		long departureTimeInDayRange = Duration.ofHours(1).toMillis();

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(21).toMillis());
		long waitTime = timeEnvUtil.computeWaitingTimeOverMidnight(previousDayFlag, departureTimeInDayRange);

		assertEquals(Duration.ofHours(4).toMillis(), waitTime);

	}

	@Test
	public void timeEnvUtilTest13() {
		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(20).toMillis());
		long previousDayFlag = timeEnvUtil.getCurrentDayFlag();
		long departureTimeInDayRange = Duration.ofHours(2).toMillis();

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(1 + 24).toMillis());
		long waitTime = timeEnvUtil.computeWaitingTimeOverMidnight(previousDayFlag, departureTimeInDayRange);

		assertEquals(Duration.ofHours(1).toMillis(), waitTime);

	}

	@Test
	public void timeEnvUtilTest14() {
		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(20).toMillis());
		long previousDayFlag = timeEnvUtil.getCurrentDayFlag();
		long departureTimeInDayRange = Duration.ofHours(1).toMillis();

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(22 + 24).toMillis());
		long waitTime = timeEnvUtil.computeWaitingTimeOverMidnight(previousDayFlag, departureTimeInDayRange);

		assertEquals(-Duration.ofHours(21).toMillis(), waitTime);

	}

	@Test
	public void timeEnvUtilTest15() {
		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(20).toMillis());
		assertEquals(EDayInWeek.MONDAY, timeEnvUtil.getCurrentDayInWeek());
		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofDays(6).toMillis());
		assertEquals(EDayInWeek.SUNDAY, timeEnvUtil.getCurrentDayInWeek());
		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofDays(7).toMillis());
		assertEquals(EDayInWeek.MONDAY, timeEnvUtil.getCurrentDayInWeek());

	}

	@Test
	public void timeEnvUtilTest16() {
		timeEnvUtil = new StandardTimeProvider(eventProcessor, parse("2014-09-10"));

		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofHours(20).toMillis());
		assertEquals(EDayInWeek.WEDNESDAY, timeEnvUtil.getCurrentDayInWeek());
		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofDays(6).toMillis());
		assertEquals(EDayInWeek.TUESDAY, timeEnvUtil.getCurrentDayInWeek());
		when(eventProcessor.getCurrentTime()).thenReturn(Duration.ofDays(7).toMillis());
		assertEquals(EDayInWeek.WEDNESDAY, timeEnvUtil.getCurrentDayInWeek());

	}

	@Test
	public void timeEnvUtilTest17() {

		timeEnvUtil = new StandardTimeProvider(eventProcessor, parse("2014-09-10"));
		when(eventProcessor.getCurrentTime()).thenReturn(
				Duration.ofDays(7).toMillis() + Duration.ofHours(12).toMillis());

		assertEquals(4 * 86400000 + 43200000, timeEnvUtil.computeWaitTimeForDayInWeek(EDayInWeek.MONDAY));
		assertEquals(5 * 86400000 + 43200000, timeEnvUtil.computeWaitTimeForDayInWeek(EDayInWeek.TUESDAY));
		assertEquals(6 * 86400000 + 43200000, timeEnvUtil.computeWaitTimeForDayInWeek(EDayInWeek.WEDNESDAY));
		assertEquals(43200000, timeEnvUtil.computeWaitTimeForDayInWeek(EDayInWeek.THURSDAY));
		assertEquals(1 * 86400000 + 43200000, timeEnvUtil.computeWaitTimeForDayInWeek(EDayInWeek.FRIDAY));
		assertEquals(2 * 86400000 + 43200000, timeEnvUtil.computeWaitTimeForDayInWeek(EDayInWeek.SATURDAY));
		assertEquals(3 * 86400000 + 43200000, timeEnvUtil.computeWaitTimeForDayInWeek(EDayInWeek.SUNDAY));

	}

	@Test
	public void timeEnvUtilTest18() {

		timeEnvUtil = new StandardTimeProvider(eventProcessor, parse("2014-09-10"));
		when(eventProcessor.getCurrentTime()).thenReturn(
				Duration.ofDays(7).toMillis() + Duration.ofHours(10).toMillis());

		assertEquals(parse("2014-09-17"), timeEnvUtil.getCurrentDate());
		assertEquals(parse("2014-09-17T10:00:00"), timeEnvUtil.getCurrentZonedDateTime());

	}

	@Test
	public void timeEnvUtilTest19() {

		timeEnvUtil = new StandardTimeProvider(eventProcessor, parse("2014-09-10T10:00:00"));
		when(eventProcessor.getCurrentTime()).thenReturn(
				Duration.ofDays(7).toMillis() + Duration.ofHours(10).toMillis());

		assertEquals(parse("2014-09-17"), timeEnvUtil.getCurrentDate());
		assertEquals(parse("2014-09-17T10:00:00"), timeEnvUtil.getCurrentZonedDateTime());

	}

	@Test
	public void timeEnvUtilTest20() {

		timeEnvUtil = new StandardTimeProvider(eventProcessor, parse("2014-09-08"));
		when(eventProcessor.getCurrentTime()).thenReturn(
				Duration.ofDays(7).toMillis() + Duration.ofHours(10).toMillis());

		assertEquals(Duration.ofDays(1).toMillis() + Duration.ofHours(19).toMillis(),
					 timeEnvUtil.computeWaitTime(parse("2014-09-17"), Duration.ofHours(5).toMillis()));

		assertEquals(Duration.ofHours(-5).toMillis(),
					 timeEnvUtil.computeWaitTime(parse("2014-09-15"), Duration.ofHours(5).toMillis()));

	}

	private ZonedDateTime parse(String s) {
		if(s.contains("T")){
			return LocalDateTime.parse(s).atZone(ZoneId.systemDefault());
		}else{
			return LocalDate.parse(s).atStartOfDay(ZoneId.systemDefault());
		}
	}

}
