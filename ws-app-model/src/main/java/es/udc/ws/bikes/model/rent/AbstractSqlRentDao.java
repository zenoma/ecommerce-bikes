package es.udc.ws.bikes.model.rent;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlRentDao implements SqlRentDao {

	@Override
	public Rent find(Connection connection, Long rentID) throws InstanceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Rent> findByUser(Connection connection, String userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Connection connection, Rent rent) throws InstanceNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Connection connection, Long rentID) throws InstanceNotFoundException {
		// TODO Auto-generated method stub

	}

}
