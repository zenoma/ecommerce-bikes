package es.udc.ws.app.user.client.service.rest.json;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.user.client.service.dto.ClientBikeDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonClientBikeDtoConversor {

	public static ObjectNode toObjectNode(ClientBikeDto bike) {

		ObjectNode bikeObject = JsonNodeFactory.instance.objectNode();

		bikeObject.put("bikeId", bike.getBikeId());
		bikeObject.put("modelName", bike.getModelName())
				.put("description", bike.getDescription())
				.set("startDate", getBikeDate(bike.getStartDate()));
		bikeObject.put("price", bike.getPrice())
				.put("availableNumber", bike.getAvailableNumber())
				.put("numberOfRents", bike.getNumberOfRents())
				.put("averageScore", bike.getAverageScore());
		return bikeObject;

	}

	public static ArrayNode toArrayNode(List<ClientBikeDto> bikes) {

		ArrayNode bikeNode = JsonNodeFactory.instance.arrayNode();
		for (int i = 0; i < bikes.size(); i++) {
			ClientBikeDto bikeDto = bikes.get(i);
			ObjectNode bikeObject = toObjectNode(bikeDto);
			bikeNode.add(bikeObject);
		}

		return bikeNode;
	}

	public static ClientBikeDto toClientBikeDto(InputStream jsonBike)
			throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();

			JsonNode rootNode = objectMapper.readTree(jsonBike);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException(
						"Unrecognized JSON (object expected)");
			} else {
				ObjectNode bikeObject = (ObjectNode) rootNode;
				Long bikeId = bikeObject.get("bikeId").longValue();

				String modelName = bikeObject.get("modelName").textValue()
						.trim();

				String description = bikeObject.get("description").textValue()
						.trim();

				float price = bikeObject.get("price").floatValue();

				int availableNumber = bikeObject.get("availableNumber")
						.intValue();

				JsonNode calendarObjectNode = bikeObject.get("startDate");
				Calendar date = Calendar.getInstance();
				date.set(calendarObjectNode.get("year").intValue(),
						calendarObjectNode.get("month").intValue() - 1,
						calendarObjectNode.get("day").intValue());

				int numberOfRents = bikeObject.get("numberOfRents").intValue();

				double averageScore = bikeObject.get("averageScore")
						.doubleValue();

				return new ClientBikeDto(bikeId, modelName, description, date,
						price, availableNumber, numberOfRents, averageScore);
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
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
