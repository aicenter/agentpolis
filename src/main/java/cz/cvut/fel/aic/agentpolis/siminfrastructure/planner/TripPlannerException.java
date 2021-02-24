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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner;

/**
 * 
 * Class represents an exception definition. This exception should be thrown, if
 * planner is not able to find a trip from start position to destination position.
 * 
 * @author Zbynek Moler
 *
 */
public class TripPlannerException extends Exception {

	private static final long serialVersionUID = 3197313361674952603L;

	private static final String EXCEPTION_MESSAGE = "Trip Planner did not find any way from %s to %s";
	
	public TripPlannerException(long startNodeById, long destinationNodeById) {
		super(String.format(EXCEPTION_MESSAGE,startNodeById,destinationNodeById));
	}
}
