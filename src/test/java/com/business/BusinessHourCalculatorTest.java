package com.business;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Before;

import com.business.BusinessHourCalculator;
import com.business.exception.BusinessHourCalculatorException;

public class BusinessHourCalculatorTest {

	protected BusinessHourCalculator calculator;
	// Mon Dec 27 11:00:00 2010
	public static final String PATTERN_DATE = "yyyy-MM-dd HH:mm:ss";

	@Before
	public void setup() {
		calculator = new BusinessHourCalculator("09:00", "17:00");
	}

	/**
	 * Assert to verify everything is ok
	 * 
	 * @param initial_date
	 *            initial date as string: yyyy-MM-dd hh:mm
	 * @param time_in_seconds
	 *            duration of work in seconds
	 * @param expected_date
	 *            expected date as string
	 * 
	 * @throws BusinessHourCalculatorException
	 * @throws ParseException
	 */
	protected void check(String initial_date, long time_in_seconds, String expected_date)
	        throws BusinessHourCalculatorException, ParseException {
		assertThat(calculator.calculateDeadline(time_in_seconds, initial_date), is(createDateOf(expected_date)));
	}

	/**
	 * A business week has 7 days
	 * 
	 * @param number
	 *            business weeks
	 * @return time in seconds
	 */
	protected long business_weeks(int number) {
		int days_per_week = 7;
		return business_days(number * days_per_week);
	}

	/**
	 * A business day has 8 hours. Because there is 8 hours between the opening
	 * (9:00) and closing (17:00) time of the shop.
	 * 
	 * @param number
	 *            business days
	 * @return time in seconds
	 */
	protected long business_days(int number) {
		final int business_hours = 8;
		return hours(business_hours * number);
	}

	protected Date createDateOf(String expected_date) throws ParseException {
		return new SimpleDateFormat(PATTERN_DATE).parse(expected_date);
	}

	protected long hours(int duration) {
		return TimeUnit.HOURS.toSeconds(duration);
	}

	protected long minutes(int duration) {
		return TimeUnit.MINUTES.toSeconds(duration);
	}

	protected long seconds(int number) {
		return number;
	}

}
