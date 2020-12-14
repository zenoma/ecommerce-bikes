package es.udc.ws.app.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class InvalidUserRateException extends Exception{
	private Long rentId;

	public InvalidUserRateException(Long rentId, String userEmail) {
		super("Invalid User Rate: The rent  \"" + rentId
				+ "\" doesn't belong to the user: " + userEmail);
		this.rentId = rentId;
	}

	public Long getRentId() {
		return rentId;
	}

	public void setRentId(Long rentId) {
		this.rentId = rentId;
	}
}
