package es.udc.ws.app.restservice.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.dto.ServiceBikeDto;
import es.udc.ws.app.restservice.exceptions.ParsingBikeException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonServiceBikeDtoConversor {

	public static ObjectNode toObjectNode(ServiceBikeDto bike) {
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
				.put("averageScore", bike.getAverageScore());

		return bikeObject;
	}

	public static ArrayNode toArrayNode(List<ServiceBikeDto> bikes) {

		ArrayNode bikeNode = JsonNodeFactory.instance.arrayNode();
		for (int i = 0; i < bikes.size(); i++) {
			ServiceBikeDto bikeDto = bikes.get(i);
			ObjectNode bikeObject = toObjectNode(bikeDto);
			bikeNode.add(bikeObject);
		}

		return bikeNode;
	}

	public static ServiceBikeDto toServiceBikeDtoPut(InputStream jsonBike)
			throws ParsingBikeException, IOException {
		
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = null;
			try {
				rootNode = objectMapper.readTree(jsonBike);
			}catch(JsonParseException ex) {
				throw new ParsingException(
						"Unrecognized JSON (malformed)");
			}
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException(
						"Unrecognized JSON (object expected)");
			} else {
				ObjectNode bikeObject = (ObjectNode) rootNode;
				JsonNode bikeIdNode = bikeObject.get("bikeId");
				Long bikeId = (bikeIdNode != null) ? bikeIdNode.longValue(): null;

				JsonNode modelNameNode = bikeObject.get("modelName");
				String modelName = null; 
				if (modelNameNode != null) {
					try {
						modelName = modelNameNode.textValue().trim();
					}catch(NullPointerException ex) {
						throw new ParsingBikeException("Invalid ModelName value, Not a String");
					}
				}

				JsonNode descriptionNode = bikeObject.get("description");
				String description =null; 
				if(descriptionNode != null) {
					try {
						description = descriptionNode.textValue().trim();
					}catch(NullPointerException ex) {
						throw new ParsingBikeException("Invalid description value, Not a String");
					}
				}

				JsonNode priceNode = bikeObject.get("price");
				float price = -1;
				if(priceNode != null) {
					if (!priceNode.isTextual()) {
						price = priceNode.floatValue();
					} else {
						throw new ParsingBikeException("Invalid price value, Not a String");
					}
				}
				
				JsonNode availableNumberNode = bikeObject.get("availableNumber");
				int availableNumber = -1;
				if (availableNumberNode != null) {
					if(!availableNumberNode.isTextual()) {
						availableNumber = availableNumberNode.intValue();
					}else {
						throw new ParsingBikeException("Invalid availableNumber value, Not a String");
					}
				}

				JsonNode calendarObjectNode = bikeObject.get("startDate");
				Calendar date = null;
				if (calendarObjectNode != null) {
					 date = Calendar.getInstance();
					date.set(calendarObjectNode.get("year").asInt(),
							calendarObjectNode.get("month").asInt() - 1,
							calendarObjectNode.get("day").asInt());
				}else {
					throw new ParsingBikeException("date Not found");
				}
				
				return new ServiceBikeDto(bikeId, modelName, description, date,
						price, availableNumber);
			}
	}

	public static ServiceBikeDto toServiceBikeDtoPost(InputStream jsonBike)
			throws ParsingBikeException, IOException {
		ObjectMapper objectMapper = ObjectMapperFactory.instance();
		JsonNode rootNode = null;
		try {
			rootNode = objectMapper.readTree(jsonBike);
		}catch(JsonParseException ex) {
			throw new ParsingException(
					"Unrecognized JSON (malformed)");
		}
		if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException(
					"Unrecognized JSON (object expected)");
		} else {
			ObjectNode bikeObject = (ObjectNode) rootNode;
			JsonNode bikeIdNode = bikeObject.get("bikeId");
			Long bikeId = (bikeIdNode != null) ? bikeIdNode.longValue(): null;

			JsonNode modelNameNode = bikeObject.get("modelName");
			String modelName;
			if (modelNameNode != null) {
				try {
					modelName = modelNameNode.textValue().trim();
				}catch (NullPointerException ex) {
					throw new ParsingBikeException("Invalid ModelName value, Not a String");
				}
			}else {
				throw new ParsingBikeException("modelName Not found");
			}

			JsonNode descriptionNode = bikeObject.get("description");
			String description;
			if (descriptionNode != null) {
				try {
					description = descriptionNode.textValue().trim();
				}catch (NullPointerException ex) {
					throw new ParsingBikeException("Invalid description value, not a String");
			}
			}else {
				throw new ParsingBikeException("description Not found");
			}

			JsonNode priceNode = bikeObject.get("price");
			float price;
			if (priceNode != null) {
				if(!priceNode.isTextual()) {
					price = priceNode.floatValue();
				}else {
					throw new ParsingBikeException("Invalid price value, not a number");
				}
			}else {
				throw new ParsingBikeException("price Not found");
			}
			
			JsonNode availableNumberNode = bikeObject.get("availableNumber");
			int availableNumber;
			if (availableNumberNode != null) {
				if (!availableNumberNode.isTextual()) {
					availableNumber = availableNumberNode.intValue();
				}else {
					throw new ParsingBikeException("Invalid availableNumber value, not a number");
				}
			}else {
				throw new ParsingBikeException("availableNumber Not found");
			}

			JsonNode calendarObjectNode = bikeObject.get("startDate");
			Calendar date = null;
			if (calendarObjectNode != null) {
				date = Calendar.getInstance();
				//try {
					date.set(calendarObjectNode.get("year").intValue(),
						calendarObjectNode.get("month").intValue() - 1,
						calendarObjectNode.get("day").intValue());
				//}catch (NullPointerException ex) {
				//	throw new ParsingBikeException("Invalid startDate value, one or more values aren't numbers");
				//}
			}else {
				throw new ParsingBikeException("date Not found");
			}
			
			return new ServiceBikeDto(bikeId, modelName, description, date,
					price, availableNumber);
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
