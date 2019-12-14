package es.udc.ws.app.model.bikeservice;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.model.bike.Bike;
import es.udc.ws.app.model.bikeservice.exceptions.*;
import es.udc.ws.app.model.rent.Rent;
import es.udc.ws.util.exceptions.*;

public interface BikeService {
	public Bike addBike(String modelName, String description,
			Calendar startDate, float price, int availableNumber)
			throws InputValidationException, NumberOfBikesException;

	public void updateBike(Long bikeId, String modelName, String description,
			Calendar startDate, float price, int availableNumber)
			throws InputValidationException, InstanceNotFoundException,
			UpdateReservedBikeException, NumberOfBikesException;

	public Bike findBike(Long bikeId) throws InstanceNotFoundException;

	public List<Bike> findBikes(String keywords, Calendar date);

	public Long rentBike(String email, String creditCard, Long bikeId,
			Calendar startRentDate, Calendar finishRentDate, int numberOfBikes)
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriodException, InstanceNotFoundException;

	public List<Rent> findRents(String email) throws InputValidationException;

	public void rateRent(Long rentId, int score)
			throws InputValidationException, InstanceNotFoundException,
			RentExpirationException;

}
