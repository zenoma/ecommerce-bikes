package es.udc.ws.app.restservice.json;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.model.bikeservice.exceptions.InvalidRentPeriodException;
import es.udc.ws.app.model.bikeservice.exceptions.NumberOfBikesException;
import es.udc.ws.app.model.bikeservice.exceptions.RentExpirationException;
import es.udc.ws.app.model.bikeservice.exceptions.UpdateReservedBikeException;
import es.udc.ws.app.model.bikeservice.exceptions.RentAlreadyRatedException;
import es.udc.ws.app.model.bikeservice.exceptions.InvalidUserRateException;
import es.udc.ws.app.restservice.exceptions.NotAllowedException;
import es.udc.ws.app.restservice.exceptions.ParsingBikeException;
import es.udc.ws.app.restservice.exceptions.ParsingRentException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class JsonServiceExceptionConversor {
	public final static String CONVERSION_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";

	public static JsonNode toInputValidationException(
			InputValidationException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("message", ex.getMessage());

		exceptionObject.set("inputValidationException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toInstanceNotFoundException(
			InstanceNotFoundException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("instanceId",
				(ex.getInstanceId() != null) ? ex.getInstanceId().toString()
						: null);
		dataObject.put("instanceType", ex.getInstanceType());

		exceptionObject.set("instanceNotFoundException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toInvalidRentPeriodException(
			InvalidRentPeriodException ex) {
		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		if ((ex.getStartDate() != null) && (ex.getFinishDate() != null)) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat(
					CONVERSION_PATTERN, Locale.ENGLISH);
			dataObject.put("startDate",
					dateFormatter.format(ex.getStartDate().getTime()));
			dataObject.put("finishDate",
					dateFormatter.format(ex.getFinishDate().getTime()));
		} else {
			dataObject.set("startDate", null);
			dataObject.set("finishDate", null);
		}
		exceptionObject.set("invalidRentPeriodException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toNumberOfBikesException(NumberOfBikesException ex) {
		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("bikeId",
				(ex.getBikeId() != null) ? ex.getBikeId() : null);
		dataObject.put("numberOfBikes", ex.getNumberOfBikes());
		exceptionObject.set("NumberOfBikesException", dataObject);
		return exceptionObject;
	}

	public static JsonNode toRentExpirationException(
			RentExpirationException ex) {
		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("rentId",
				(ex.getRentId() != null) ?  ex.getMessage() : null);
		exceptionObject.set("RentExpirationException", dataObject);
		return exceptionObject;
	}

	public static JsonNode toUpdateReservedBikeException(
			UpdateReservedBikeException ex) {
		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("bikeId",
				(ex.getBikeId() != null) ? ex.getBikeId() : null);

		if ((ex.getDate() != null) && (ex.getStartDate() != null)) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat(
					CONVERSION_PATTERN, Locale.ENGLISH);
			dataObject.put("date",
					dateFormatter.format(ex.getDate().getTime()));
			dataObject.put("startDate",
					dateFormatter.format(ex.getStartDate().getTime()));
		} else {
			dataObject.set("Date", null);
			dataObject.set("startDate", null);
		}

		exceptionObject.set("UpdateReservedBikeException", dataObject);
		return exceptionObject;
	}
	
	public static JsonNode toParsingBikeException (ParsingBikeException ex) {
		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
		
		dataObject.put("message", ex.getMessage());

		exceptionObject.set("ParsingBikeException", dataObject);

		return exceptionObject;
	}
	
	public static JsonNode toParsingRentException (ParsingRentException ex) {
		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
		
		dataObject.put("message", ex.getMessage());

		exceptionObject.set("ParsingBikeException", dataObject);

		return exceptionObject;
	}
	
	public static JsonNode toNotAllowedException (NotAllowedException ex) {
		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
		
		dataObject.put("message", ex.getMessage());

		exceptionObject.set("NotAllowedException", dataObject);

		return exceptionObject;
	}
	
	public static JsonNode toRentAlreadyRatedException (RentAlreadyRatedException ex) {
		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
		
		dataObject.put("message", ex.getMessage());

		exceptionObject.set("RentAlreadyRatedException", dataObject);

		return exceptionObject;
	}
	
	public static JsonNode toInvalidUserRateException (InvalidUserRateException ex) {
		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
		
		dataObject.put("message", ex.getMessage());

		exceptionObject.set("InvalidUserRateException", dataObject);

		return exceptionObject;
	}
}
