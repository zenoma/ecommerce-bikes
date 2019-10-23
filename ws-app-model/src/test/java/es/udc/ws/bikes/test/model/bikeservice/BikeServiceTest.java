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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

public class BikeServiceTest {
	private static BikeService bikeService = null;

	private static SqlBikeDao bikeDao = null;
	
	private static SqlRentDao rentDao = null;


	@BeforeClass
	public static void init() {
		/*
		 * Create a simple data source and add it to "DataSourceLocator" (this is needed
		 * to test "es.udc.ws.bikes.model.bikeservice.BikeService"
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
		startDate.add(Calendar.DAY_OF_MONTH, 5);
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
		DataSource dataSource = DataSourceLocator.getDataSource(BIKE_DATA_SOURCE);
		try (Connection connection = dataSource.getConnection()) {
			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
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
	public void testAddBike() throws InputValidationException, InvalidDateException, InstanceNotFoundException {
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
}
