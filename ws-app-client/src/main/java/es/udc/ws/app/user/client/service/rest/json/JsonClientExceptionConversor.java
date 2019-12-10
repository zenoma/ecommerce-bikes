package es.udc.ws.app.user.client.service.rest.json;

import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonClientExceptionConversor {

	public final static String CONVERSION_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";

	public static InputValidationException fromInputValidationException(
			InputStream ex) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
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
			InputStream ex) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException(
						"Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.get("instanceNotFoundException");
				String instanceId = data.get("instanceId").textValue();
				String instanceType = data.get("instanceType").textValue();
				return new InstanceNotFoundException(instanceId, instanceType);
			}

		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	// FIXME Faltan excepciones
//	public static JsonNode toNumberOfBikesException(NumberOfBikesException ex) {
//		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
//		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
//
//		dataObject.put("bikeId",
//				(ex.getBikeId() != null) ? ex.getBikeId() : null);
//		dataObject.put("numberOfBikes", ex.getNumberOfBikes());
//		return exceptionObject;
//	}
	
}
