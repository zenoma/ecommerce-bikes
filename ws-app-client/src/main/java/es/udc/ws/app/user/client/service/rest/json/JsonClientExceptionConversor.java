package es.udc.ws.app.user.client.service.rest.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.user.client.service.exception.InvalidRentPeriodException;
import es.udc.ws.app.user.client.service.exception.InvalidUserRateException;
import es.udc.ws.app.user.client.service.exception.NotAllowedException;
import es.udc.ws.app.user.client.service.exception.NumberOfBikesException;
import es.udc.ws.app.user.client.service.exception.RentAlreadyRatedException;
import es.udc.ws.app.user.client.service.exception.RentExpirationException;
import es.udc.ws.app.user.client.service.exception.UpdateReservedBikeException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonClientExceptionConversor {

	public final static String CONVERSION_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";

	public static Exception from400(InputStream ex) throws IOException,
			RentExpirationException, NumberOfBikesException {
		ObjectMapper objectMapper = ObjectMapperFactory.instance();
		JsonNode rootNode = objectMapper.readTree(ex);
		if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else if (rootNode.has("inputValidationException")) {
			return fromInputValidationException(rootNode);
		} else if (rootNode.has("InvalidUserRateException")) {
			return fromInvalidUserRateException(rootNode);
		} else if (rootNode.has("invalidRentPeriodException")) {
			return fromInvalidRentPeriodException(rootNode);
		}
		return new Exception("from400");
	}

	private static InvalidRentPeriodException fromInvalidRentPeriodException(
			JsonNode rootNode) {
		if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			JsonNode data = rootNode.get("invalidRentPeriodException");
			String startDate = data.get("startDate").textValue();
			String finishDate = data.get("finishDate").textValue();
			return new InvalidRentPeriodException(startDate, finishDate);
		}
	}

	private static InvalidUserRateException fromInvalidUserRateException(
			JsonNode rootNode) {
		if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			JsonNode data = rootNode.get("InvalidUserRateException");
			String string = data.get("message").textValue();
			return new InvalidUserRateException(string);
		}
	}

	public static InputValidationException fromInputValidationException(
			JsonNode rootNode) throws ParsingException {
		try {
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException(
						"Unrecognized JSON (object expected)");
			} else {
				String message = rootNode.get("inputValidationException")
						.get("message").textValue();
				return new InputValidationException(message);
			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static InstanceNotFoundException fromInstanceNotFoundException(
			InputStream ex) throws ParsingException, IOException {
		ObjectMapper objectMapper = ObjectMapperFactory.instance();
		JsonNode rootNode = objectMapper.readTree(ex);
		if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			JsonNode data = rootNode.get("instanceNotFoundException");
			String instanceId = data.get("instanceId").textValue();
			String instanceType = data.get("instanceType").textValue();

			return new InstanceNotFoundException(instanceId, instanceType);
		}
	}

	public static NotAllowedException fromNotAllowedException(InputStream ex)
			throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException(
						"Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.get("instanceNotFoundException");
				String texto = data.get("texto").textValue();
				return new NotAllowedException(texto);
			}

		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static Exception from403(InputStream ex) throws IOException {
		ObjectMapper objectMapper = ObjectMapperFactory.instance();
		JsonNode rootNode = objectMapper.readTree(ex);
		if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else if (rootNode.has("UpdatedReservedBikeException")) {
			return fromUpdatedReservedBikeException(rootNode);
		} else if (rootNode.has("RentAlreadyRatedException")) {
			return fromRentAlreadyRatedException(rootNode);
		} else if (rootNode.has("NumberOfBikesException")) {
			return fromNumberOfBikesException(rootNode);
		} else if (rootNode.has("RentExpirationException")) {
			return fromRenExpirationException(rootNode);
		}
		return new Exception("from403");
	}

	private static UpdateReservedBikeException fromUpdatedReservedBikeException(
			JsonNode root) {
		if (root.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			JsonNode data = root.get("UpdatedReservedBikeException");
			Long instanceId = data.get("bikeId").asLong();
			String date = data.get("date").textValue();
			String startDate = data.get("startDate").textValue();
			return new UpdateReservedBikeException(instanceId, date, startDate);
		}
	}

	private static RentAlreadyRatedException fromRentAlreadyRatedException(
			JsonNode root) {
		if (root.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			JsonNode data = root.get("RentAlreadyRatedException");
			String message = data.get("message").textValue();
			return new RentAlreadyRatedException(message);
		}
	}

	private static NumberOfBikesException fromNumberOfBikesException(
			JsonNode root) {
		if (root.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			JsonNode data = root.get("NumberOfBikesException");
			Long instanceId = data.get("bikeId").asLong();
			int instanceType = data.get("numberOfBikes").intValue();
			return new NumberOfBikesException(instanceId, instanceType);
		}
	}

	private static RentExpirationException fromRenExpirationException(
			JsonNode rootNode) throws IOException {

		if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			JsonNode data = rootNode.get("RentExpirationException");
			Long instanceId = data.get("rentId").asLong();
			return new RentExpirationException(instanceId);
		}
	}
}
