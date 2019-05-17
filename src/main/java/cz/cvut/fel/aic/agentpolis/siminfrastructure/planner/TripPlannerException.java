/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
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
