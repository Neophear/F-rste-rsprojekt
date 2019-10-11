package db.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.sql.Date;
import java.sql.Timestamp;

public class Util {
	
	//Private to hide the default public one
	private Util() {}
	/**
	 * Convert a java.util.LocalDate to a java.sql.Date.
	 * @param l LocalDate to convert
	 * @return A SQL-ready date
	 */
	public static Date toDate(LocalDate l) {
		// Please don't ask why the cast is needed. Just move along.
		return (Date) Date.from(l.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	public static Timestamp dateTimeToTimestamp(LocalDateTime l) {
		return new Timestamp(l.toEpochSecond(ZoneOffset.UTC));
	}
}
