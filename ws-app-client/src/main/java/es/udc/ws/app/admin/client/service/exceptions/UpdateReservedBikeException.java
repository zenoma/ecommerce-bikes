package es.udc.ws.app.admin.client.service.exceptions;

@SuppressWarnings("serial")
public class UpdateReservedBikeException extends Exception {

	private Long bikeId;
	private String date;
	private String startDate;

	public UpdateReservedBikeException(Long bikeId, String date,
			String startDate) {
		super("Bike with id=\"" + bikeId + "\" can't delay the rent day to \""
				+ date + "\".(Actual bike rent day = \""
				+ startDate + ")");
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

}
