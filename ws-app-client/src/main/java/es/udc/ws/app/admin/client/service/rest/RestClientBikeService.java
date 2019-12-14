package es.udc.ws.app.admin.client.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.ws.app.admin.client.service.ClientBikeService;
import es.udc.ws.app.admin.client.service.dto.ClientBikeDto;
import es.udc.ws.app.admin.client.service.rest.json.JsonClientBikeDtoConversor;
import es.udc.ws.app.admin.client.service.rest.json.JsonClientExceptionConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class RestClientBikeService implements ClientBikeService {

	private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientBikeService.endpointAddress";
	private String endpointAddress;

	@Override
	public ClientBikeDto addBike(String modelName, String description, Calendar startDate, float price,
			int availableNumber) throws InputValidationException {

		try {
			ClientBikeDto bike = new ClientBikeDto(modelName, description, startDate, price, availableNumber);
			HttpResponse response = Request.Post(getEndpointAddress() + "bikes")
					.bodyStream(toInputStream(bike), ContentType.create("application/json")).execute().returnResponse();
			validateStatusCode(HttpStatus.SC_CREATED, response);

			return JsonClientBikeDtoConversor.toClientBikeDto(response.getEntity().getContent());

		} catch (InputValidationException e) {
			throw e;
		} catch (Exception e) {
			System.out.println(e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public void updateBike(Long bikeId, String modelName, String description, Calendar startDate, float price,
			int availableNumber) throws InstanceNotFoundException, InputValidationException {

		try {
			ClientBikeDto bike = new ClientBikeDto(bikeId, modelName, description, startDate, price, availableNumber);
			HttpResponse response = Request.Put(getEndpointAddress() + "bikes/" + bike.getBikeId())
					.bodyStream(toInputStream(bike), ContentType.create("application/json")).execute().returnResponse();

			validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

		} catch (InputValidationException e) {
			throw e;
		} catch (InstanceNotFoundException e) {
			System.out.println("ERROR:");
			System.out.println("Bike not found. Id = " + bikeId);
			System.exit(-1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public ClientBikeDto findBike(Long bikeId) {

		try {
			HttpResponse response = Request
					.Get(getEndpointAddress() + "bikes/" + URLEncoder.encode(bikeId.toString(), "UTF-8")).execute()
					.returnResponse();

			validateStatusCode(HttpStatus.SC_OK, response);

			return JsonClientBikeDtoConversor.toClientBikeDto(response.getEntity().getContent());

		} catch (InstanceNotFoundException ex) {
			System.out.println("ERROR:");
			System.out.println("Bike not found. Id = " + bikeId);
			System.exit(-1);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;

	}

	private synchronized String getEndpointAddress() {
		if (endpointAddress == null) {
			endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
		}
		return endpointAddress;
	}

	private void validateStatusCode(int successCode, HttpResponse response)
			throws InstanceNotFoundException, InputValidationException, ParsingException {

		try {
			int statusCode = response.getStatusLine().getStatusCode();

			/* Success? */
			if (statusCode == successCode) {
				return;
			}

			/* Handler error. */
			switch (statusCode) {

			case HttpStatus.SC_NOT_FOUND:
				throw JsonClientExceptionConversor.fromInstanceNotFoundException(response.getEntity().getContent());

			case HttpStatus.SC_BAD_REQUEST:
				throw JsonClientExceptionConversor.fromInputValidationException(response.getEntity().getContent());

			default:
				throw new RuntimeException("HTTP error; status code = " + statusCode);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private InputStream toInputStream(ClientBikeDto bike) {

		try {

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
					JsonClientBikeDtoConversor.toJsonObject(bike));

			return new ByteArrayInputStream(outputStream.toByteArray());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
