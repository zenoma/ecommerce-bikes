package es.udc.ws.bikes.model.bike;

import java.sql.Connection;

public class Jdbc3CcSqlBikeDao extends AbstractSqlBikeDao {

	@Override
	public Bike create(Connection connection, Bike bike) {
		// TODO Auto-generated method stub
		
		//Create queryString
		String queryString = "INSERT INTO Bike"
				+ "(modelName, description, startDate, price, availableNumber, )"
				+ "adquisitionDate, numberOfRents, averageScore)" 
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		return null;
	}

}