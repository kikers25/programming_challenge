package com.business.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.business.exception.BusinessHourCalculatorException;

public class ConvertStringToDate {

	public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm";

	public void check(String date_string) throws BusinessHourCalculatorException {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern(PATTERN_DATE_TIME);
			formatter.parseDateTime(date_string);
		} catch (Exception e) {
			throw new BusinessHourCalculatorException(e.getMessage());
		}
	}

	public DateTime from(String date_string) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(PATTERN_DATE_TIME);
		DateTime dateTime = formatter.parseDateTime(date_string);
		return dateTime;
	}

}
