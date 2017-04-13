/* 
 * AgentSCAI
 */
package cz.agents.agentpolis.siminfrastructure;

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
