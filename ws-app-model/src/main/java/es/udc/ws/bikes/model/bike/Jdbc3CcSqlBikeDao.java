package es.udc.ws.bikes.model.bike;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

public class Jdbc3CcSqlBikeDao extends AbstractSqlBikeDao {

	@Override
	public Bike create(Connection connection, Bike bike) {
		// TODO Auto-generated method stub

		// Create queryString
		String queryString = "INSERT INTO Bike" + "(modelName, description, startDate, price, availableNumber, )"
				+ "adquisitionDate, numberOfRents, averageScore)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString,
				Statement.RETURN_GENERATED_KEYS)) {

			// Fill preparedStatement
			int i = 1;
			preparedStatement.setString(i++, bike.getModelName());
			preparedStatement.setString(i++, bike.getDescription());
			// FIXME Calendar startDate
			Timestamp timeStamp = bike.getStartDate() != null ? 
					new Timestamp(bike.getStartDate().getTime().getTime()) : null;
			preparedStatement.setTimestamp(i++, timeStamp);
			preparedStatement.setFloat(i++, bike.getPrice());
			preparedStatement.setInt(i++, bike.getAvailableNumber());
			// FIXME Calendar AdquisitionDate
			timeStamp = bike.getAdquisitionDate() != null ? 
					new Timestamp(bike.getAdquisitionDate().getTime().getTime()) : null;
			preparedStatement.setTimestamp(i++, timeStamp);
			preparedStatement.setInt(i++, bike.getNumberOfRents());
			preparedStatement.setDouble(i++, bike.getAverageScore());

			// Execute query
			preparedStatement.executeUpdate();

			// Return bike
			return new Bike(bike.getModelName(), bike.getDescription(), bike.getStartDate(), bike.getPrice(),
					bike.getAvailableNumber(), bike.getAdquisitionDate(), bike.getNumberOfRents(),
					bike.getAverageScore());

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
