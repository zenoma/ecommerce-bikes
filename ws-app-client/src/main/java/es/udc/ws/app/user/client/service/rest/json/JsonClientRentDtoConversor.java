package es.udc.ws.app.user.client.service.rest.json;

import java.io.InputStream;
import java.util.Calendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.admin.client.service.dto.ClientBikeDto;
import es.udc.ws.app.user.client.service.dto.ClientRentDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonClientRentDtoConversor {

	public static JsonNode toJsonObject(ClientRentDto rent) {
		ObjectNode rentNode = JsonNodeFactory.instance.objectNode();
		rentNode.put("userEmail", rent.getUserEmail())
				.put("bikeId", rent.getBikeId())
				.put("creditCard", rent.getCreditCard())
				.set("startRentDate", getRentDate(rent.getStartRentDate()));
		rentNode.set("finishRentDate", getRentDate(rent.getFinishRentDate()));
		rentNode.put("numberOfBikes", rent.getNumberOfBikes());
		return rentNode;
	}

	public static ObjectNode getRentDate(Calendar date) {
		ObjectNode dateObject = JsonNodeFactory.instance.objectNode();

		int day = date.get(Calendar.DAY_OF_MONTH);
		int month = date.get(Calendar.MONTH) + 1;
		int year = date.get(Calendar.YEAR);

		dateObject.put("day", day).put("month", month).put("year", year);
		return dateObject;
	}

	public static int getRentDays(Calendar startDateRent,
			Calendar finishDateRent) {
		Long value = startDateRent.getTimeInMillis()
				- finishDateRent.getTimeInMillis();
		return (int) (value / (1000 * 60 * 60 * 24));
	}

	public static ClientRentDto toClientRentDto(InputStream jsonRent)
			throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonRent);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException(
						"Unrecognized JSON (object expected)");
			} else {
				return toClientRentDto(rootNode);
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	private static ClientRentDto toClientRentDto(JsonNode rentNode)
			throws ParsingException {
		if (rentNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			ObjectNode rentObject = (ObjectNode) rentNode;
			JsonNode rentIdNode = rentObject.get("rentId");
			Long rentId = (rentIdNode != null) ? rentIdNode.longValue() : null;
			return new ClientRentDto(rentId);
		}
	}

}
