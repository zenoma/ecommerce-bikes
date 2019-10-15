package es.udc.ws.bikes.model.bike;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlBikeDao implements SqlBikeDao {
	// TODO find, findByKeyword, update, remove

	@Override
	public Bike find(Connection connection, String modelName) throws InstanceNotFoundException {
		// Create queryString
		String queryString = "SELECT modelName, description, startDate, price, availableNumber, adquisitionDate, "
				+ " numberOfRents, scoreAverage FROM Bike WHERE modelName = ?";
		
		String queryString2 = "SELECT * FROM Bike WHERE modelName = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			//Fill preparedStatement
			//Precompiled SQL statement
			int i= 1;
			preparedStatement.setString(i++, modelName);

			//Execute query/consulta
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				//Bike.class.getName() -> Nombre de la clase
				throw new InstanceNotFoundException(modelName, Bike.class.getName());
			}

			//Get results from query
			i=1;
			String modelName1 = resultSet.getString(i++);
			String description = resultSet.getString(i++);
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(resultSet.getTimestamp(i++));
			float price = resultSet.getFloat(i++);
			int availableNumber = resultSet.getInt(i++);
			Calendar adquisitionDate = Calendar.getInstance();
			adquisitionDate.setTime(resultSet.getTimestamp(i++));
			int numberOfRents = resultSet.getInt(i++);
			double scoreAverage = resultSet.getDouble(i++);

			//Return bike
			return new Bike(modelName1, description, startDate, price, availableNumber, 
					adquisitionDate, numberOfRents, scoreAverage);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

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