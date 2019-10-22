package es.udc.ws.bikes.model.bikeservice;

import java.util.Calendar;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

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
import es.udc.ws.util.validation.PropertyValidator;
import es.udc.ws.bikes.model.util.BikesPropertyValidator;

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
	
	private void validateBike (Bike bike) throws InputValidationException {
		
		PropertyValidator.validateMandatoryString("modelName", bike.getModelName());
		PropertyValidator.validateMandatoryString("description", bike.getDescription());
		BikesPropertyValidator.validateLowerFloat("price", bike.getPrice(), 0);
		BikesPropertyValidator.validateLowerInt("availableNumber", bike.getAvailableNumber(), 1);
		PropertyValidator.validatePastDate("startDate", bike.getStartDate());
		
	}
	
	private void initBike (Bike bike) {
		
		bike.setAdquisitionDate(Calendar.getInstance());
		bike.setAverageScore(0);
		bike.setNumberOfRents(0);
	}
	
	@Override
	public Bike addBike(Bike bike) throws InputValidationException, InvalidDateException {
		// TODO Auto-generated method stub

		validateBike(bike);
		initBike(bike);
			
		try (Connection connection = dataSource.getConnection()) {
			
			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Bike createdBike = bikeDao.create(connection, bike);

				/* Commit. */
				connection.commit();

				return createdBike;

			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Bike bike) throws InputValidationException, InstanceNotFoundException, InvalidDateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Bike findBikeById(Long bikeId) throws InputValidationException, InstanceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bike> findBikeByKeyword(String keywords, Calendar date) throws InputValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long rentBike(String email, Long creditCard, Long bikeId, Calendar startRentDate,
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
