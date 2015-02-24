package com.business;

import java.util.Date;

import com.business.exception.BusinessHourCalculatorException;
import com.business.util.ConvertStringToDate;

/**
 * Allows one to define the opening and closing time for each day and calculates
 * the time when it's going to be ready the work
 * 
 * @author Enrique
 * 
 */
public class BusinessHourCalculator {

	private ClosedDateAndDay closedDays;
	private DateOpeningHours dateOpeningHours;
	private ConvertStringToDate convertToDate;

	public enum DayOfWeek {
		MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);

		// This field is the same value of DayTime.getDayOfWeek()
		// On this way it's easier to calculate when a day of week is close
		private int number;

		DayOfWeek(int number) {
			this.number = number;
		}

		public int getNumber() {
			return number;
		}
	}

	/**
	 * Both parameters should have 24h time format. For example: "09:00",
	 * "15:55", "00:01"
	 * 
	 * @param defaultOpeningTime
	 *            string with the opening time in an specific format
	 * @param defaultClosingTime
	 *            string with the closing time in an specific format
	 */
	public BusinessHourCalculator(String defaultOpeningTime, String defaultClosingTime) {
		dateOpeningHours = new DateOpeningHours(defaultOpeningTime, defaultClosingTime);
		closedDays = new ClosedDateAndDay();
		convertToDate = new ConvertStringToDate();
	}

	/**
	 * Add a specific day when the store is closed
	 * 
	 * @param closing_dates
	 *            date or dates when store is closed. The dates should have the
	 *            format yyyy-MM-dd.
	 */
	public void setClosed(String... closing_dates) {
		closedDays.addDate(closing_dates);
	}

	/**
	 * Add a specific day of week when the store is closed
	 * 
	 * @param days_of_week
	 */
	public void setClosed(DayOfWeek... days_of_week) {
		closedDays.addDayOfWeek(days_of_week);
	}

	/**
	 * Add a specific opening hours for one day.
	 * 
	 * @param date_opening_hours
	 *            Date of the specific opening date. It has to have the format
	 *            yyyy-MM-dd
	 * @param specific_opening_time
	 *            opening hour. It should have 24h time format: hh:mm
	 * @param specific_closing_time
	 *            closing hour. It should have 24h time format: hh:mm
	 */
	public void setOpeningHours(String date_opening_hours, String specific_opening_time, String specific_closing_time) {
		dateOpeningHours.addNewDate(date_opening_hours, specific_opening_time, specific_closing_time);
	}

	/**
	 * Add a specific opening hour for a day of week. Note: If there is an
	 * specific opening hour for one day It will be more than the day of week.
	 * 
	 * @param day_of_week
	 * @param specific_opening_time
	 *            opening hour. It should have 24h time format: hh:mm
	 * @param specific_closing_time
	 *            closing hour. It should have 24h time format: hh:mm
	 */
	public void setOpeningHours(DayOfWeek day_of_week, String specific_opening_time, String specific_closing_time) {
		dateOpeningHours.addNewDayOfWeek(day_of_week, specific_opening_time, specific_closing_time);
	}

	/**
	 * Calculates the date when it's going to be ready the work
	 * 
	 * @param seconds
	 *            time to finish the task in seconds
	 * @param date_string
	 *            current date. It should have a specific format of
	 *            year-month-day hour:minute. For example: "2010-12-26 16:00",
	 *            "2000-01-01 09:05" and "2014-08-11 19:32"
	 * @return deadline a {@link java.util.Data Data} object
	 * @throws BusinessHourCalculatorException
	 */
	public Date calculateDeadline(long seconds, String date_string) throws BusinessHourCalculatorException {
		Task task;

		checkParameters(seconds, date_string);
		task = new Task(convertToDate.from(date_string), seconds);

		task.calculateForDayOfReference(dateOpeningHours);
		if (task.isFinish()) {
			return task.deadLine();
		}

		do {
			task.nextOpenDayWithOpeningHour(closedDays, dateOpeningHours);
			task.calculateForDayOfReference(dateOpeningHours);
		} while (task.isFinish() == false);

		return task.deadLine();
	}

	private void checkParameters(long seconds, String date_string) throws BusinessHourCalculatorException {
		dateOpeningHours.checkValues();
		convertToDate.check(date_string);
		if (seconds > Integer.MAX_VALUE) {
			throw new BusinessHourCalculatorException("the maximum value of seconds is " + Integer.MAX_VALUE);
		}
	}

}
