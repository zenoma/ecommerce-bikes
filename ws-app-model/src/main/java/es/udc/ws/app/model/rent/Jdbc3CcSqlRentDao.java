package es.udc.ws.app.model.rent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

public class Jdbc3CcSqlRentDao extends AbstractSqlRentDao {

	@Override
	public Rent create(Connection connection, Rent rent) {
		// Create queryString
		String queryString = "INSERT INTO Rent"
				+ "(userEmail, bikeId, creditCard, startRentDate, "
				+ "finishRentDate, numberOfBikes, rentDate, price)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				queryString, Statement.RETURN_GENERATED_KEYS)) {
			// Fill preparedStatement
			int i = 1;
			preparedStatement.setString(i++, rent.getUserEmail());
			preparedStatement.setLong(i++, rent.getBikeId());
			preparedStatement.setString(i++, rent.getCreditCard());
			Timestamp timeStamp = rent.getStartRentDate() != null
					? new Timestamp(rent.getStartRentDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, timeStamp);
			timeStamp = rent.getFinishRentDate() != null
					? new Timestamp(
							rent.getFinishRentDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, timeStamp);
			preparedStatement.setInt(i++, rent.getNumberOfBikes());
			timeStamp = rent.getRentDate() != null
					? new Timestamp(rent.getRentDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, timeStamp);
			preparedStatement.setFloat(i++, rent.getPrice());

			// execute query
			preparedStatement.executeUpdate();

			ResultSet resultSet = preparedStatement.getGeneratedKeys();

			if (!resultSet.next()) {
				throw new SQLException(
						"JDBC driver did not return generated key.");
			}
			Long rentId = resultSet.getLong(1);
			return new Rent(rentId, rent.getUserEmail(), rent.getBikeId(),
					rent.getCreditCard(), rent.getStartRentDate(),
					rent.getFinishRentDate(), rent.getNumberOfBikes(),
					rent.getRentDate(), rent.getPrice());

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
