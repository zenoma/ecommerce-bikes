package es.udc.ws.app.user.client.service.exception;

@SuppressWarnings("serial")
public class RentExpirationException extends Exception {

	private Long rentId;

	public RentExpirationException(Long rentId) {
		super("Invalid Rate Rent: The rent \"" + rentId + "\" is not finished.");
		this.rentId = rentId;
	}

	public Long getRentId() {
		return rentId;
	}

	public void setRentId(Long rentId) {
		this.rentId = rentId;
	}

}
