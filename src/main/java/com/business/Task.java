package com.business;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

public class Task {

	private DateTime dateOfReference;
	private long secondsToFinishTask;

	public Task(DateTime dateOfReference, long seconds) {
		this.dateOfReference = dateOfReference;
		this.secondsToFinishTask = seconds;
	}

	public void calculateForDayOfReference(DateOpeningHours dateOpeningHours) {
		DateTime closingDate;

		closingDate = dateOpeningHours.getClosingDateOf(this);
		dateOfReferenceLess(closingDate);
	}

	public boolean isFinish() {
		return secondsToFinishTask <= 0;
	}

	public void nextOpenDayWithOpeningHour(ClosedDateAndDay closed, DateOpeningHours dateOpeningHours) {
		calculateNextOpenDay(closed);
		calculateOpeningHour(dateOpeningHours);
	}

	public Date deadLine() {
		return dateOfReference.toDate();
	}

	public DateTime getDateOfReference() {
		return dateOfReference;
	}

	private void dateOfReferenceLess(DateTime date) {
		int restSecondsToFinishWorkDay;

		restSecondsToFinishWorkDay = Seconds.secondsBetween(dateOfReference, date).getSeconds();
		if (restSecondsToFinishWorkDay >= secondsToFinishTask) {
			dateOfReference = dateOfReference.plusSeconds((int) secondsToFinishTask);
		} else {
			dateOfReference = dateOfReference.plusSeconds(restSecondsToFinishWorkDay);
		}
		secondsToFinishTask = secondsToFinishTask - restSecondsToFinishWorkDay;
	}

	private void calculateNextOpenDay(ClosedDateAndDay closed) {
		dateOfReference = dateOfReference.plusDays(1);
		while (closed.isCloseThisDate(dateOfReference)) {
			dateOfReference = dateOfReference.plusDays(1);
		}
	}

	private void calculateOpeningHour(DateOpeningHours dateOpeningHours) {
		DateTime openingDate;
		openingDate = dateOpeningHours.getOpeningDateOf(dateOfReference);
		setDateOfReference(openingDate);
	}

	private void setDateOfReference(DateTime date) {
		dateOfReference = new DateTime(date);
	}

}
