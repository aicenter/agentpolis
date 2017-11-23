/* 
 * Copyright (C) 2017 fido.
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
package cz.cvut.fel.aic.agentpolis.siminfrastructure;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * AgentAI log formater.
 * @author F.I.D.O.
 */
public class LogFormater extends Formatter{
	
	/**
	 * Line sepearator.
	 */
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    private static final DateFormat FORMATTER = new SimpleDateFormat("HH:mm:ss:SSS");

	@Override
	public String format(LogRecord record) {
        Date date = new Date(record.getMillis());
		String output = "(" + FORMATTER.format(date) + ")" + "[" + record.getLevel() + "]: " + formatMessage(record) 
                + LINE_SEPARATOR;
		return output;	
	}
	
}
