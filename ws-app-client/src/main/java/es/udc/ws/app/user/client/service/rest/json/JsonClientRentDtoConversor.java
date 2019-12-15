package es.udc.ws.app.user.client.service.rest.json;

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
		if (rent.getRentDate() != null) {
			rentNode.set("rentDate", getRentDate(rent.getRentDate()));
		}
		rentNode.put("price", rent.getPrice()).put("rentScore",
				rent.getRentScore());
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

			String userEmail;
			if (rentObject.get("userEmail") != null) {
				userEmail = rentObject.get("userEmail").textValue().trim();
			} else {
				throw new ParsingException("userEmail");
			}

			String creditCard;
			if (rentObject.get("creditCard") != null) {
				creditCard = rentObject.get("creditCard").textValue();
			} else {
				throw new ParsingException("creditCard");
			}

			Long bikeId;
			if (rentObject.get("bikeId") != null) {
				bikeId = rentObject.get("bikeId").longValue();
			} else {
				throw new ParsingException("bikeId");
			}

			JsonNode calendarObjectNode = rentObject.get("startRentDate");
			Calendar startRentDate = null;
			if (calendarObjectNode != null) {
				startRentDate = Calendar.getInstance();
				startRentDate.set(calendarObjectNode.get("year").intValue(),
						calendarObjectNode.get("month").intValue() - 1,
						calendarObjectNode.get("day").intValue());
			} else {
				throw new ParsingException("startRentDate");
			}

			calendarObjectNode = rentObject.get("finishRentDate");
			Calendar finishRentDate = null;
			if (calendarObjectNode != null) {
				finishRentDate = Calendar.getInstance();
				finishRentDate.set(calendarObjectNode.get("year").intValue(),
						calendarObjectNode.get("month").intValue() - 1,
						calendarObjectNode.get("day").intValue());
			} else {
				throw new ParsingException("finishRentDate");
			}

			int numberOfBikes;
			if (rentObject.get("numberOfBikes") != null) {
				numberOfBikes = rentObject.get("numberOfBikes").intValue();
			} else {
				throw new ParsingException("numberOfBikes");
			}

			calendarObjectNode = rentObject.get("rentDate");
			Calendar rentDate = Calendar.getInstance();
			if (calendarObjectNode != null) {
				rentDate = Calendar.getInstance();
				rentDate.set(calendarObjectNode.get("year").intValue(),
						calendarObjectNode.get("month").intValue() - 1,
						calendarObjectNode.get("day").intValue());
			}

			int price = 0;
			if (rentObject.get("price") != null) {
				price = rentObject.get("price").intValue();
			}
			int rentScore = 0;
			if (rentObject.get("rentScore") != null) {
				rentScore = rentObject.get("rentScore").intValue();
			}
			return new ClientRentDto(rentId, userEmail, bikeId, creditCard,
					startRentDate, finishRentDate, numberOfBikes, rentDate,
					price, rentScore);
		}
	}

	public static List<ClientRentDto> toClientRentDtos(InputStream jsonBikes)
			throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBikes);
			if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
				throw new ParsingException(
						"Unrecognized JSON (array expected)");
			} else {
				ArrayNode bikesArray = (ArrayNode) rootNode;
				List<ClientRentDto> bikeDtos = new ArrayList<>(
						bikesArray.size());
				for (JsonNode bikeNode : bikesArray) {
					bikeDtos.add(toClientRentDto(bikeNode));
				}

				return bikeDtos;
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

}
