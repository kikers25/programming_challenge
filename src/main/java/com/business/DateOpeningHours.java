package com.business;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.business.BusinessHourCalculator.DayOfWeek;
import com.business.exception.BusinessHourCalculatorException;
import com.business.util.ConvertStringToDate;

public class DateOpeningHours {

	private final static String DATE_PATTERN = "yyyy-MM-dd";
	private final static int NO_SPECIFIC_VALUE = -1;
	private String defaultOpeningTime;
	private String defaultClosingTime;

	private List<String> dates;
	private List<String> opening_time_dates;
	private List<String> closing_time_dates;

	private List<DayOfWeek> daysOfWeek;
	private List<String> opening_time_days_of_week;
	private List<String> closing_time_days_of_week;
	private ConvertStringToDate convertToDate;

	public DateOpeningHours(String defaultOpeningTime, String defaultClosingTime) {
		this.defaultOpeningTime = defaultOpeningTime;
		this.defaultClosingTime = defaultClosingTime;

		dates = new ArrayList<String>();
		opening_time_dates = new ArrayList<String>();
		closing_time_dates = new ArrayList<String>();

		daysOfWeek = new ArrayList<DayOfWeek>();
		opening_time_days_of_week = new ArrayList<String>();
		closing_time_days_of_week = new ArrayList<String>();

		convertToDate = new ConvertStringToDate();
	}

	public void checkValues() throws BusinessHourCalculatorException {
		if ("".equals(defaultOpeningTime) || "".equals(defaultClosingTime)) {
			throw new BusinessHourCalculatorException();
		}
		checkDefaultOpeningTimeIsNotAfterClosingTime();
	}

	public void addNewDate(String date_opening_hours, String specific_opening_time, String specific_closing_time) {
		this.dates.add(date_opening_hours);
		this.opening_time_dates.add(specific_opening_time);
		this.closing_time_dates.add(specific_closing_time);
	}

	public void addNewDayOfWeek(DayOfWeek day_of_week, String specific_opening_time, String specific_closing_time) {
		daysOfWeek.add(day_of_week);
		opening_time_days_of_week.add(specific_opening_time);
		closing_time_days_of_week.add(specific_closing_time);
	}

	public DateTime getOpeningDateOf(DateTime date) {
		if (isThereOneSpecificOpeningHoursFor(date)) {
			return generateDateOf(dateStringOf(date), opening_time_dates.get(numberForSpecificDate(date)));

		} else if (isThereAnOpeningHoursForDayOfWeekOf(date)) {
			return generateDateOf(dateStringOf(date), opening_time_days_of_week.get(numberForSpecificDayOfWeekOf(date)));

		} else {
			return generateDateOf(dateStringOf(date), defaultOpeningTime);
		}
	}

	public DateTime getClosingDateOf(Task task) {
		DateTime dateOfReference = task.getDateOfReference();
		if (isThereOneSpecificOpeningHoursFor(dateOfReference)) {
			return generateDateOf(dateStringOf(dateOfReference),
			        closing_time_dates.get(numberForSpecificDate(dateOfReference)));

		} else if (isThereAnOpeningHoursForDayOfWeekOf(dateOfReference)) {
			return generateDateOf(dateStringOf(dateOfReference),
			        closing_time_days_of_week.get(numberForSpecificDayOfWeekOf(dateOfReference)));

		} else {
			return generateDateOf(dateStringOf(dateOfReference), defaultClosingTime);
		}
	}

	private boolean isThereAnOpeningHoursForDayOfWeekOf(DateTime date) {
		return numberForSpecificDayOfWeekOf(date) != NO_SPECIFIC_VALUE;
	}

	private boolean isThereOneSpecificOpeningHoursFor(DateTime date) {
		return numberForSpecificDate(date) != NO_SPECIFIC_VALUE;
	}

	private int numberForSpecificDate(DateTime date) {
		for (int index = 0; index < dates.size(); index++) {
			String date_opening_hours = dates.get(index);
			if (date.toString(DATE_PATTERN).equals(date_opening_hours)) {
				return index;
			}
		}
		return NO_SPECIFIC_VALUE;
	}

	private int numberForSpecificDayOfWeekOf(DateTime date) {
		int dayOfWeek = date.getDayOfWeek();
		int index = 0;
		for (DayOfWeek dayOfWeekSpecificFor : daysOfWeek) {
			if (dayOfWeek == dayOfWeekSpecificFor.getNumber()) {
				return index;
			}
			index++;
		}
		return NO_SPECIFIC_VALUE;
	}

	private DateTime generateDateOf(String date_string, String time_string) {
		String whole_date_string = date_string + " " + time_string;
		return convertToDate.from(whole_date_string);
	}

	private String dateStringOf(DateTime date_time) {
		return date_time.toString(DateTimeFormat.forPattern("yyyy-MM-dd"));
	}

	private void checkDefaultOpeningTimeIsNotAfterClosingTime() throws BusinessHourCalculatorException {
		DateTime now = DateTime.now();
		DateTime openingTimeOfToday = generateDateOf(dateStringOf(now), defaultOpeningTime);
		DateTime closingTimeOfToday = generateDateOf(dateStringOf(now), defaultClosingTime);
		if (openingTimeOfToday.isAfter(closingTimeOfToday)) {
			throw new BusinessHourCalculatorException("opening time is after closing time");
		}
	}

}
