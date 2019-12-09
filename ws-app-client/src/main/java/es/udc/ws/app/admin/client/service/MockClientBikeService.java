package es.udc.ws.app.admin.client.service;

import java.util.Calendar;

import es.udc.ws.app.admin.client.service.dto.ClientBikeDto;

public class MockClientBikeService implements ClientBikeService {

	@Override
	public ClientBikeDto addBike(String modelName, String description, Calendar startDate, float price,
			int availableNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateBike(Long bikeId, String modelName, String description, Calendar startDate, float price,
			int availableNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ClientBikeDto findBike(Long bikeId) {
		// TODO Auto-generated method stub
        return null;
        
	}

}
