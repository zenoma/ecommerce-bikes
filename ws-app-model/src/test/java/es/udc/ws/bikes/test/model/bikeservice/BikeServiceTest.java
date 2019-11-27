package es.udc.ws.bikes.test.model.bikeservice;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.bikes.model.bike.SqlBikeDao;
import es.udc.ws.bikes.model.bike.SqlBikeDaoFactory;
import es.udc.ws.bikes.model.bikeservice.BikeService;
import es.udc.ws.bikes.model.bikeservice.BikeServiceFactory;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidRentPeriodException;
import es.udc.ws.bikes.model.bikeservice.exceptions.NumberOfBikesException;
import es.udc.ws.bikes.model.bikeservice.exceptions.RentExpirationException;
import es.udc.ws.bikes.model.bikeservice.exceptions.UpdateReservedBikeException;
import es.udc.ws.bikes.model.rent.Rent;
import es.udc.ws.bikes.model.rent.SqlRentDao;
import es.udc.ws.bikes.model.rent.SqlRentDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import static es.udc.ws.bikes.model.util.ModelConstants.BIKE_DATA_SOURCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class BikeServiceTest {
	private final long NON_EXISTENT_BIKE_ID = -1;
	final String USER_EMAIL = "ws@email.com";

	private final long VALID_CREDIT_CARD_NUMBER = 1234_5678_9012_3456L;
	private final long INVALID_CREDIT_CARD_NUMBER = 1L;

	private static BikeService bikeService = null;

	private static SqlBikeDao bikeDao = null;

	private static SqlRentDao rentDao = null;

	@BeforeClass
	public static void init() {
		/*
		 * Create a simple data source and add it to "DataSourceLocator" (this
		 * is needed to test "es.udc.ws.bikes.model.bikeservice.BikeService"
		 */
		DataSource dataSource = new SimpleDataSource();

		/* Add "dataSource" to "DataSourceLocator". */
		DataSourceLocator.addDataSource(BIKE_DATA_SOURCE, dataSource);

		bikeService = BikeServiceFactory.getService();
		rentDao = SqlRentDaoFactory.getDao();
		bikeDao = SqlBikeDaoFactory.getDao();
	}

	private Bike getValidBike() {
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.MILLISECOND, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.add(Calendar.DAY_OF_YEAR, -5);
		return new Bike(1L, "Bike Model", "Bike Description", startDate, 200F,
				5, startDate, 0, 0);
	}

	private Bike createBike(String modelName, String description, float price,
			int availableNumber)
			throws InputValidationException, NumberOfBikesException {
		Bike addedBike = null;
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.MILLISECOND, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.add(Calendar.DAY_OF_YEAR, +5);
		addedBike = bikeService.addBike(modelName, description, startDate,
				price, availableNumber);
		return addedBike;
	}

	private Bike createBike(String modelName, String description)
			throws InputValidationException, NumberOfBikesException {
		Bike addedBike = null;
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.MILLISECOND, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.add(Calendar.DAY_OF_YEAR, +5);
		addedBike = bikeService.addBike(modelName, description, startDate, 200F,
				5);
		return addedBike;
	}

	private Bike createBike(String modelName, String description,
			Calendar startDate)
			throws InputValidationException, NumberOfBikesException {
		Bike addedBike = null;
		addedBike = bikeService.addBike(modelName, description, startDate, 200F,
				5);
		return addedBike;
	}

	private Bike createBike()
			throws InputValidationException, NumberOfBikesException {
		Bike addedBike = null;
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.MILLISECOND, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.add(Calendar.DAY_OF_YEAR, +5);
		addedBike = bikeService.addBike("Model Name", "Bike Description",
				startDate, 200F, 5);
		return addedBike;
	}

	private Bike addBike(Bike bike) {
		DataSource dataSource = DataSourceLocator
				.getDataSource(BIKE_DATA_SOURCE);
		Bike createdBike = null;
		try (Connection connection = dataSource.getConnection()) {
			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(
						Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				createdBike = bikeDao.create(connection, bike);

				/* Commit. */
				connection.commit();
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
		return createdBike;
	}

	private void removeBike(Long bikeId) {
		DataSource dataSource = DataSourceLocator
				.getDataSource(BIKE_DATA_SOURCE);
		try (Connection connection = dataSource.getConnection()) {
			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(
						Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				bikeDao.remove(connection, bikeId);

				/* Commit. */
				connection.commit();
			} catch (InstanceNotFoundException e) {
				connection.commit();
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

	private void removeRent(Long rentId) {
		DataSource dataSource = DataSourceLocator
				.getDataSource(BIKE_DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(
						Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				rentDao.remove(connection, rentId);

				/* Commit. */
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw new RuntimeException(e);
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

	@Test
	public void testAddBikeAndFindBike() throws InputValidationException,
			InstanceNotFoundException, NumberOfBikesException {
		Bike addedBike = null;

		try {
			addedBike = createBike();
			Bike foundBike = bikeService.findBike(addedBike.getBikeId());

			assertEquals(addedBike.getBikeId(), foundBike.getBikeId());
		} finally {
			// Clear database
			if (addedBike != null) {
				removeBike(addedBike.getBikeId());
			}
		}
	}

	@Test
	public void testAddInvalidBike() throws NumberOfBikesException {
		Bike addedBike = null;
		boolean exceptionCatched = false;

		try {
			// Check bike model not null
			try {
				addedBike = createBike(null, "Bike Description");
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;

			// Check bike model not empty
			try {
				addedBike = createBike("", "Bike Description");
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;

			// Check bike description not null
			try {
				addedBike = createBike("Bike Model", null);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;

			// Check bike description not empty
			try {
				addedBike = createBike("Bike Model", "");
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;

			// Check bike startDate not null
			try {
				addedBike = createBike("Bike Model", "Bike Description", null);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;

			// Check bike startDate is previous the actual date
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DAY_OF_YEAR, -1);
			try {
				addedBike = createBike("Bike Model", "Bike Description", null);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;

			// Check price is positive
			try {
				addedBike = createBike("Bike Model", "Bike Description", -200F,
						5);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;

			// Check availableNumber is greater than 0
			try {
				addedBike = createBike("Bike Model", "Bike Description", 200F,
						-1);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;

		} finally {
			// Clear Database
			if (addedBike != null) {
				removeBike(addedBike.getBikeId());
			}
		}
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentBike()
			throws InstanceNotFoundException, InputValidationException {
		bikeService.findBike(NON_EXISTENT_BIKE_ID);
	}

	@Test
	public void testUpdateBike()
			throws InputValidationException, InstanceNotFoundException,
			UpdateReservedBikeException, NumberOfBikesException {
		Bike bike = createBike();
		try {
			Calendar startDate = Calendar.getInstance();
			startDate.set(Calendar.MILLISECOND, 0);
			startDate.set(Calendar.SECOND, 0);
			startDate.add(Calendar.DAY_OF_YEAR, +10);
			bikeService.updateBike(bike.getBikeId(), "New Model 1",
					"New Description", startDate, 250F, 100);

			Bike updatedBike = bikeService.findBike(bike.getBikeId());
			assertNotEquals(bike, updatedBike);
		} finally {
			// Clear Database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testUpdateInvalidBike()
			throws InputValidationException, InstanceNotFoundException,
			UpdateReservedBikeException, NumberOfBikesException {
		Bike bike = createBike();
		try {
			// Check bike model not null
			Calendar startDate = Calendar.getInstance();
			startDate.set(Calendar.MILLISECOND, 0);
			startDate.set(Calendar.SECOND, 0);
			startDate.add(Calendar.DAY_OF_YEAR, +10);
			bikeService.updateBike(bike.getBikeId(), null, "New Description",
					startDate, 250F, 100);
		} finally {
			// Clear Database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateNonExistentBike()
			throws InputValidationException, InstanceNotFoundException,
			UpdateReservedBikeException, NumberOfBikesException {
		Bike bike = getValidBike(); // Obtengo una bici pero no la añado
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.MILLISECOND, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.add(Calendar.DAY_OF_YEAR, +10);
		bikeService.updateBike(bike.getBikeId(), null, "New Description",
				startDate, 250F, 100);
	}

	@Test(expected = UpdateReservedBikeException.class)
	public void testUpdateReservedBikeFutureDate()
			throws InputValidationException, InstanceNotFoundException,
			NumberOfBikesException, InvalidRentPeriodException,
			UpdateReservedBikeException {
		Bike bike = null;
		Long rent = null;

		try {
			bike = createBike();
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, 5);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 10);

			rent = bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, 3);
			// Update reserved bike
			Calendar startDate = bike.getStartDate();
			startDate.add(Calendar.DAY_OF_YEAR, +5);
			bikeService.updateBike(bike.getBikeId(), bike.getModelName(),
					bike.getDescription(), startDate, bike.getPrice(),
					bike.getAvailableNumber());

		} finally {
			// Clear Database
			if (rent != null) {
				removeRent(rent);
			}
			removeBike(bike.getBikeId());
		}
	}

	@Test
	public void testUpdateReservedBikePreviuousDate()
			throws InputValidationException, InstanceNotFoundException,
			NumberOfBikesException, InvalidRentPeriodException,
			UpdateReservedBikeException {
		Bike bike = null;
		Long rent = null;

		try {
			bike = createBike();
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, 5);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 10);

			rent = bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, 3);
			// Update reserved bike
			Calendar startDate = bike.getStartDate();
			startDate.add(Calendar.DAY_OF_YEAR, -5);
			bikeService.updateBike(bike.getBikeId(), bike.getModelName(),
					bike.getDescription(), startDate, bike.getPrice(),
					bike.getAvailableNumber());

		} finally {
			// Clear Database
			if (rent != null) {
				removeRent(rent);
			}
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testUpdateReservedBikePreviuousDateToAdquisitionDate()
			throws InputValidationException, InstanceNotFoundException,
			NumberOfBikesException, InvalidRentPeriodException,
			UpdateReservedBikeException {
		Bike bike = null;
		Long rent = null;

		try {
			bike = createBike();
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, 5);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 10);

			rent = bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, 3);
			// Update reserved bike
			Calendar startDate = bike.getStartDate();
			startDate.add(Calendar.DAY_OF_YEAR, -20);
			bikeService.updateBike(bike.getBikeId(), bike.getModelName(),
					bike.getDescription(), startDate, bike.getPrice(),
					bike.getAvailableNumber());

		} finally {
			// Clear Database
			if (rent != null) {
				removeRent(rent);
			}
			removeBike(bike.getBikeId());
		}
	}

	@Test
	public void testFindBikes() throws NumberOfBikesException {

		List<Bike> bikes = new LinkedList<Bike>();
		try {
			// Add bikes
			Bike bike1 = createBike("bike 1", "Bike Description");
			bikes.add(bike1);
			Bike bike2 = createBike("bike 2", "Bike Description");
			bikes.add(bike2);
			Bike bike3 = createBike("bike 3", "Bike Description");
			bikes.add(bike3);
			List<Bike> foundBikes = bikeService.findBikes("biKe", null);
			assertEquals(bikes, foundBikes);

		} catch (InputValidationException e) {
			e.printStackTrace();
		} finally {
			// Clear Database
			for (Bike bike : bikes) {
				removeBike(bike.getBikeId());
			}
		}
	}

	@Test
	public void testFindBikesWithDate() throws NumberOfBikesException {

		List<Bike> bikes = new LinkedList<Bike>();
		Bike bike3 = null;
		try {
			// Add bikes
			Bike bike1 = createBike("bike 1", "Bike Description");
			bikes.add(bike1);
			Bike bike2 = createBike("bike 2", "Bike Description");
			bikes.add(bike2);
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.add(Calendar.DAY_OF_YEAR, +10);
			bike3 = createBike("bike 3", "Bike Description", calendar);

			calendar = Calendar.getInstance();
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.add(Calendar.DAY_OF_YEAR, +5);
			List<Bike> foundBikes = bikeService.findBikes("biKe", calendar);
			assertEquals(bikes, foundBikes);

		} catch (InputValidationException e) {
			e.printStackTrace();
		} finally {
			// Clear Database
			for (Bike bike : bikes) {
				removeBike(bike.getBikeId());
			}
			removeBike(bike3.getBikeId());
		}
	}

	@Test
	public void testRentBikeAndFindRent()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriodException, InstanceNotFoundException {

		Bike bike = null;
		Long rent = null;

		try {
			bike = createBike();
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, 5);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 10);

			rent = bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, 3);
			// Find rent
			List<Rent> foundRents = bikeService.findRents(USER_EMAIL);

			assertEquals(rent, foundRents.get(0).getRentId());
			assertEquals(3, foundRents.get(0).getNumberOfBikes());
			assertEquals(bike.getBikeId(), foundRents.get(0).getBikeId());
			assertEquals(startRentDate, foundRents.get(0).getStartRentDate());
			assertEquals(finishRentDate, foundRents.get(0).getFinishRentDate());

		} finally {
			// Clear Database
			if (rent != null) {
				removeRent(rent);
			}
			removeBike(bike.getBikeId());
		}
	}

	@Test
	public void testFindRents()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriodException, InstanceNotFoundException {
		Bike bike = null;
		Long rent = null;
		List<Long> foundIdRents = new LinkedList<Long>();

		try {
			bike = createBike();
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, 5);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 10);

			rent = bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, 3);
			foundIdRents.add(rent);

			rent = bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, 2);
			foundIdRents.add(rent);

			// Find rent
			bikeService.findRents(USER_EMAIL);
			int i = 0;
			for (Long id : foundIdRents) {
				assertEquals(id,
						bikeService.findRents(USER_EMAIL).get(i).getRentId());
				i++;
			}

		} finally {
			// Clear Database
			if (rent != null) {
				removeRent(rent);
			}
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testRentBikeWithInvalidEmail()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriodException, InstanceNotFoundException {
		Bike bike = null;
		try {
			bike = createBike();
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, 5);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 10);
			bikeService.rentBike("", VALID_CREDIT_CARD_NUMBER, bike.getBikeId(),
					startRentDate, finishRentDate, 5);
		} finally {
			// Clear database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testRentBikeWithInvalidCreditCard()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriodException, InstanceNotFoundException {
		Bike bike = null;
		try {
			bike = createBike();
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, 5);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 10);
			bikeService.rentBike(USER_EMAIL, INVALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, 4);
		} finally {
			// Clear database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testRentNonExistentBike()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriodException, InstanceNotFoundException {
		Bike bike = null;
		try {
			bike = createBike();
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, 5);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 10);
			bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					NON_EXISTENT_BIKE_ID, startRentDate, finishRentDate, 4);
		} finally {
			// Clear database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testRentWithInvalidDate()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriodException, InstanceNotFoundException {
		Bike bike = null;
		try {
			bike = createBike();
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 10);
			bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, 5);
		} finally {
			// Clear database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InvalidRentPeriodException.class)
	public void testRentWithInvalidPeriod()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriodException, InstanceNotFoundException {
		Bike bike = null;
		try {
			bike = createBike();
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, 5);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 25);
			bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, 4);
		} finally {
			// Clear database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testRentWithInvalidNumberOfBikes()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriodException, InstanceNotFoundException {
		Bike bike = null;
		try {
			bike = createBike();
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, 5);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 10);
			bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, -2);
		} finally {
			// Clear database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testRentWithOverMaxAvailableBikes()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriodException, InstanceNotFoundException {
		Bike bike = null;
		try {
			bike = createBike();
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, 5);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 10);
			bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, 1000);
		} finally {
			// Clear database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testFindRentWithInvalidEmail() throws InputValidationException {
		bikeService.findRents("");
	}

	@Test
	public void testRatedRent() throws InputValidationException,
			NumberOfBikesException, InvalidRentPeriodException,
			InstanceNotFoundException, RentExpirationException {

		Bike bike = null;
		Long rent = null;
		Bike createdBike = null;

		try {
			// Create a bike on DB
			bike = getValidBike();
			bike.setModelName("Previous Bike");
			bike.setAverageScore(-1);
			createdBike = addBike(bike);

			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, -2);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 1);
			finishRentDate.add(Calendar.DAY_OF_YEAR, -1);

			rent = bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					createdBike.getBikeId(), startRentDate, finishRentDate, 3);
			// Rate rent
			bikeService.rateRent(rent, 3);

			// Find bike
			Bike ratedBike = bikeService.findBike(createdBike.getBikeId());
			assertEquals(3, ratedBike.getAverageScore(), 0.01);
		} finally {
			// Clear Database
			if (rent != null) {
				removeRent(rent);
			}
			removeBike(createdBike.getBikeId());
		}
	}

	@Test(expected = RentExpirationException.class)
	public void testRateNotFinishedRent() throws InputValidationException,
			NumberOfBikesException, InvalidRentPeriodException,
			InstanceNotFoundException, RentExpirationException {

		Bike bike = null;
		Long rent = null;
		Bike createdBike = null;

		try {
			// Create a bike on DB
			bike = getValidBike();
			bike.setModelName("Previous Bike");
			createdBike = addBike(bike);

			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, -2);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 1);
			finishRentDate.add(Calendar.DAY_OF_YEAR, +2);

			rent = bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					createdBike.getBikeId(), startRentDate, finishRentDate, 3);
			// Rate rent
			bikeService.rateRent(rent, 3);
		} finally {
			// Clear Database
			if (rent != null) {
				removeRent(rent);
			}
			removeBike(createdBike.getBikeId());
		}
	}

	@Test
	public void testRateRatedRent() throws InputValidationException,
			NumberOfBikesException, InvalidRentPeriodException,
			InstanceNotFoundException, RentExpirationException {

		Bike bike = null;
		Long rent = null;
		Bike createdBike = null;

		try {
			// Create a bike on DB
			bike = getValidBike();
			bike.setModelName("Previous Bike");
			bike.setAverageScore(-1);
			createdBike = addBike(bike);

			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, -2);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 1);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 0);

			rent = bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					createdBike.getBikeId(), startRentDate, finishRentDate, 3);
			// Rate rent
			bikeService.rateRent(rent, 3);
			bikeService.rateRent(rent, 5);

			// Find bike
			Bike ratedBike = bikeService.findBike(createdBike.getBikeId());
			assertEquals(4, ratedBike.getAverageScore(), 0.01);
		} finally {
			// Clear Database
			if (rent != null) {
				removeRent(rent);
			}
			removeBike(createdBike.getBikeId());
		}
	}
}
