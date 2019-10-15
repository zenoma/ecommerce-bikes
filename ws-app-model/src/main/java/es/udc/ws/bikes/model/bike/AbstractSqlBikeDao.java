package es.udc.ws.bikes.model.bike;

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

	@Override
	public Bike find(Connection connection, String modelName) throws InstanceNotFoundException {
		// Create queryString
		String queryString = "SELECT modelName, description, startDate, price, availableNumber, adquisitionDate, "
				+ " numberOfRents, averageScore FROM Bike WHERE modelName = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			// Fill preparedStatement
			// Precompiled SQL statement
			int i = 1;
			preparedStatement.setString(i++, modelName);

			// Execute query/consulta
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				// Bike.class.getName() -> Nombre de la clase
				throw new InstanceNotFoundException(modelName, Bike.class.getName());
			}

			// Get results from query
			i = 1;
			String modelName1 = resultSet.getString(i++);
			String description = resultSet.getString(i++);
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(resultSet.getTimestamp(i++));
			float price = resultSet.getFloat(i++);
			int availableNumber = resultSet.getInt(i++);
			Calendar adquisitionDate = Calendar.getInstance();
			adquisitionDate.setTime(resultSet.getTimestamp(i++));
			int numberOfRents = resultSet.getInt(i++);
			double averageScore = resultSet.getDouble(i++);

			// Return bike
			return new Bike(modelName1, description, startDate, price, availableNumber, adquisitionDate, numberOfRents,
					averageScore);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<Bike> findByKeywords(Connection connection, String keywords, Calendar searchDate) {

		// Create "queryString"
		// Tokeniza las keywords en un array
		String[] words = keywords != null ? keywords.split(" ") : null;
		String queryString = " SELECT modelName, description, startDate, price, availableNumber, "
				+ "adquisitionDate, numberOfRents, averageScore FROM Bike";
		if (words != null && words.length > 0) {
			queryString += " WHERE";
			for (int i = 0; i < words.length; i++) {
				if (i > 0) {
					queryString += " AND";
				}
				queryString += " LOWER(description) LIKE LOWER(?)";
			}
		} // Si keywords es null o 0, devolvemos todas las tuplas.
		queryString += " ORDER BY modelName";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			if (words != null) {
				// Fill "preparedStatment".
				for (int i = 0; i < words.length; i++) {
					// Se sustituyen las ? por cada keyword con la forma %keyword%
					preparedStatement.setString(i + 1, "%" + words[i] + "%");
				}
			}

			// Execute query.
			ResultSet resultSet = preparedStatement.executeQuery();

			// Read Bikes.
			List<Bike> bikes = new ArrayList<Bike>();

			while (resultSet.next()) {
				int i = 1;
				String modelName = resultSet.getString(i++);
				String description = resultSet.getString(i++);
				Calendar startDate = Calendar.getInstance();
				startDate.setTime(resultSet.getTimestamp(i++));
				float price = resultSet.getFloat(i++);
				int availableNumber = resultSet.getInt(i++);
				Calendar adquisitionDate = Calendar.getInstance();
				adquisitionDate.setTime(resultSet.getTimestamp(i++));
				int numberOfRents = resultSet.getInt(i++);
				double averageScore = resultSet.getDouble(i++);

				Bike bike = new Bike(modelName, description, startDate, price, availableNumber, adquisitionDate,
						numberOfRents, averageScore);

				bikes.add(bike);
			}

			// Return bikes.
			return bikes;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void update(Connection connection, Bike bike) throws InstanceNotFoundException {
		// Create "queryString"
		String queryString = "UPDATE Bike" + "SET description = ?, startDate = ?, price = ?, "
				+ "availableNumber = ?, adquisitionDate = ?, numberOfRents = ?, " + "averageScore = ? WHERE modelName";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			// Fill "preparedStatement"
			int i = 1;
			preparedStatement.setString(i++, bike.getDescription());
			Timestamp timestamp = bike.getStartDate() != null ? new Timestamp(bike.getStartDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, timestamp);
			preparedStatement.setFloat(i++, bike.getPrice());
			preparedStatement.setInt(i++, bike.getAvailableNumber());
			timestamp = bike.getStartDate() != null ? new Timestamp(bike.getAdquisitionDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, timestamp);
			preparedStatement.setInt(i++, bike.getNumberOfRents());
			preparedStatement.setDouble(i++, bike.getAverageScore());

			// Execute query.
			int updatedRows = preparedStatement.executeUpdate();

			if (updatedRows == 0) {
				throw new InstanceNotFoundException(bike.getModelName(), Bike.class.getName());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove(Connection connection, String modelName) throws InstanceNotFoundException {
		// Create "queryString.
		String queryString = "DELETE FROM Bike WHERE " + "modelName = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			// Fill "preparedStatement"
			int i = 1;
			preparedStatement.setString(i++, modelName);

			// Execute query.
			int removedRows = preparedStatement.executeUpdate();

			if (removedRows == 0) {
				throw new InstanceNotFoundException(modelName, Bike.class.getName());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
