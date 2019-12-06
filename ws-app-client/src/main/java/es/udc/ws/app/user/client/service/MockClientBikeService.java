package es.udc.ws.app.user.client.service;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.user.client.service.dto.ClientBikeDto;
import es.udc.ws.app.user.client.service.dto.ClientRentDto;
import es.udc.ws.util.exceptions.InputValidationException;

public class MockClientBikeService implements ClientBikeService{

	@Override
	public List<ClientBikeDto> findBikes(String keywords, Calendar calendar) {
		// FIXME Devolver lista 
		return null;
	}

	@Override
	public Long rentBike(String email, Long creditCard, Long bikeId,
			Calendar startRentDate, Calendar finishRentDate,
			int numberOfBikes) {
		return (long) 0;
	}

	@Override
	public List<ClientRentDto> findRents(String email)
			throws InputValidationException {
		// FIXME Devolver lista
		return null;
	}

	@Override
	public void rateRent(Long rentId, int score) {	
	}

}
