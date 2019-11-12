package es.udc.ws.bikes.model.bikeservice;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.bikes.model.bikeservice.exceptions.*;
import es.udc.ws.bikes.model.rent.Rent;
import es.udc.ws.util.exceptions.*;

public interface BikeService {

	//TODO Cambiar addBike, updateBike y findBike pasando parámetros en vez de el Objeto
	public Bike addBike(Bike bike)
			throws InputValidationException;

	public void update(Bike bike) throws InputValidationException,
			InstanceNotFoundException;

	public Bike findBike(Long bikeId) throws InstanceNotFoundException;

	public List<Bike> findBikes(String keywords, Calendar date);

	public Long rentBike(String email, Long creditCard, Long bikeId,
			Calendar startRentDate, Calendar finishRentDate, int numberOfBikes)
			throws InputValidationException, NumberOfBikesException, InvalidRentPeriod, InstanceNotFoundException;

	public List<Rent> findRents(String email) throws InputValidationException;

	public void rateRent(Long rentId, int score)
			throws InputValidationException, InstanceNotFoundException,
			RateRentDateException;

}
