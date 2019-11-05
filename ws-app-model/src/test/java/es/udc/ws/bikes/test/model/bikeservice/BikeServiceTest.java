package es.udc.ws.bikes.test.model.bikeservice;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.bikes.model.bike.SqlBikeDao;
import es.udc.ws.bikes.model.bike.SqlBikeDaoFactory;
import es.udc.ws.bikes.model.bikeservice.BikeService;
import es.udc.ws.bikes.model.bikeservice.BikeServiceFactory;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidDateException;
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

	private Bike getValidBike() {
		return getValidBike("Bike model");
	}

	private Bike createBike(Bike bike) {
		Bike addedBike = null;
		try {
			addedBike = bikeService.addBike(bike);
		} catch (InputValidationException e) {
			throw new RuntimeException(e);
		} catch (InvalidDateException e) {
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

	@Test
	public void testAddBikeAndFindBike() throws InputValidationException,
			InvalidDateException, InstanceNotFoundException {
		Bike bike = getValidBike();
		Bike addedBike = null;

		try {
			addedBike = bikeService.addBike(bike);
			Bike foundBike = bikeService.findBike(addedBike.getBikeId());

			assertEquals(addedBike.getBikeId(), foundBike.getBikeId());
		} finally {
			// Clear Database
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
			} catch (InvalidDateException e) {

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
			} catch (InvalidDateException e) {

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
			} catch (InvalidDateException e) {

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
			} catch (InvalidDateException e) {

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
			} catch (InvalidDateException e) {
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
			} catch (InvalidDateException e) {
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
			} catch (InvalidDateException e) {
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
			} catch (InvalidDateException e) {
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
			} catch (InvalidDateException e) {
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
			} catch (InvalidDateException e) {
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
			} catch (InvalidDateException e) {
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
			} catch (InvalidDateException e) {
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
	public void testUpdateBike() throws InputValidationException,
			InstanceNotFoundException, InvalidDateException {
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
	public void testUpdateInvalidMovie() throws InputValidationException,
			InstanceNotFoundException, InvalidDateException {
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
	public void testUpdateNonExistentBike() throws InputValidationException,
			InstanceNotFoundException, InvalidDateException {
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
	public void testRentBikeAndFindRent() {
		
	}
	
	@Test
	public void testRentBikeWithInvalidEmail() {
		
	}
	
	@Test
	public void testRentNonExistentMovie() {
		
	}
	
	@Test
	public void testFindNonExistentRent() {
		
	}
	
}
