package es.udc.ws.bikes.model.bikeservice.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class UpdateReservedBikeException extends Exception {

	private Long bikeId;
	private Calendar date;
	private Calendar startDate;

	public UpdateReservedBikeException(Long bikeId, Calendar date,
			Calendar startDate) {
		super("Bike with id=\"" + bikeId + "\" can't delay the rent day to \""
				+ date + "\".(Actual bike rent day = \"" + startDate + ")");
		this.bikeId = bikeId;
		this.date = date;
		this.startDate = startDate;
	}

	public Long getBikeId() {
		return bikeId;
	}

	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

}
