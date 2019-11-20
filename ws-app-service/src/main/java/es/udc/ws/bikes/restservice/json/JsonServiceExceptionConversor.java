package es.udc.ws.bikes.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidRentPeriodException;

public class JsonServiceExceptionConversor {
	// TODO Json to Exception Conversor
	public final static String CONVERSION_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";
	
	public static JsonNode toInputValidationException(InputValidationException ex) {
		
    	ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
    	ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
		
    	dataObject.put("message", ex.getMessage());
    	
    	exceptionObject.set("inputValidationException", dataObject);

        return exceptionObject;
    }
	
    public static JsonNode toInstanceNotFoundException(InstanceNotFoundException ex) {

    	ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
    	ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
		
    	dataObject.put("instanceId", (ex.getInstanceId()!=null) ? ex.getInstanceId().toString() : null);
    	dataObject.put("instanceType", ex.getInstanceType());
    	
    	exceptionObject.set("instanceNotFoundException", dataObject);

        return exceptionObject;
    }
    
    public static JsonNode toInvalidRentPeriodException(InvalidRentPeriodException ex) {
    	ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
    	ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
    	
    	//TODO revisar comportamiento de las excepciones, ya que no guardan información de la excepción
    	// solamente devuelven el mensaje, porque de la manera que está actualmente solamente prodria 
    	// funcionar de la siguiente manera, y no creo que sea correcto.
    	dataObject.put("Exception: ", ex.toString());
    	exceptionObject.set("saleExpirationException", dataObject);
    	return exceptionObject;
    }
    
    public static JsonNode toNumberOfBikesException() {
    	ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
    	ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
    	
    	return exceptionObject;
    }

    public static JsonNode toRateRentDateException() {
    	ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
    	ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
    	
    	return exceptionObject;
    }
    
    public static JsonNode toRentExpirationException() {
    	ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
    	ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
    	
    	return exceptionObject;
    }
    
    public static JsonNode toUpdateReservedBikeException() {
    	ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
    	ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
    	
    	return exceptionObject;
    }
}
