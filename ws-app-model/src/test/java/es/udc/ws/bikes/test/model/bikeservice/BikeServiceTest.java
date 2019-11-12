package es.udc.ws.bikes.test.model.bikeservice;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.bikes.model.bike.SqlBikeDao;
import es.udc.ws.bikes.model.bike.SqlBikeDaoFactory;
import es.udc.ws.bikes.model.bikeservice.BikeService;
import es.udc.ws.bikes.model.bikeservice.BikeServiceFactory;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidRentPeriod;
import es.udc.ws.bikes.model.bikeservice.exceptions.NumberOfBikesException;
import es.udc.ws.bikes.model.rent.Rent;
import es.udc.ws.bikes.model.rent.SqlRentDao;
import es.udc.ws.bikes.model.rent.SqlRentDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import static es.udc.ws.bikes.model.util.ModelConstants.BIKE_DATA_SOURCE;
import static org.junit.Assert.assertEquals;
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

	private Bike getValidBike(String modelName) {
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.MILLISECOND, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.add(Calendar.DAY_OF_YEAR, +5);
		return new Bike(modelName, "Bike description", startDate, 199.95F, 5);
	}

	private Bike getValidBike(String modelName, Calendar startDate) {
		return new Bike(modelName, "Bike description", startDate, 199.95F, 5);
	}

	private Bike getValidBike() {
		return getValidBike("Bike model");
	}

	private Bike createBike(Bike bike) {
		Bike addedBike = null;
		try {
			addedBike = bikeService.addBike(bike);
		} catch (InputValidationException e) {
			throw new RuntimeException(e);
		}
		return addedBike;
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
	public void testAddBikeAndFindBike()
			throws InputValidationException, InstanceNotFoundException {
		Bike bike = getValidBike();
		Bike addedBike = null;

		try {
			addedBike = bikeService.addBike(bike);
			Bike foundBike = bikeService.findBike(addedBike.getBikeId());

			assertEquals(addedBike.getBikeId(), foundBike.getBikeId());
		} finally {
			// Clear atabase
			if (addedBike != null) {
				removeBike(addedBike.getBikeId());
			}
		}
	}

	@Test
	public void testAddInvalidBike() {
		Bike bike = getValidBike();
		Bike addedBike = null;
		boolean exceptionCatched = false;

		try {
			// Check bike model not null
			bike.setModelName(null);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;
			bike = getValidBike();

			// Check bike model not empty
			bike.setModelName("");
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;
			bike.setModelName("modelName");

			// Check bike description not null
			bike.setDescription(null);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;
			bike.setDescription("Bike description");

			// Check bike description not empty
			bike.setDescription("");
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;
			bike.setDescription("Bike description");

			// Check bike startDate not null
			bike.setStartDate(null);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;
			Calendar startDate = Calendar.getInstance();
			startDate.add(Calendar.DAY_OF_YEAR, +5);
			bike.setStartDate(startDate);

			// Check bike startDate is past the actual date
			Calendar date = bike.getAdquisitionDate();
			date.add(Calendar.DAY_OF_YEAR, -1);
			bike.setStartDate(date);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;
			date.add(Calendar.DAY_OF_YEAR, 1);
			bike.setStartDate(date);

			// Check price is positive
			bike.setPrice(-1);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;
			bike.setPrice(199.95F);

			// Check availableNumber is greater than 0
			bike.setAvailableNumber(-1);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;
			bike.setAvailableNumber(5);

			// Check adquisitionDate not null
			bike.setAdquisitionDate(null);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;

			bike = getValidBike();

			// Check adquisitionDate is past today
			date = Calendar.getInstance();
			date.add(Calendar.DAY_OF_YEAR, +1);
			bike.setAdquisitionDate(date);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;

			bike = getValidBike();

			// Check numberOfRents greater than 0
			bike.setNumberOfRents(-1);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			exceptionCatched = false;

			bike = getValidBike();

			// Check averageScore greater than 0
			bike.setAverageScore(-1);
			try {
				addedBike = bikeService.addBike(bike);
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
			throws InputValidationException, InstanceNotFoundException {
		Bike bike = createBike(getValidBike());
		try {
			bike.setModelName("new Model Name");
			bikeService.update(bike);

			Bike updatedBike = bikeService.findBike(bike.getBikeId());
			assertEquals(bike, updatedBike);
		} finally {
			// Clear Database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testUpdateInvalidMovie()
			throws InputValidationException, InstanceNotFoundException {
		Bike bike = createBike(getValidBike());
		try {
			// Check bike model not null
			bike = bikeService.findBike(bike.getBikeId());
			bike.setModelName(null);
			bikeService.update(bike);
		} finally {
			// Clear Database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateNonExistentBike()
			throws InputValidationException, InstanceNotFoundException {
		Bike bike = getValidBike();
		bike.setBikeId(NON_EXISTENT_BIKE_ID);
		bike.setAdquisitionDate(Calendar.getInstance());
		bikeService.update(bike);
	}

	@Test
	public void testFindBikes() {

		// Add bikes
		List<Bike> bikes = new LinkedList<Bike>();
		Bike bike1 = createBike(getValidBike("bike 1"));
		bikes.add(bike1);
		Bike bike2 = createBike(getValidBike("bike 2"));
		bikes.add(bike2);
		Bike bike3 = createBike(getValidBike("bike 3"));
		bikes.add(bike3);

		try {
			List<Bike> foundBikes = bikeService.findBikes("biKe", null);
			assertEquals(bikes, foundBikes);

		} finally {
			// Clear Database
			for (Bike bike : bikes) {
				removeBike(bike.getBikeId());
			}
		}
	}

	@Test
	public void testFindBikesWithDate() {

		// Add bikes
		List<Bike> bikes = new LinkedList<Bike>();
		Bike bike1 = createBike(getValidBike("bike 1"));
		bikes.add(bike1);
		Bike bike2 = createBike(getValidBike("bike 2"));
		bikes.add(bike2);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DAY_OF_YEAR, 10);
		Bike bike3 = createBike(getValidBike("bike 3", calendar));

		try {
			calendar = Calendar.getInstance();
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.add(Calendar.DAY_OF_YEAR, 5);
			List<Bike> foundBikes = bikeService.findBikes("biKe", calendar);
			assertEquals(bikes, foundBikes);

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
			InvalidRentPeriod, InstanceNotFoundException {

		Bike bike = createBike(getValidBike());
		Long rent = null;

		try {
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
			InvalidRentPeriod, InstanceNotFoundException {
		Bike bike = createBike(getValidBike());
		Long rent = null;
		List<Long> foundIdRents = new LinkedList<Long>();

		try {
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
			InvalidRentPeriod, InstanceNotFoundException {
		Bike bike = createBike(getValidBike());
		Long rent = null;
		try {
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, 5);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 10);
			rent = bikeService.rentBike("", VALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, 5);
		} finally {
			// Clear database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testRentBikeWithInvalidCreditCard()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriod, InstanceNotFoundException {
		Bike bike = createBike(getValidBike());
		Long rent = null;
		try {
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, 5);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 10);
			rent = bikeService.rentBike(USER_EMAIL, INVALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, 4);
		} finally {
			// Clear database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testRentNonExistentBike()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriod, InstanceNotFoundException {
		Bike bike = createBike(getValidBike());
		Long rent = null;
		try {
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
					NON_EXISTENT_BIKE_ID, startRentDate, finishRentDate, 4);
		} finally {
			// Clear database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testRentWithInvalidDate()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriod, InstanceNotFoundException {
		Bike bike = createBike(getValidBike());
		Long rent = null;
		try {
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 10);
			rent = bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, 5);
		} finally {
			// Clear database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InvalidRentPeriod.class)
	public void testRentWithInvalidPeriod()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriod, InstanceNotFoundException {
		Bike bike = createBike(getValidBike());
		Long rent = null;
		try {
			// Rent bike
			Calendar startRentDate = Calendar.getInstance();
			Calendar finishRentDate = Calendar.getInstance();
			startRentDate.set(Calendar.MILLISECOND, 0);
			startRentDate.set(Calendar.SECOND, 0);
			startRentDate.add(Calendar.DAY_OF_YEAR, 5);
			finishRentDate.set(Calendar.MILLISECOND, 0);
			finishRentDate.set(Calendar.SECOND, 0);
			finishRentDate.add(Calendar.DAY_OF_YEAR, 25);
			rent = bikeService.rentBike(USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					bike.getBikeId(), startRentDate, finishRentDate, 4);
		} finally {
			// Clear database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testRentWithInvalidNumberOfBikes()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriod, InstanceNotFoundException {
		Bike bike = createBike(getValidBike());
		Long rent = null;
		try {
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
					bike.getBikeId(), startRentDate, finishRentDate, -2);
		} finally {
			// Clear database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = NumberOfBikesException.class)
	public void testRentWithOverMaxAvailableBikes()
			throws InputValidationException, NumberOfBikesException,
			InvalidRentPeriod, InstanceNotFoundException {
		Bike bike = createBike(getValidBike());
		Long rent = null;
		try {
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

	// TODO Rate rent

}
