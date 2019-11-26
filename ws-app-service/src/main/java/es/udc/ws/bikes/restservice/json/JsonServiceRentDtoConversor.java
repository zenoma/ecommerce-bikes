package es.udc.ws.bikes.restservice.json;

import java.util.Calendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.dto.ServiceRentDto;

public class JsonServiceRentDtoConversor {

	public static JsonNode toJsonObject(ServiceRentDto rent) {
		ObjectNode rentNode = JsonNodeFactory.instance.objectNode();

		if (rent.getRentId() != null) {
			rentNode.put("rentId", rent.getRentId());
		}
		// First call to .set transforms an ObjectNode into a JsonNode;
		rentNode.put("userEmail", rent.getUserEmail())
			.put("bikeId", rent.getBikeId())
			.put("creditCard", rent.getCreditCard())
			.set("startRentDate", getRentDate(rent.getStartRentDate()));
		rentNode.set("finishRentDate", getRentDate(rent.getFinishRentDate()));
		rentNode.put("numberOfBikes", rent.getNumberOfBikes())
			.set("rentDate", getRentDate(rent.getRentDate()));
		rentNode.put("price", rent.getPrice());
		

		return rentNode;
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
