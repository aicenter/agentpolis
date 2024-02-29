package cz.cvut.fel.aic.agentpolis.siminfrastructure.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeParser {
	public static ZonedDateTime parseDateTime(String dateTimeString) {
		LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
	}

	public static ZonedDateTime createDateTimeFromMillis(long millis) {
		return ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(millis), ZoneId.systemDefault());
	}

	public static ZonedDateTime parseDateTimeFromUnknownFormat(String unknownFormat) {
		if(unknownFormat.contains(" ")) {
			return parseDateTime(unknownFormat);
		}
		else {
			return createDateTimeFromMillis(Long.parseLong(unknownFormat));
		}
	}
}
