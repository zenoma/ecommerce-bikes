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

import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidRentPeriod;
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

	private void validateBike(Bike bike)
			throws InputValidationException {

		PropertyValidator.validateMandatoryString("modelName",
				bike.getModelName());
		PropertyValidator.validateMandatoryString("description",
				bike.getDescription());
		BikesPropertyValidator.validateLowerFloat("price", bike.getPrice(), 0);
		BikesPropertyValidator.validateLowerInt("availableNumber",
				bike.getAvailableNumber(), 1);
		BikesPropertyValidator.validatePairDates(bike.getAdquisitionDate(),
				bike.getStartDate());
		BikesPropertyValidator.validatePreviousDate("adquisitionDate",
				bike.getStartDate());
		BikesPropertyValidator.validatePairDates(bike.getAdquisitionDate(),
				Calendar.getInstance());
		BikesPropertyValidator.validateLowerInt("numberOfRents",
				bike.getNumberOfRents(), 0);
		BikesPropertyValidator.validateLowerDouble("averageScore",
				bike.getAverageScore(), 0);

	}

	private void initBike(Bike bike) {

		bike.setAdquisitionDate(Calendar.getInstance());
		bike.setAverageScore(0);
		bike.setNumberOfRents(0);
	}

	@Override
	public Bike addBike(Bike bike) throws InputValidationException {
		validateBike(bike);
		initBike(bike);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(
						Connection.TRANSACTION_SERIALIZABLE);
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
	public void update(Bike bike) throws InputValidationException,
			InstanceNotFoundException {

		validateBike(bike);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(
						Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				// TODO No se puede actualizar una bici que tiene hecha una
				// reserva
				bikeDao.update(connection, bike);

				/* Commit. */
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
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
	public Bike findBike(Long bikeId) throws InstanceNotFoundException {

		try (Connection connection = dataSource.getConnection()) {
			return bikeDao.find(connection, bikeId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Bike> findBikes(String keywords, Calendar date) {

		try (Connection connection = dataSource.getConnection()) {
			return bikeDao.findByKeywords(connection, keywords, date);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Long rentBike(String email, Long creditCard, Long bikeId,
			Calendar startRentDate, Calendar finishRentDate, int numberOfBikes)
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriod, InstanceNotFoundException {
		BikesPropertyValidator.validateCreditCard("creditCard", creditCard);
		BikesPropertyValidator.validateEmail("email", email);
		BikesPropertyValidator.validateRentPeriod(startRentDate,
				finishRentDate);
		BikesPropertyValidator.validateLowerInt("numberOfBikes", numberOfBikes,
				1);
		try (Connection connection = dataSource.getConnection()) {
			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(
						Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				// Validate Rent
				Bike bike = bikeDao.find(connection, bikeId);
				BikesPropertyValidator.validatePairDates(bike.getStartDate(),
						startRentDate);
				// TODO Validar numero de bicis disponibles
				BikesPropertyValidator.validateNumberOfBikes("numberOfBikes",
						bike, numberOfBikes);
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.MILLISECOND, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				BikesPropertyValidator.validatePairDates(calendar,
						startRentDate);

				/* Do work. */
				Rent rent = new Rent(email, bikeId, creditCard, startRentDate,
						finishRentDate, numberOfBikes);
				Rent createdRent = rentDao.create(connection, rent);
				bike.setAvailableNumber(
						bike.getAvailableNumber() - numberOfBikes);
				bikeDao.update(connection, bike);

				/* Commit. */
				connection.commit();

				return createdRent.getRentId();
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			} catch (InputValidationException | InstanceNotFoundException e) {
				connection.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Rent> findRents(String email) throws InputValidationException {
		BikesPropertyValidator.validateEmail("email", email);
		try (Connection connection = dataSource.getConnection()) {
			return rentDao.findByUser(connection, email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void rateRent(Long rentId, int score)
			throws InputValidationException, InstanceNotFoundException,
			RateRentDateException {
		BikesPropertyValidator.validateScore("score", score);

		//TODO Cuando se actualize el Alquiler se ignoran los campos calculados
		try (Connection connection = dataSource.getConnection()) {
			Rent rent = rentDao.find(connection, rentId);
			// Add 1 to finishRentDate
			Calendar date = rent.getFinishRentDate();
			date.add(Calendar.DAY_OF_YEAR, 1);
			BikesPropertyValidator.validatePreviousDate("finishRentDate", date);

		} catch (SQLException e) {
			throw new RuntimeException(e);

		}
	}

}
