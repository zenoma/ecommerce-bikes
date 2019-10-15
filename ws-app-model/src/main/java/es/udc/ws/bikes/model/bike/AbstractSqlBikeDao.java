package es.udc.ws.bikes.model.bike;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlBikeDao implements SqlBikeDao {
	// TODO find, findByKeyword, update, remove

	@Override
	public Bike find(Connection connection, String modelName) throws InstanceNotFoundException {
		// Create queryString
		String queryString = "SELECT modelName, description, " + "startDate, price, availableNumber, adquisitionDate"
				+ "FROM Bike WHERE modelName = ?";
		return null;
	}

	@Override
	public List<Bike> findByKeywords(Connection connection, String keywords, Calendar searchDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Connection connection, Bike bike) throws InstanceNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Connection connection, String modelName) throws InstanceNotFoundException {
		// TODO Auto-generated method stub

	}

}
