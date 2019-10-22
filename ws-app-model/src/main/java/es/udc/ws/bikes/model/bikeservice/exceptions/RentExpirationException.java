package es.udc.ws.bikes.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class RentExpirationException extends Exception{

	public RentExpirationException(String message) {
        super(message);
    }
}
