package com.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;

import com.business.BusinessHourCalculator.DayOfWeek;

public class ClosedDateAndDay {

	private List<String> closing_date_string;
	private List<DayOfWeek> closed_day_of_week;

	public ClosedDateAndDay() {
		this.closing_date_string = new ArrayList<String>();
		this.closed_day_of_week = new ArrayList<DayOfWeek>();
	}

	public void addDate(String[] closing_dates) {
		this.closing_date_string = Arrays.asList(closing_dates);
	}

	public void addDayOfWeek(DayOfWeek[] days_of_week) {
		this.closed_day_of_week = Arrays.asList(days_of_week);
	}

	public boolean isCloseThisDate(DateTime date) {
		int dayOfWeek = date.getDayOfWeek();
		for (DayOfWeek closedDayOfWeek : closed_day_of_week) {
			if (dayOfWeek == closedDayOfWeek.getNumber()) {
				return true;
			}
		}
		return closing_date_string.contains(date.toString("yyy-MM-dd"));
	}

}
