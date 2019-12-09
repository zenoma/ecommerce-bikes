package es.udc.ws.app.admin.client.service;

import java.util.Calendar;

import es.udc.ws.app.admin.client.service.dto.ClientBikeDto;

public interface ClientBikeService {

	public ClientBikeDto addBike(String modelName, String description,
			Calendar startDate, float price, int availableNumber);
	
	public void updateBike(Long bikeId, String modelName, String description,
			Calendar startDate, float price, int availableNumber);
	
	public ClientBikeDto findBike(Long bikeId);

}