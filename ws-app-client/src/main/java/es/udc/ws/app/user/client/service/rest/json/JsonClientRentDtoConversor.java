package es.udc.ws.app.user.client.service.rest.json;

import java.util.Calendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.user.client.service.dto.ClientRentDto;

public class JsonClientRentDtoConversor {

	public static JsonNode toJsonObject(ClientRentDto rent) {
		ObjectNode rentNode = JsonNodeFactory.instance.objectNode();

		rentNode.put("rentId", rent.getRentId())
				.put("userEmail", rent.getRentId())
				.put("bikeId", rent.getBikeId())
				.put("creditCard", rent.getCreditCard())
				.set("startRentDate", getRentDate(rent.getStartRentDate()));
		rentNode.put("duration",
				getRentDays(rent.getStartRentDate(), rent.getFinishRentDate()))
				.put("numberOfBikes", rent.getNumberOfBikes())
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

	public static int getRentDays(Calendar startDateRent,
			Calendar finishDateRent) {
		Long value = startDateRent.getTimeInMillis()
				- finishDateRent.getTimeInMillis();
		return (int) (value / (1000 * 60 * 60 * 24));
	}

}
