package es.udc.ws.bikes.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class InvalidDateException extends Exception {
	
	public InvalidDateException(String message) {
        super(message);
    }
}
