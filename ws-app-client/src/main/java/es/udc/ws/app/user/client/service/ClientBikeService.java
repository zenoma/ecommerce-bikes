package es.udc.ws.app.user.client.service;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.user.client.service.dto.ClientBikeDto;
import es.udc.ws.app.user.client.service.dto.ClientRentDto;
import es.udc.ws.util.exceptions.InputValidationException;

public interface ClientBikeService {

	public List<ClientBikeDto> findBikes(String keywords, Calendar calendar);

	public Long rentBike(String email, String creditCard, Long bikeId,
			Calendar startRentDate, Calendar finishRentDate, int numberOfBikes);

	public List<ClientRentDto> findRents(String email)
			throws InputValidationException;

	public void rateRent(Long rentId, int score, String email);
}
