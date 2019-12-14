package es.udc.ws.app.restservice.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.dto.ServiceRentDto;
import es.udc.ws.app.restservice.exceptions.ParsingRentException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonServiceRentDtoConversor {

	public static JsonNode toJsonObject(ServiceRentDto rent) {
		ObjectNode rentNode = JsonNodeFactory.instance.objectNode();

		if (rent.getRentId() != null) {
			rentNode.put("rentId", rent.getRentId());
		}
		return rentNode;
	}
	
	public static ObjectNode toObjectNode(ServiceRentDto rent) {
		ObjectNode rentObject = JsonNodeFactory.instance.objectNode();
		if (rent.getRentId() != null) {
			rentObject.put("rentId", rent.getRentId());
		}
		rentObject.put("userEmail",rent.getUserEmail())
				.put("bikeId",rent.getBikeId())
				.put("creditCard", rent.getCreditCard())
				.set("startRentDate", getRentDate(rent.getStartRentDate()));
		rentObject.set("finishRent",getRentDate(rent.getFinishRentDate()));
		rentObject.put("numberOfBikes", rent.getNumberOfBikes())
				.set("rentDate", getRentDate(rent.getRentDate()));
		rentObject.put("price", rent.getPrice());
		return rentObject;
	}
	
	public static ArrayNode toArrayNode(List<ServiceRentDto> rents) {
		ArrayNode rentNode = JsonNodeFactory.instance.arrayNode();
		for (int i = 0; i < rents.size(); i++) {
			ServiceRentDto rentDto = rents.get(i);
			ObjectNode rentObject = toObjectNode(rentDto);
			rentNode.add(rentObject);
		}
		return rentNode;
	}
	
	public static ServiceRentDto toServiceRentDtoPut(InputStream jsonRent)  throws ParsingException{
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonRent);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException(
						"Unrecognized JSON (object expected)");
			}else {
				ObjectNode rentObject = (ObjectNode) rootNode;
				
				JsonNode userEmailNode = rentObject.get("userEmail");
				String userEmail = (userEmailNode != null) ? userEmailNode.textValue().trim() : null;
				
				JsonNode creditCardNode = rentObject.get("creditCard");
				String creditCard = (creditCardNode != null) ? creditCardNode.textValue() : null;
				
				JsonNode bikeIdNode = rentObject.get("bikeId");
				Long bikeId = (bikeIdNode != null) ? bikeIdNode.longValue() : null;

				JsonNode calendarObjectNode = rentObject.get("startRentDate");
				Calendar startRentDate = null;
				if (calendarObjectNode != null) {
					startRentDate = Calendar.getInstance();
					startRentDate.set(calendarObjectNode.get("year").intValue(),
							calendarObjectNode.get("month").intValue() - 1,
							calendarObjectNode.get("day").intValue());
				}

				calendarObjectNode = rentObject.get("finishRentDate");
				Calendar finishRentDate = null;
				if (calendarObjectNode != null) {
					finishRentDate = Calendar.getInstance();
					finishRentDate.set(calendarObjectNode.get("year").intValue(),
							calendarObjectNode.get("month").intValue() - 1,
							calendarObjectNode.get("day").intValue());
				}
				
				JsonNode numberOfBikesNode = rentObject.get("numberOfBikes");
				int numberOfBikes = (numberOfBikesNode != null) ? numberOfBikesNode.intValue(): -1;
				
				return new ServiceRentDto(userEmail, bikeId, creditCard, 
						startRentDate, finishRentDate, numberOfBikes);
			}
		}catch(ParsingException ex) {
			throw ex;
		}catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	public static ServiceRentDto toServiceRentDtoPost(InputStream jsonRent)  
			throws ParsingRentException, IOException{
		ObjectMapper objectMapper = ObjectMapperFactory.instance();
		JsonNode rootNode = objectMapper.readTree(jsonRent);
		if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException(
					"Unrecognized JSON (object expected)");
		}else {
			ObjectNode rentObject = (ObjectNode) rootNode;
			String userEmail; 
			if (rentObject.get("userEmail") != null) {
				userEmail = rentObject.get("userEmail").textValue().trim();
			}else {
				throw new ParsingRentException("userEmail");
			}
			
			String creditCard;
			if (rentObject.get("creditCard") != null) {
				creditCard = rentObject.get("creditCard").textValue();
			}else {
				throw new ParsingRentException("creditCard");
			}
			
			Long bikeId;
			if (rentObject.get("bikeId") != null) {
				bikeId = rentObject.get("bikeId").longValue();
			}else {
				throw new ParsingRentException("bikeId");
			}

			JsonNode calendarObjectNode = rentObject.get("startRentDate");
			Calendar startRentDate = null;
			if (calendarObjectNode != null) {
				startRentDate = Calendar.getInstance();
				startRentDate.set(calendarObjectNode.get("year").intValue(),
						calendarObjectNode.get("month").intValue() - 1,
						calendarObjectNode.get("day").intValue());
			}else {
				throw new ParsingRentException("startRentDate");
			}

			calendarObjectNode = rentObject.get("finishRentDate");
			Calendar finishRentDate = null;
			if (calendarObjectNode != null) {
				finishRentDate = Calendar.getInstance();
				finishRentDate.set(calendarObjectNode.get("year").intValue(),
						calendarObjectNode.get("month").intValue() - 1,
						calendarObjectNode.get("day").intValue());
			}else {
				throw new ParsingRentException("finishRentDate");
			}
			
			int numberOfBikes;
			if (rentObject.get("numberOfBikes") != null) {
				numberOfBikes = rentObject.get("numberOfBikes").intValue();
			}else {
				throw new ParsingRentException("numberOfBikes");
			}

			return new ServiceRentDto(userEmail, bikeId, creditCard, 
					startRentDate, finishRentDate, numberOfBikes);
		}
		
	}

	public static ObjectNode getRentDate(Calendar date) {
		ObjectNode dateObject = JsonNodeFactory.instance.objectNode();

		int day = date.get(Calendar.DAY_OF_MONTH);
		int month = date.get(Calendar.MONTH) - Calendar.JANUARY + 1;
		int year = date.get(Calendar.YEAR);

		dateObject.put("day", day).put("month", month).put("year", year);
		return dateObject;
	}
}
