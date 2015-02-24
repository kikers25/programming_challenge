package com.business;

import org.junit.Test;

import com.business.BusinessHourCalculator.DayOfWeek;

public class ClosedDatesAndClosedDaysOfWeekTest extends BusinessHourCalculatorTest {

	@Test
	public void //
	should_notice_a_closed_day_to_calculate_the_deadline() throws Exception {
		calculator.setClosed("2013-08-13");

		check("2013-08-12 16:00", hours(1) + seconds(1), "2013-08-14 09:00:01");
		check("2013-08-12 09:15", business_days(1), "2013-08-14 09:15:00");
		check("2013-08-12 09:15", business_days(2), "2013-08-15 09:15:00");
		check("2013-08-11 09:15", business_days(2), "2013-08-14 09:15:00");
	}

	@Test
	public void //
	should_notice_two_closed_days_together_to_calculate_the_deadline() throws Exception {
		calculator.setClosed("2013-08-13", "2013-08-14");

		check("2013-08-12 09:15", business_days(1) + hours(2), "2013-08-15 11:15:00");
		check("2013-08-11 09:15", business_days(2) + minutes(15), "2013-08-15 09:30:00");
	}

	@Test
	public void //
	should_notice_two_closed_days_is_a_week_when_the_work_is_of_one_week() throws Exception {
		calculator.setClosed("2013-08-13", "2013-08-15");

		check("2013-08-12 09:15", business_weeks(1), "2013-08-21 09:15:00");
	}

	@Test
	public void //
	should_notice_when_there_is_a_closed_day_of_week() throws Exception {
		String friday_date = "2014-07-25 09:15";
		String sunday_date = "2014-07-27 09:15:00";
		calculator.setClosed(DayOfWeek.SATURDAY);

		check(friday_date, business_days(1), sunday_date);
	}

	@Test
	public void //
	should_notice_when_there_are_more_than_one_closed_days_of_week() throws Exception {
		String friday_date = "2014-07-25 09:15";
		String monday_date = "2014-07-28 09:15:00";
		calculator.setClosed(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

		check(friday_date, business_days(1), monday_date);
	}

}
