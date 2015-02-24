package com.business;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.business.BusinessHourCalculator;
import com.business.exception.BusinessHourCalculatorException;

public class SimpleDeadlineTest extends BusinessHourCalculatorTest {

	@Test(expected = BusinessHourCalculatorException.class)
	public void //
	should_throw_exception_when_date_format_is_not_right() throws Exception {
		calculator.calculateDeadline(hours(1), "2010-12-24");
	}

	@Test(expected = BusinessHourCalculatorException.class)
	public void //
	should_throw_exception_when_parameter_seconds_is_bigger_than_the_maximum_of_a_integer() throws Exception {
		Long seconds = Integer.MAX_VALUE + 1L;
		calculator.calculateDeadline(seconds, "2010-12-24 12:00");
	}

	@Test(expected = BusinessHourCalculatorException.class)
	public void //
	should_throw_exception_when_default_opening_time_is_empty() throws Exception {
		calculator = new BusinessHourCalculator("", "15:00");

		calculator.calculateDeadline(hours(6), "2010-12-24 06:45");
	}

	@Test(expected = BusinessHourCalculatorException.class)
	public void //
	should_throw_exception_when_default_closing_time_is_empty() throws Exception {
		calculator = new BusinessHourCalculator("09:00", "");

		calculator.calculateDeadline(hours(6), "2010-12-24 06:45");
	}

	@Test
	public void //
	should_throw_exception_when_opening_time_is_after_closing_time() throws Exception {
		calculator = new BusinessHourCalculator("11:00", "09:00");

		try {
			calculator.calculateDeadline(hours(6), "2010-12-24 06:45");
			fail("No threw exception");
		} catch (BusinessHourCalculatorException e) {
			assertThat(e.getMessage(), is("opening time is after closing time"));
		}
	}

	@Test
	public void //
	should_return_a_date_with_the_same_date_and_time_when_the_seconds_are_zero() throws Exception {
		check("2010-12-24 09:30", 0, "2010-12-24 09:30:00");
	}

	@Test
	public void //
	should_return_a_date_with_different_seconds_when_the_seconds_are_just_few() throws Exception {
		check("2010-12-24 09:30", seconds(10), "2010-12-24 09:30:10");
		check("2010-12-24 09:30", seconds(30), "2010-12-24 09:30:30");
		check("2010-12-24 09:30", seconds(7), "2010-12-24 09:30:07");
	}

	@Test
	public void //
	should_return_the_same_date_plus_the_time_when_date_plus_time_in_seconds_dont_reach_the_closing_time()
	        throws Exception {
		check("2010-12-24 09:45", hours(2), "2010-12-24 11:45:00");
		check("2010-12-24 09:45", minutes(15) + seconds(1), "2010-12-24 10:00:01");
		check("2010-12-24 09:45", hours(6), "2010-12-24 15:45:00");
	}

	@Test
	public void //
	should_return_the_next_day_when_there_is_no_time_to_finish_it_in_the_business_hours() throws Exception {
		calculator = new BusinessHourCalculator("09:00", "17:00");

		check("2010-12-24 16:00", hours(2), "2010-12-25 10:00:00");
		check("2010-12-24 16:00", hours(6), "2010-12-25 14:00:00");
		check("2010-12-24 16:00", hours(1) + seconds(1), "2010-12-25 09:00:01");
	}

	@Test
	public void //
	should_return_a_date_within_two_days_when_there_is_no_time_to_finish_it_in_the_business_hours_of_the_day_and_the_next_day()
	        throws Exception {
		calculator = new BusinessHourCalculator("09:00", "17:00");

		check("2010-12-24 16:00", business_days(2), "2010-12-26 16:00:00");
		check("2010-12-24 10:00", business_days(2) + hours(2), "2010-12-26 12:00:00");
	}

	@Test
	public void //
	should_return_a_date_within_one_week() throws Exception {
		calculator = new BusinessHourCalculator("09:00", "17:00");

		check("2010-12-01 13:00", business_weeks(1) + minutes(15), "2010-12-08 13:15:00");
		check("2010-12-01 10:00", business_weeks(1) + hours(5), "2010-12-08 15:00:00");
	}

}
