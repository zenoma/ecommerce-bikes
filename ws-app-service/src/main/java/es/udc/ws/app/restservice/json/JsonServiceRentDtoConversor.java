package es.udc.ws.app.restservice.json;

import java.io.InputStream;
import java.util.Calendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.dto.ServiceRentDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonServiceRentDtoConversor {

	public static JsonNode toJsonObject(ServiceRentDto rent) {
		ObjectNode rentNode = JsonNodeFactory.instance.objectNode();

		if (rent.getRentId() != null) {
			rentNode.put("rentId", rent.getRentId());
		}
		// First call to .set transforms an ObjectNode into a JsonNode;
		/*rentNode.put("userEmail", rent.getUserEmail())
			.put("bikeId", rent.getBikeId())
			.put("creditCard", rent.getCreditCard())
			.set("startRentDate", getRentDate(rent.getStartRentDate()));
		rentNode.set("finishRentDate", getRentDate(rent.getFinishRentDate()));
		rentNode.put("numberOfBikes", rent.getNumberOfBikes())
			.set("rentDate", getRentDate(rent.getRentDate()));
		rentNode.put("price", rent.getPrice());*/
		

		return rentNode;
	}
	
	public static ServiceRentDto toServiceRentDto(InputStream jsonRent)  throws ParsingException{
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonRent);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException(
						"Unrecognized JSON (object expected)");
			}else {
				ObjectNode rentObject = (ObjectNode) rootNode;
				
				String userEmail = rentObject.get("userEmail").textValue().trim();
				
				Long creditCard =  rentObject.get("creditCardNumber").longValue();
				
				Long bikeId = rentObject.get("bikeId").longValue();

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
				
				int numberOfBikes = rentObject.get("numberOfBikes").intValue();

				return new ServiceRentDto(userEmail, bikeId, creditCard, 
						startRentDate, finishRentDate, numberOfBikes);
			}
		}catch(ParsingException ex) {
			throw ex;
		}catch (Exception e) {
			throw new ParsingException(e);
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
