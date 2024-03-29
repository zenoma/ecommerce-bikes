package es.udc.ws.app.model.rent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.model.bike.Bike;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlRentDao implements SqlRentDao {

	@Override
	public Rent find(Connection connection, Long rentId)
			throws InstanceNotFoundException {
		// Create queryString
		String queryString = "SELECT userEmail, bikeId, creditCard, startRentDate, "
				+ "finishRentDate, numberOfBikes, rentDate, price, rentScore FROM Rent WHERE rentID = ?";
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(queryString)) {
			// Fill preparedStatement
			int i = 1;
			preparedStatement.setLong(i++, rentId);

			// Execute query/consulta
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				throw new InstanceNotFoundException(rentId,
						Rent.class.getName());
			}

			// Get results from query
			i = 1;
			String userEmail = resultSet.getString(i++);
			Long bikeId = resultSet.getLong(i++);
			String creditCard = resultSet.getString(i++);
			Calendar startRentDate = Calendar.getInstance();
			startRentDate.setTime(resultSet.getTimestamp(i++));
			Calendar finishRentDate = Calendar.getInstance();
			finishRentDate.setTime(resultSet.getTimestamp(i++));
			int numberOfBikes = resultSet.getInt(i++);
			Calendar rentDate = Calendar.getInstance();
			rentDate.setTime(resultSet.getTimestamp(i++));
			Float price = resultSet.getFloat(i++);
			int rentScore = resultSet.getInt(i++);

			// Return Rent
			return new Rent(rentId, userEmail, bikeId, creditCard,
					startRentDate, finishRentDate, numberOfBikes, rentDate,
					price, rentScore);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Rent> findByUser(Connection connection, String userEmail) {
		// Create "queryString"
		String queryString = "SELECT rentID, bikeId, creditCard, startRentDate, "
				+ "finishRentDate, numberOfBikes, rentDate, price, rentScore FROM Rent WHERE userEmail=?";

		try (PreparedStatement preparedStatement = connection
				.prepareStatement(queryString)) {
			int i = 1;
			preparedStatement.setString(i++, userEmail);

			// Execute query/consulta
			ResultSet resultSet = preparedStatement.executeQuery();

			List<Rent> rents = new ArrayList<Rent>();
			while (resultSet.next()) {
				i = 1;
				Long rentId = resultSet.getLong(i++);
				Long bikeId = resultSet.getLong(i++);
				String creditCard = resultSet.getString(i++);
				Calendar startRentDate = Calendar.getInstance();
				startRentDate.setTime(resultSet.getTimestamp(i++));
				Calendar finishRentDate = Calendar.getInstance();
				finishRentDate.setTime(resultSet.getTimestamp(i++));
				int numberOfBikes = resultSet.getInt(i++);
				Calendar rentDate = Calendar.getInstance();
				rentDate.setTime(resultSet.getTimestamp(i++));
				Float price = resultSet.getFloat(i++);
				int rentScore = resultSet.getInt(i++);

				Rent rent = new Rent(rentId, userEmail, bikeId, creditCard,
						startRentDate, finishRentDate, numberOfBikes, rentDate,
						price, rentScore);
				rents.add(rent);
			}
			return rents;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Connection connection, Rent rent)
			throws InstanceNotFoundException {
		// Create "queryString"
		String queryString = "UPDATE Rent"
				+ " SET userEmail = ?, bikeId = ?, creditCard = ?, "
				+ "startRentDate = ?, finishRentDate = ?, numberOfBikes = ?, rentDate = ?, price = ?, rentScore = ? "
				+ "WHERE rentId = ?";

		try (PreparedStatement preparedStatement = connection
				.prepareStatement(queryString)) {
			// Fill "preparedStatement"
			int i = 1;
			preparedStatement.setString(i++, rent.getUserEmail());
			preparedStatement.setLong(i++, rent.getBikeId());
			preparedStatement.setString(i++, rent.getCreditCard());
			Timestamp timestamp = rent.getStartRentDate() != null
					? new Timestamp(rent.getStartRentDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, timestamp);
			timestamp = rent.getFinishRentDate() != null
					? new Timestamp(
							rent.getFinishRentDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, timestamp);
			preparedStatement.setInt(i++, rent.getNumberOfBikes());
			timestamp = rent.getRentDate() != null
					? new Timestamp(rent.getRentDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, timestamp);
			preparedStatement.setFloat(i++, rent.getPrice());
			preparedStatement.setInt(i++, rent.getRentScore());
			preparedStatement.setLong(i++, rent.getRentId());

			// Execute query.
			int updatedRows = preparedStatement.executeUpdate();

			if (updatedRows == 0) {
				throw new InstanceNotFoundException(rent.getRentId(),
						Bike.class.getName());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove(Connection connection, Long rentId)
			throws InstanceNotFoundException {
		// Create "queryString.
		String queryString = "DELETE FROM Rent WHERE " + "rentId = ?";
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(queryString)) {
			int i = 1;
			preparedStatement.setLong(i++, rentId);

			// Execute query.
			int removedRows = preparedStatement.executeUpdate();

			if (removedRows == 0) {
				throw new InstanceNotFoundException(rentId,
						Rent.class.getName());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

}
