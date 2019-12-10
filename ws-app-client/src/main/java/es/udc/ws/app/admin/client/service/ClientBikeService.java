package es.udc.ws.app.admin.client.service;

import java.util.Calendar;

import es.udc.ws.app.admin.client.service.dto.ClientBikeDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface ClientBikeService {

	public ClientBikeDto addBike(String modelName, String description,
			Calendar startDate, float price, int availableNumber) throws InputValidationException;
	
	public void updateBike(Long bikeId, String modelName, String description,
			Calendar startDate, float price, int availableNumber) throws InstanceNotFoundException;
	
	public ClientBikeDto findBike(Long bikeId);

}