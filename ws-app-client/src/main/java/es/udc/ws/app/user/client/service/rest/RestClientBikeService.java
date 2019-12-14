package es.udc.ws.app.user.client.service.rest;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

import es.udc.ws.app.user.client.service.rest.json.JsonClientBikeDtoConversor;
import es.udc.ws.app.user.client.service.rest.json.JsonClientExceptionConversor;
import es.udc.ws.app.user.client.service.ClientBikeService;
import es.udc.ws.app.user.client.service.dto.ClientBikeDto;
import es.udc.ws.app.user.client.service.dto.ClientRentDto;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.exceptions.ParsingException;

public class RestClientBikeService implements ClientBikeService {

	private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientBikeService.endpointAddress";
	private String endpointAddress;

	@Override
	public List<ClientBikeDto> findBikes(String keywords, Calendar calendar) {
		try {
			HttpResponse response = Request
					.Get(getEndpointAddress() + "bikes?keywords="
							+ URLEncoder.encode(keywords.toString(), "UTF-8")
							+ "&date="
							+ URLEncoder.encode(calendar.toString(), "UTF-8"))
					.execute().returnResponse();

			validateStatusCode(HttpStatus.SC_OK, response);

			return JsonClientBikeDtoConversor
					.toClientBikeDtos((response.getEntity().getContent()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Long rentBike(String email, Long creditCard, Long bikeId,
			Calendar startRentDate, Calendar finishRentDate,
			int numberOfBikes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClientRentDto> findRents(String email)
			throws InputValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rateRent(Long rentId, int score) {
		// TODO Auto-generated method stub

	}

	private synchronized String getEndpointAddress() {
		if (endpointAddress == null) {
			endpointAddress = ConfigurationParametersManager
					.getParameter(ENDPOINT_ADDRESS_PARAMETER);
		}
		return endpointAddress;
	}

	private void validateStatusCode(int successCode, HttpResponse response)
			throws InstanceNotFoundException, InputValidationException,
			ParsingException {

		try {

			int statusCode = response.getStatusLine().getStatusCode();

			/* Success? */
			if (statusCode == successCode) {
				return;
			}

			/* Handler error. */
			switch (statusCode) {

			case HttpStatus.SC_NOT_FOUND:
				throw JsonClientExceptionConversor
						.fromInstanceNotFoundException(
								response.getEntity().getContent());

			case HttpStatus.SC_BAD_REQUEST:
				throw JsonClientExceptionConversor.fromInputValidationException(
						response.getEntity().getContent());

			default:
				throw new RuntimeException(
						"HTTP error; status code = " + statusCode);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
