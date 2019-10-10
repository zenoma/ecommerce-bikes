package es.udc.ws.bikes.model.bike;

import java.sql.Connection;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlBikeDao {
	// TODO find, findByKeyword, update, remove	

	public Bike find(Connection connection, String modelName)
            throws InstanceNotFoundException {
		
		//Create queryString
		String queryString = "SELECT modelName, description, " + 
				"startDate, price, availableNumber, adquisitionDate" + 
				"FROM Bike WHERE modelName = ?";
		
		return null;
	}
}
