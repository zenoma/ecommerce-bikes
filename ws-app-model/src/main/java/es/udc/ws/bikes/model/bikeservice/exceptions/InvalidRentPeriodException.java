package es.udc.ws.bikes.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class InvalidRentPeriodException extends Exception {

	public InvalidRentPeriodException(String message) {
		super(message);
	}
}
