package es.udc.ws.bikes.model.rent;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlRentDao {
	// TODO create, find, findByKeyword, update, remove
	public Rent create(Connection connection, Rent rent);

	public Rent find(Connection connection, Long rentID) throws InstanceNotFoundException;

	public List<Rent> findByUser(Connection connection, String userEmail);

	public void update(Connection connection, Rent rent) throws InstanceNotFoundException;

	public void remove(Connection connection, Long rentID) throws InstanceNotFoundException;
}