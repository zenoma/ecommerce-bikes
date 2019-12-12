package es.udc.ws.app.admin.client.service.rest.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.admin.client.service.dto.ClientBikeDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonClientBikeDtoConversor {

	public static JsonNode toJsonObject(ClientBikeDto bike) throws IOException {

		ObjectNode bikeObject = JsonNodeFactory.instance.objectNode();

		if (bike.getBikeId() != null) {
			bikeObject.put("bikeId", bike.getBikeId());
		}
		bikeObject.put("modelName", bike.getModelName())
				.put("description", bike.getDescription())
				.set("startDate", getBikeDate(bike.getStartDate()));
		bikeObject.put("price", bike.getPrice())
				.put("availableNumber", bike.getAvailableNumber())
				.put("numberOfRents", bike.getNumberOfRents())
				.put("totalScore", bike.getTotalScore());
		
		return bikeObject;
	}

	public static ClientBikeDto toClientBikeDto(InputStream jsonBike) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBike);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				return toClientBikeDto(rootNode);
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static List<ClientBikeDto> toClientBikeDtos(InputStream jsonBikes) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBikes);
			if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
				throw new ParsingException("Unrecognized JSON (array expected)");
			} else {
				ArrayNode bikesArray = (ArrayNode) rootNode;
				List<ClientBikeDto> bikeDtos = new ArrayList<>(bikesArray.size());
				for (JsonNode bikeNode : bikesArray) {
					bikeDtos.add(toClientBikeDto(bikeNode));
				}

				return bikeDtos;
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	private static ClientBikeDto toClientBikeDto(JsonNode bikeNode) throws ParsingException {
		if (bikeNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			ObjectNode bikeObject = (ObjectNode) bikeNode;

			JsonNode bikeIdNode = bikeObject.get("bikeId");
			Long bikeId = (bikeIdNode != null) ? bikeIdNode.longValue() : null;

			String modelName = bikeObject.get("modelName").textValue().trim();
			String description = bikeObject.get("description").textValue().trim();
			JsonNode calendarObject = bikeObject.get("startDate");
			Calendar date = Calendar.getInstance();
			date.set(calendarObject.get("year").intValue(), calendarObject.get("month").intValue(),
					calendarObject.get("day").intValue());
			float price = bikeObject.get("price").floatValue();
			int availableNumber = bikeObject.get("availableNumber").intValue();
			int numberOfRents = bikeObject.get("nomberOfRents").intValue();
			double totalScore = bikeObject.get("totalScore").doubleValue();
			
			return new ClientBikeDto(bikeId, modelName, description, date, 
					price, availableNumber, numberOfRents, totalScore);
		}
	}
	
	private static ObjectNode getBikeDate(Calendar Date) {

		ObjectNode releaseDateObject = JsonNodeFactory.instance.objectNode();

		int day = Date.get(Calendar.DAY_OF_MONTH);
		int month = Date.get(Calendar.MONTH) - Calendar.JANUARY + 1;
		int year = Date.get(Calendar.YEAR);

		releaseDateObject.put("day", day).put("month", month).put("year", year);

		return releaseDateObject;
	}

	
}
