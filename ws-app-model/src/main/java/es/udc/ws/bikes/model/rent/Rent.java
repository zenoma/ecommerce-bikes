package es.udc.ws.bikes.model.rent;

import java.util.Date;

public class Rent {
	//Attributes - Pueden faltar
	String usserEmail;
	Integer creditCard;//puede que necesite otro tipo por la longitud del código de als tarjetas
	Date startRentDate;
	Date finishRentDate;
	int rentNumber;
	//getters
	public String getUsserEmail() {
		return usserEmail;
	}
	public Integer getCreditCard() {
		return creditCard;
	}
	public Date getStartRentDate() {
		return startRentDate;
	}
	public Date getFinishRentDate() {
		return finishRentDate;
	}
	public int getRentNumber() {
		return rentNumber;
	}
	// TODO setters, hash, equals
}
