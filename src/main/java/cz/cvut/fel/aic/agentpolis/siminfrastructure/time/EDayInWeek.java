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

import java.util.HashMap;
import java.util.Map;

/**
 * Representation days in a week.
 * 
 * @author Zbynek Moler
 *
 */
public enum EDayInWeek {
	MONDAY(0,true,"MON"),
	TUESDAY(1,true,"TUE"),
	WEDNESDAY(2,true,"WED"),
	THURSDAY(3,true,"THU"),
	FRIDAY(4,true,"FRI"),
	SATURDAY(5,false,"SAT"),
	SUNDAY(6,false,"SUN");
	
	private static final EDayInWeek[] VALUES = EDayInWeek.values();
	public final boolean isWeekDay;
	public final int weekDayShiftIndex;
	public final String acronym;
	public static final int numberOfDayInWeek = 7;
	
	private static final Map<String,EDayInWeek> ACRONYM_MAP = new HashMap<>();

	static {
		for (EDayInWeek day : values()) {
			ACRONYM_MAP.put(day.acronym, day);
		}
	}
	
	private EDayInWeek(int weekDayShiftIndex, boolean isWeekDay, String acronym) {
		this.weekDayShiftIndex = weekDayShiftIndex;
		this.isWeekDay = isWeekDay;
		this.acronym = acronym;
	}
	
	/**
	 * Get day from {@code acronym}. It is case-insensitive. If a day with
	 * {@code acronym} doesn't exist, {@link IllegalArgumentException} is
	 * thrown.
	 * 
	 * @param acronym
	 * @return
	 * @throws IllegalArgumentException
	 *			 if a day with {@code acronym} doesn't exist
	 */
	public static EDayInWeek getDayFromAcronym(String acronym) throws IllegalArgumentException{
		EDayInWeek day = ACRONYM_MAP.get(acronym.toUpperCase());
		if (day == null)
			throw new IllegalArgumentException("Day with acronym '" + acronym + "' doesn't exist.");
		return day;
	}


	/**
	 * 
	 * @param dayIndex
	 * MONDAY(0) 
	 * TUESDAY(1),
	 * WEDNESDAY(2),
	 * THURSDAY(3),
	 * FRIDAY(4),
	 * SATURDAY(5),
	 * SUNDAY(6);
	 * 
	 * @return
	 */
	public static EDayInWeek getDayInWeekBasedOnIndex(int dayIndex){
		
		if(!(dayIndex >= 0 && dayIndex < 7)){
			throw new RuntimeException("Index for day in week is not in range 0 ... 6");
		}
		
		return VALUES[dayIndex];
	}
	
	
	
}
