package es.udc.ws.app.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class RentAlreadyRatedException extends Exception {
	private Long rentId;

	public RentAlreadyRatedException(Long rentId) {
		super("Invalid Rate Rent: The rent \"" + rentId
				+ "\" is already rated.");
		this.rentId = rentId;
	}

	public Long getRentId() {
		return rentId;
	}

	public void setRentId(Long rentId) {
		this.rentId = rentId;
	}
}
