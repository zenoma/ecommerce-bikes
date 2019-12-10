package es.udc.ws.app.model.bikeservice.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class InvalidRentPeriodException extends Exception {

	private Calendar startDate;
	private Calendar finishDate;

	public InvalidRentPeriodException(Calendar startDate, Calendar finishDate) {

		super("Invalid reservation period: Period between " + startDate
				+ " and " + finishDate + " is greater than 15 days.");
		this.startDate = startDate;
		this.finishDate = finishDate;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Calendar finishDate) {
		this.finishDate = finishDate;
	}

}
