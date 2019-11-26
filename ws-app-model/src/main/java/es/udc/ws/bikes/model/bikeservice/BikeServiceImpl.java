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

import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidRentPeriodException;
import es.udc.ws.bikes.model.bikeservice.exceptions.NumberOfBikesException;
import es.udc.ws.bikes.model.bikeservice.exceptions.RateRentDateException;
import es.udc.ws.bikes.model.bikeservice.exceptions.RentExpirationException;
import es.udc.ws.bikes.model.bikeservice.exceptions.UpdateReservedBikeException;
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
			throws InputValidationException, NumberOfBikesException {

		PropertyValidator.validateMandatoryString("modelName",
				bike.getModelName());
		PropertyValidator.validateMandatoryString("description",
				bike.getDescription());
		BikesPropertyValidator.validateLowerFloat("price", bike.getPrice(), 0);
		BikesPropertyValidator.validateLowerInt("availableNumber",
				bike.getAvailableNumber(), 0);
		BikesPropertyValidator.validatePairDates(bike.getAdquisitionDate(),
				bike.getStartDate());
		BikesPropertyValidator.validatePairDates(bike.getAdquisitionDate(),
				Calendar.getInstance());
		BikesPropertyValidator.validateNumberOfBikes("numberOfBikes", bike,
				bike.getAvailableNumber());

	}

	private void validateRent(Rent rent)
			throws InputValidationException, InvalidRentPeriodException {
		BikesPropertyValidator.validateCreditCard("creditCard",
				rent.getCreditCard());
		BikesPropertyValidator.validateEmail("email", rent.getUserEmail());
		BikesPropertyValidator.validateRentPeriod(rent.getStartRentDate(),
				rent.getFinishRentDate());
		BikesPropertyValidator.validateLowerInt("numberOfBikes",
				rent.getNumberOfBikes(), 1);
	}

	@Override
	public Bike addBike(String modelName, String description,
			Calendar startDate, float price, int availableNumber)
			throws InputValidationException, NumberOfBikesException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		Bike bike = new Bike(modelName, description, startDate, price,
				availableNumber, calendar, 0, 0);
		validateBike(bike);

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
	public void updateBike(Long bikeId, String modelName, String description,
			Calendar startDate, float price, int availableNumber)
			throws InputValidationException, InstanceNotFoundException,
			UpdateReservedBikeException, NumberOfBikesException {
		try (Connection connection = dataSource.getConnection()) {

			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(
						Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Bike bike = bikeDao.find(connection, bikeId);
				bike.setModelName(modelName);
				bike.setDescription(description);
				if (bike.getNumberOfRents() > 0) {
					try {
						BikesPropertyValidator.validatePairDates(startDate,
								bike.getStartDate());
					} catch (InputValidationException e) {
						throw new UpdateReservedBikeException(
								"Not be able to delay the start rent day of "
										+ bike.getModelName());
					}
				}
				BikesPropertyValidator.validatePairDates(
						bike.getAdquisitionDate(), startDate);
				bike.setStartDate(startDate);
				bike.setPrice(price);
				bike.setAvailableNumber(availableNumber);
				validateBike(bike);
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
			throws InputValidationException, InvalidRentPeriodException,
			InstanceNotFoundException, NumberOfBikesException {

		try (Connection connection = dataSource.getConnection()) {
			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(
						Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				// Validate Rent
				Bike bike = bikeDao.find(connection, bikeId);
				// Miro que estoy dentro del rango de la bici
				BikesPropertyValidator.validatePairDates(bike.getStartDate(),
						startRentDate);
				// Miro que reservo con un día de antelación
				Calendar calendar = startRentDate;

				/* Do work. */
				calendar = Calendar.getInstance();
				calendar.set(Calendar.MILLISECOND, 0);
				calendar.set(Calendar.SECOND, 0);

				Rent rent = new Rent(email, bikeId, creditCard, startRentDate,
						finishRentDate, numberOfBikes, calendar,
						bike.getPrice() * numberOfBikes);
				validateRent(rent);
				Rent createdRent = rentDao.create(connection, rent);
				bike.setAvailableNumber(
						bike.getAvailableNumber() - numberOfBikes);
				bike.setNumberOfRents(bike.getNumberOfRents() + 1);
				validateBike(bike);
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
			RateRentDateException, RentExpirationException {
		BikesPropertyValidator.validateScore("score", score);
		// TODO No se pueden valorar 2 veces el mismo alquiler
		try (Connection connection = dataSource.getConnection()) {
			Rent rent = rentDao.find(connection, rentId);
			BikesPropertyValidator.validateRateRent("Rate Rent", rent);
			Bike bike = bikeDao.find(connection, rent.getBikeId());
			bike.setAverageScore((bike.getAverageScore() + score) / 2);
			bikeDao.update(connection, bike);

		} catch (SQLException e) {
			throw new RuntimeException(e);

		}
	}
}
