package es.udc.ws.app.admin.client.service.rest.json;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import es.udc.ws.app.user.client.service.exception.UpdateReservedBikeException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonClientExceptionConversor {

	public final static String CONVERSION_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";

	public static InputValidationException fromInputValidationException(
			InputStream ex) throws ParsingException, IOException {

		ObjectMapper objectMapper = ObjectMapperFactory.instance();
		JsonNode rootNode = objectMapper.readTree(ex);
		if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			String message = rootNode.get("inputValidationException")
					.get("message").textValue();
			return new InputValidationException(message);
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

	public static UpdateReservedBikeException fromUpdateReservedBikeException(
			InputStream ex) throws UpdateReservedBikeException, IOException {

		ObjectMapper objectMapper = ObjectMapperFactory.instance();
		JsonNode rootNode = objectMapper.readTree(ex);
		if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			JsonNode data = rootNode.get("UpdateReservedBikeException");
			Long instanceId = data.get("bikeId").asLong();
			String instanceType = data.get("date").textValue();
			String instanceType2 = data.get("startDate").textValue();
			return new UpdateReservedBikeException(instanceId, instanceType,
					instanceType2);
		}

	}

}
