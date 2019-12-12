package es.udc.ws.app.model.bike;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlBikeDao implements SqlBikeDao {

	protected AbstractSqlBikeDao() {
	}

	@Override
	public Bike find(Connection connection, Long bikeId)
			throws InstanceNotFoundException {

		// Create queryString
		String queryString = "SELECT modelName, description, startDate, price, "
				+ "availableNumber, adquisitionDate, numberOfRents, totalScore, numberOfScores "
				+ "FROM Bike WHERE bikeId = ?";

		try (PreparedStatement preparedStatement = connection
				.prepareStatement(queryString)) {

			// Fill preparedStatement
			// Precompiled SQL statement
			int i = 1;
			preparedStatement.setLong(i++, bikeId);

			// Execute query/consulta
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				// Bike.class.getName() -> Nombre de la clase
				throw new InstanceNotFoundException(bikeId,
						Bike.class.getName());
			}

			// Get results from query
			i = 1;
			String modelName = resultSet.getString(i++);
			String description = resultSet.getString(i++);
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(resultSet.getTimestamp(i++));
			float price = resultSet.getFloat(i++);
			int availableNumber = resultSet.getInt(i++);
			Calendar adquisitionDate = Calendar.getInstance();
			adquisitionDate.setTime(resultSet.getTimestamp(i++));
			int numberOfRents = resultSet.getInt(i++);
			double totalScore = resultSet.getDouble(i++);
			int numberOfScores = resultSet.getInt(i++);

			// Return bike
			return new Bike(bikeId, modelName, description, startDate, price,
					availableNumber, adquisitionDate, numberOfRents,
					totalScore, numberOfScores);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<Bike> findByKeywords(Connection connection, String keywords,
			Calendar searchDate) {

		// Create "queryString"
		// Tokeniza las keywords en un array
		String[] words = keywords != null ? keywords.split(" ") : null;
		String queryString = " SELECT bikeId, modelName, description, startDate, "
				+ "price, availableNumber, adquisitionDate, numberOfRents, totalScore, numberOfScores "
				+ "FROM Bike";
		if (words != null && words.length > 0) {
			queryString += " WHERE";
			for (int i = 0; i < words.length; i++) {
				if (i > 0) {
					queryString += " AND";
				}
				queryString += " LOWER(description) LIKE LOWER(?)";
			}
		} // Si keywords es null o 0, devolvemos todas las tuplas.

		if (searchDate != null) {
			if (words != null)
				queryString += " AND";
			else
				queryString += " WHERE";
			queryString += " startDate >= (?)";
		}
		queryString += " ORDER BY modelName";

		try (PreparedStatement preparedStatement = connection
				.prepareStatement(queryString)) {
			int i = 0;
			if (words != null) {
				// Fill "preparedStatment".
				for (i = 0; i < words.length; i++) {
					// Se sustituyen las ? por cada keyword con la forma
					// %keyword%
					preparedStatement.setString(i + 1, "%" + words[i] + "%");
				}
			}
			if (searchDate != null) {
				Timestamp timestamp = new Timestamp(
						searchDate.getTimeInMillis());
				preparedStatement.setTimestamp(++i, timestamp);
			}
			// Execute query.
			ResultSet resultSet = preparedStatement.executeQuery();

			// Read Bikes.
			List<Bike> bikes = new ArrayList<Bike>();

			while (resultSet.next()) {
				i = 1;
				Long bikeId = resultSet.getLong(i++);
				String modelName = resultSet.getString(i++);
				String description = resultSet.getString(i++);
				Calendar startDate = Calendar.getInstance();
				startDate.setTime(resultSet.getTimestamp(i++));
				float price = resultSet.getFloat(i++);
				int availableNumber = resultSet.getInt(i++);
				Calendar adquisitionDate = Calendar.getInstance();
				adquisitionDate.setTime(resultSet.getTimestamp(i++));
				int numberOfRents = resultSet.getInt(i++);
				double totalScore = resultSet.getDouble(i++);
				int numberOfScores = resultSet.getInt(i++);

				Bike bike = new Bike(bikeId, modelName, description, startDate,
						price, availableNumber, adquisitionDate, numberOfRents,
						totalScore, numberOfScores);

				bikes.add(bike);
			}

			// Return bikes.
			return bikes;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void update(Connection connection, Bike bike)
			throws InstanceNotFoundException {
		// Create "queryString"
		String queryString = "UPDATE Bike"
				+ " SET modelName = ?, description = ?, startDate = ?, price = ?, "
				+ "availableNumber = ?, numberOfRents = ?, "
				+ "totalScore = ?, numberOfScores = ? WHERE bikeId = ?";

		try (PreparedStatement preparedStatement = connection
				.prepareStatement(queryString)) {

			// Fill "preparedStatement"
			int i = 1;
			preparedStatement.setString(i++, bike.getModelName());
			preparedStatement.setString(i++, bike.getDescription());
			Timestamp timestamp = bike.getStartDate() != null
					? new Timestamp(bike.getStartDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, timestamp);
			preparedStatement.setFloat(i++, bike.getPrice());
			preparedStatement.setInt(i++, bike.getAvailableNumber());
			preparedStatement.setInt(i++, bike.getNumberOfRents());
			preparedStatement.setDouble(i++, bike.getTotalScore());
			preparedStatement.setInt(i++, bike.getNumberOfScores());
			preparedStatement.setLong(i++, bike.getBikeId());

			// Execute query.
			int updatedRows = preparedStatement.executeUpdate();

			if (updatedRows == 0) {
				throw new InstanceNotFoundException(bike.getBikeId(),
						Bike.class.getName());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove(Connection connection, Long bikeId)
			throws InstanceNotFoundException {
		// Create "queryString.
		String queryString = "DELETE FROM Bike WHERE " + "bikeId = ?";

		try (PreparedStatement preparedStatement = connection
				.prepareStatement(queryString)) {

			// Fill "preparedStatement"
			int i = 1;
			preparedStatement.setLong(i++, bikeId);

			// Execute query.
			int removedRows = preparedStatement.executeUpdate();

			if (removedRows == 0) {
				throw new InstanceNotFoundException(bikeId,
						Bike.class.getName());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
