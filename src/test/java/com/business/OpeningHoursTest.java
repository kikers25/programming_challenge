package com.business;

import org.junit.Test;

import com.business.BusinessHourCalculator;
import com.business.BusinessHourCalculator.DayOfWeek;

public class OpeningHoursTest extends BusinessHourCalculatorTest {

	@Test
	public void //
	should_notice_when_there_is_an_specific_opening_hour_for_the_same_date() throws Exception {
		calculator = new BusinessHourCalculator("09:00", "17:00");
		calculator.setOpeningHours("2013-05-01", "9:00", "19:00");

		check("2013-05-01 16:00", hours(2) + minutes(30), "2013-05-01 18:30:00");
	}

	@Test
	public void //
	should_notice_when_there_is_one_specific_opening_hour_for_another_date_within_time_to_finish_the_work()
	        throws Exception {
		calculator = new BusinessHourCalculator("09:00", "17:00");
		calculator.setOpeningHours("2013-05-02", "8:00", "17:00");

		check("2013-05-01 16:00", hours(1) + seconds(1), "2013-05-02 08:00:01");
	}

	@Test
	public void //
	should_notice_when_there_are_two_specific_opening_hours() throws Exception {
		calculator = new BusinessHourCalculator("09:00", "17:00");
		calculator.setOpeningHours("2013-05-02", "09:00", "11:00");
		calculator.setOpeningHours("2013-05-03", "10:00", "12:00");

		check("2013-05-02 10:00", hours(1) + seconds(1), "2013-05-03 10:00:01");
		check("2013-05-02 10:00", hours(1 + 2), "2013-05-03 12:00:00");
		check("2013-05-02 10:00", hours(1 + 2) + minutes(5), "2013-05-04 09:05:00");
	}

	@Test
	public void //
	should_notice_when_there_is_one_specific_opening_hour_for_a_day_per_week() throws Exception {
		String friday_at_2_pm = "2014-07-25 14:00";
		String thursday_at_2pm = "2014-07-24 14:00";
		String friday_at_4_pm = "2014-07-25 16:00:00";
		String friday_at_11_am = "2014-07-25 11:00:00";

		calculator = new BusinessHourCalculator("09:00", "15:00");
		calculator.setOpeningHours(DayOfWeek.FRIDAY, "10:00", "17:00");

		check(friday_at_2_pm, hours(2), friday_at_4_pm);
		check(thursday_at_2pm, hours(2), friday_at_11_am);
	}

	@Test
	public void //
	should_notice_when_there_are_two_specific_opening_hours_for_a_day_per_week() throws Exception {
		String thursday_at_2pm = "2014-07-24 14:00";
		String thursday_at_5_30pm = "2014-07-24 17:30:00";
		String friday_at_2_pm = "2014-07-25 14:00";
		String friday_at_4_pm = "2014-07-25 16:00:00";
		calculator = new BusinessHourCalculator("09:00", "15:00");
		calculator.setOpeningHours(DayOfWeek.THURSDAY, "10:00", "18:00");
		calculator.setOpeningHours(DayOfWeek.FRIDAY, "11:00", "17:00");

		check(thursday_at_2pm, hours(3) + minutes(30), thursday_at_5_30pm);
		check(friday_at_2_pm, hours(2), friday_at_4_pm);
		check("2014-07-24 16:00", hours(3), "2014-07-25 12:00:00");
	}
}
