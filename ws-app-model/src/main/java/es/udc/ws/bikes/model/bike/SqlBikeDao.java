package es.udc.ws.bikes.model.bike;

import java.sql.Connection;
import java.util.List;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlBikeDao {
	// TODO create, find, findByKeyword, update, remove

	public Bike create(Connection connection, Bike bike);

    public Bike find(Connection connection, String modelName)
            throws InstanceNotFoundException;

    public List<Bike> findByKeywords(Connection connection,
            String keywords);

    public void update(Connection connection, Bike bike)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long movieId)
            throws InstanceNotFoundException;
}
