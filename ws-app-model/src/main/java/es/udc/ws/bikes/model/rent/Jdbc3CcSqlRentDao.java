package es.udc.ws.bikes.model.rent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlRentDao extends AbstractSqlRentDao {

	@Override
	public Rent create(Connection connection, Rent rent) {
		//Create queryString
		String queryString = "INSERT INTO Rent" + "(rentId, userEmail, bikeId, creditCard, startRentDate, "
				+ "finishRentDate, numberOfBikes, rentDate)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		try(PreparedStatement preparedStatement = connection.prepareStatement(queryString, 
				Statement.RETURN_GENERATED_KEYS)){
			//Fill preparedStatement
			int i = 1;
			preparedStatement.setLong(i++, rent.getRentID());
			preparedStatement.setString(i++, rent.getUserEmail());
			preparedStatement.setLong(i++, rent.getBikeId());
			preparedStatement.setLong(i++, rent.getCreditCard());
			Timestamp timeStamp = rent.getStartRentDate() != null ? 
					new Timestamp(rent.getStartRentDate().getTime().getTime()) : null;
			preparedStatement.setTimestamp(i++, timeStamp);
			timeStamp = rent.getFinishRentDate() != null ? 
					new Timestamp(rent.getFinishRentDate().getTime().getTime()) : null;
			preparedStatement.setTimestamp(i++, timeStamp);
			preparedStatement.setInt(i++, rent.getNumberOfBikes());
			timeStamp = rent.getRentDate() != null ? 
					new Timestamp(rent.getRentDate().getTime().getTime()) : null;
			preparedStatement.setTimestamp(i++, timeStamp);
			
			//execute query
			preparedStatement.executeUpdate();
			return new Rent(rent.getUserEmail(), rent.getBikeId(), rent.getCreditCard(),
					rent.getStartRentDate(), rent.getFinishRentDate(), rent.getNumberOfBikes());
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
}
