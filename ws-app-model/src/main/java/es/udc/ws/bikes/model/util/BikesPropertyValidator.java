package es.udc.ws.bikes.model.util;

import es.udc.ws.util.exceptions.InputValidationException;

public final class BikesPropertyValidator {

	private BikesPropertyValidator() {}
	
    public static void validateLowerFloat(String propertyName,
            float value, int lowerValidLimit)
            throws InputValidationException {

        if (value < lowerValidLimit) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than " + lowerValidLimit +
                     "): " + value);
        }
    }
    
    public static void validateLowerInt(String propertyName,
            int value, int lowerValidLimit)
            throws InputValidationException {

        if (value < lowerValidLimit) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than " + lowerValidLimit +
                     "): " + value);
        }
    }
}
