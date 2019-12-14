package es.udc.ws.app.user.client.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

import javax.xml.stream.events.StartDocument;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.ws.app.user.client.service.rest.json.JsonClientBikeDtoConversor;
import es.udc.ws.app.user.client.service.rest.json.JsonClientExceptionConversor;
import es.udc.ws.app.user.client.service.rest.json.JsonClientRentDtoConversor;
import es.udc.ws.app.user.client.service.ClientBikeService;
import es.udc.ws.app.user.client.service.dto.ClientBikeDto;
import es.udc.ws.app.user.client.service.dto.ClientRentDto;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
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
							+ "&date=" + URLEncoder.encode(
									calendarToString(calendar), "UTF-8"))
					.execute().returnResponse();
			validateStatusCode(HttpStatus.SC_OK, response);

			return JsonClientBikeDtoConversor
					.toClientBikeDtos((response.getEntity().getContent()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Long rentBike(String email, String creditCard, Long bikeId,
			Calendar startRentDate, Calendar finishRentDate,
			int numberOfBikes) {
		try {
			ClientRentDto rentDto = new ClientRentDto(email, bikeId, creditCard,
					startRentDate, finishRentDate, numberOfBikes);
			HttpResponse response = Request.Post(getEndpointAddress() + "rents")
					.bodyStream(toInputStream(rentDto),
							ContentType.create("application/json"))
					.execute().returnResponse();
			validateStatusCode(HttpStatus.SC_CREATED, response);
			return JsonClientRentDtoConversor
					.toClientRentDto(response.getEntity().getContent())
					.getRentId();
		} catch (InputValidationException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		} catch (InstanceNotFoundException e) {
			System.out.println("ERROR:");
			System.out.println(e.getMessage());
			System.exit(-1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return -1L;
	}

	@Override
	public List<ClientRentDto> findRents(String email)
			throws InputValidationException {
		try {
			HttpResponse response = Request
					.Get(getEndpointAddress() + "rents?userEmail="
							+ URLEncoder.encode(email.toString(), "UTF-8"))
					.execute().returnResponse();
			validateStatusCode(HttpStatus.SC_OK, response);

			return JsonClientRentDtoConversor
					.toClientRentDtos((response.getEntity().getContent()));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void rateRent(Long rentId, int score) {
		try {
			HttpResponse response = Request
					.Put(getEndpointAddress() + "rents/"
							+ URLEncoder.encode(rentId.toString(), "UTF-8")
							+ "?score="
							+ URLEncoder.encode(
									Integer.valueOf(score).toString(), "UTF-8"))
					.execute().returnResponse();

			validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

		} catch (InputValidationException e) {
			System.out.println(e);
			;
		} catch (InstanceNotFoundException e) {
			System.out.println("ERROR:");
			System.out.println("Rent not found. Id = " + rentId);
			System.exit(-1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

	private InputStream toInputStream(ClientRentDto rentDto) {

		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			objectMapper.writer(new DefaultPrettyPrinter()).writeValue(
					outputStream,
					JsonClientRentDtoConversor.toJsonObject(rentDto));
			return new ByteArrayInputStream(outputStream.toByteArray());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String calendarToString(Calendar calendar) {
		return (calendar.get(Calendar.DAY_OF_MONTH) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.YEAR));
	}

}