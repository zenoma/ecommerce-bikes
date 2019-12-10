package es.udc.ws.app.model.bike;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlBikeDao extends AbstractSqlBikeDao {

	@Override
	public Bike create(Connection connection, Bike bike) {
		// Create queryStrin
		String queryString = "INSERT INTO Bike"
				+ "(modelName, description, startDate, price, availableNumber, "
				+ "adquisitionDate, numberOfRents, averageScore)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				queryString, Statement.RETURN_GENERATED_KEYS)) {

			// Fill preparedStatement
			int i = 1;
			preparedStatement.setString(i++, bike.getModelName());
			preparedStatement.setString(i++, bike.getDescription());
			Timestamp timeStamp = bike.getStartDate() != null
					? new Timestamp(bike.getStartDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, timeStamp);
			preparedStatement.setFloat(i++, bike.getPrice());
			preparedStatement.setInt(i++, bike.getAvailableNumber());
			timeStamp = bike.getAdquisitionDate() != null
					? new Timestamp(
							bike.getAdquisitionDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, timeStamp);
			preparedStatement.setInt(i++, bike.getNumberOfRents());
			preparedStatement.setDouble(i++, bike.getAverageScore());

			// Execute query
			preparedStatement.executeUpdate();

			/* Get generated identifier. */
			ResultSet resultSet = preparedStatement.getGeneratedKeys();

			if (!resultSet.next()) {
				throw new SQLException(
						"JDBC driver did not return generated key.");
			}
			Long bikeId = resultSet.getLong(1);

			// Return bike
			return new Bike(bikeId, bike.getModelName(), bike.getDescription(),
					bike.getStartDate(), bike.getPrice(),
					bike.getAvailableNumber(), bike.getAdquisitionDate(),
					bike.getNumberOfRents(), bike.getAverageScore());

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
