package es.udc.ws.bikes.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class InvalidRentPeriod extends Exception {
	
	public InvalidRentPeriod(String message) {
        super(message);
    }
}
