package es.udc.ws.bikes.model.bikeservice;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidDateException;
import es.udc.ws.bikes.model.bikeservice.exceptions.NumberOfBikesException;
import es.udc.ws.bikes.model.bikeservice.exceptions.RateRentDateException;
import es.udc.ws.bikes.model.rent.Rent;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class BikeServiceImpl implements BikeService {

	@Override
	public Bike addBike(Bike bike) throws InputValidationException, InvalidDateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Bike bike) throws InputValidationException, InstanceNotFoundException, InvalidDateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Bike findBikeById(String modelName) throws InputValidationException, InstanceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bike> findBikeByKeyword(String keywords, Calendar date) throws InputValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long rentBike(String email, Long creditCard, String modelName, Calendar startRentDate,
			Calendar finishRentDate, int numberOfBikes)
			throws InputValidationException, NumberOfBikesException, InvalidDateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Rent> findRentByUser(String email) throws InputValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rateRent(Long rentId, int score)
			throws InputValidationException, InstanceNotFoundException, RateRentDateException {
		// TODO Auto-generated method stub
		
	}
	// TODO Imp Casos Uso
	

}
