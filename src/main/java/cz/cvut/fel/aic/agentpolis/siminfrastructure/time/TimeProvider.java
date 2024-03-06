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

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 *	Simulation time provider interface
 * @author fido
 */
public interface TimeProvider {

	ZonedDateTime getInitDateTime();

	/**
	 * Provides simulation time.
	 *
	 * @return Simulation time in milliseconds.
	 */
	public long getCurrentSimTime();


	/**
	 * Provides a date and time that represents the current simulation time.
	 **/
	ZonedDateTime getCurrentSimDateTime();


	/**
	 * Provides a datetime that represents the provided simulation time.
	 * @param simTime Simulation time in milliseconds.
	 * @return ZonedDateTime
	 **/
	ZonedDateTime getDateTimeFromSimTime(long simTime);

	/**
	 * Provides the number of milliseconds between the start of the simulation and the provided datetime.
	 * @param dateTime ZonedDateTime
	 * @return time in milliseconds that represents the provided datetime.
	 **/
	long getSimTimeFromDateTime(ZonedDateTime dateTime);
}
