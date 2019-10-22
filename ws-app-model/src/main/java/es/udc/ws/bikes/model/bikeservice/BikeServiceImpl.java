package es.udc.ws.bikes.model.bikeservice;

import java.util.Calendar;
import java.util.List;
import java.sql.Connection;

import javax.sql.DataSource;

import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.bikes.model.bike.SqlBikeDao;
import es.udc.ws.bikes.model.bike.SqlBikeDaoFactory;
import es.udc.ws.bikes.model.rent.Rent;
import es.udc.ws.bikes.model.rent.SqlRentDao;
import es.udc.ws.bikes.model.rent.SqlRentDaoFactory;

import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidDateException;
import es.udc.ws.bikes.model.bikeservice.exceptions.NumberOfBikesException;
import es.udc.ws.bikes.model.bikeservice.exceptions.RateRentDateException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;

import static es.udc.ws.bikes.model.util.ModelConstants.BIKE_DATA_SOURCE;

public class BikeServiceImpl implements BikeService {

	private final DataSource dataSource;
	private SqlBikeDao bikeDao = null;
	private SqlRentDao rentDao = null;

	public BikeServiceImpl() {
		dataSource = DataSourceLocator.getDataSource(BIKE_DATA_SOURCE);
		bikeDao = SqlBikeDaoFactory.getDao();
		rentDao = SqlRentDaoFactory.getDao();
	}
	
	@Override
	public Bike addBike(Bike bike) throws InputValidationException, InvalidDateException {
		// TODO Auto-generated method stub
		
		Calendar actualDate = Calendar.getInstance();
		bike.setAdquisitionDate(actualDate);
		bike.setAverageScore(0);
		
		/*
		Calendar startDate = bike.getStartDate();
		if (startDate.compareTo(actualDate) < 0) {
			throw new InvalidDateException("Fecha anterior a la actual");
		}
		if (bike.getAvailableNumber() <= 0) {
			throw new InputValidationException("Numero de bicis igual o inferior a 0");
		}
		if (bike.getPrice() < 0) {
			throw new InputValidationException("Precio inferior a 0");
		}
		*/
		
		try (Connection connection = dataSource.getConnection()) {
			
		} catch (Exception e) {}
		
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
