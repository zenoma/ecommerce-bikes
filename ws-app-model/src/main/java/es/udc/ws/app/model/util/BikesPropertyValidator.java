package es.udc.ws.app.model.util;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import es.udc.ws.app.model.bike.Bike;
import es.udc.ws.app.model.bikeservice.exceptions.InvalidRentPeriodException;
import es.udc.ws.app.model.bikeservice.exceptions.NumberOfBikesException;
import es.udc.ws.app.model.bikeservice.exceptions.RentExpirationException;
import es.udc.ws.app.model.rent.Rent;
import es.udc.ws.util.exceptions.InputValidationException;

public final class BikesPropertyValidator {

	private BikesPropertyValidator() {
	}

	public static void validateLowerFloat(String propertyName, float value,
			int lowerValidLimit) throws InputValidationException {

		if (value < lowerValidLimit) {
			throw new InputValidationException("Invalid " + propertyName
					+ " value (it must be greater than " + lowerValidLimit
					+ "): " + value);
		}
	}

	public static void validateLowerDouble(String propertyName, double value,
			int lowerValidLimit) throws InputValidationException {

		if (value < lowerValidLimit) {
			throw new InputValidationException("Invalid " + propertyName
					+ " value (it must be greater than " + lowerValidLimit
					+ "): " + value);
		}
	}

	public static void validateLowerInt(String propertyName, int value,
			int lowerValidLimit) throws InputValidationException {

		if (value < lowerValidLimit) {
			throw new InputValidationException("Invalid " + propertyName
					+ " value (it must be greater than " + lowerValidLimit
					+ "): " + value);
		}
	}

	public static void validatePreviousDate(String propertyName,
			Calendar propertyValue) throws InputValidationException {

		Calendar now = Calendar.getInstance();
		if (propertyValue == null) {
			throw new InputValidationException(
					"Invalid " + propertyName + " value (it cannot be null): ");
		} else {
			if (propertyValue.before(now)) {
				throw new InputValidationException("Invalid " + propertyName
						+ " value (it must be a past date): " + propertyValue.getTime());
			}
		}
	}

	public static void validatePairDates(Calendar calendarPrev,
			Calendar calendarPost) throws InputValidationException {

		if (calendarPrev == null || calendarPost == null) {
			throw new InputValidationException("Invalid " + calendarPrev
					+ "and " + calendarPost + " values cannot be null");
		} else {
			if (calendarPrev.after(calendarPost)) {
				throw new InputValidationException("Invalid: calendarPrev"
						+ calendarPrev.getTime() + "must be previous than calendarPost)"
						+ calendarPost.getTime());
			}
		}
	}

	public static void validateFutureDate(String propertyName,
			Calendar propertyValue) throws InputValidationException {

		Calendar now = Calendar.getInstance();
		if (propertyValue == null) {
			throw new InputValidationException(
					"Invalid " + propertyName + " value (it cannot be null): ");
		} else {
			if (propertyValue.before(now)) {
				throw new InputValidationException("Invalid " + propertyName
						+ " value (it must be a future date): "
						+ propertyValue.getTime());
			}
		}
	}

	public static void validateCreditCard(String propertyName, Long creditCard)
			throws InputValidationException {

		if (String.valueOf(creditCard).length() != 16) {
			throw new InputValidationException(
					"Invalid " + propertyName + " size (it must be 16)");
		}
	}

	public static void validateScore(String propertyName, int score)
			throws InputValidationException {

		if (score < 0 && score > 10) {
			throw new InputValidationException("Invalid " + propertyName
					+ " score (it must be between 0 and 10)");
		}
	}

	public static void validateEmail(String propertyName, String email)
			throws InputValidationException {
		int index = email.indexOf('@');
		if (index == -1) {
			throw new InputValidationException(
					"Invalid" + propertyName + "email must have a '@'");
		}
	}

	public static void validateRentPeriod(Calendar startDate,
			Calendar finishDate)
			throws InputValidationException, InvalidRentPeriodException {
		if (startDate == null || finishDate == null) {
			throw new InputValidationException("Invalid " + startDate.getTime() + "or "
					+ finishDate.getTime() + " values cannot be null");
		}
		if (startDate.after(finishDate)) {
			throw new InputValidationException("Invalid: calendarPrev"
					+ startDate.getTime() + "must be previous than calendarPost)"
					+ finishDate.getTime());
		}
		long days = ChronoUnit.DAYS.between(startDate.toInstant(),
				finishDate.toInstant());
		if (days > 15) {
			throw new InvalidRentPeriodException(startDate, finishDate);
		}

	}

	public static void validateNumberOfBikes(String string, Bike bike,
			int numberOfBikes) throws NumberOfBikesException {
		if (bike.getAvailableNumber() - numberOfBikes < 0) {
			throw new NumberOfBikesException(bike.getBikeId(), numberOfBikes);
		}

	}

	public static void validateRateRent(String string, Rent rent)
			throws RentExpirationException {
		if (rent.getFinishRentDate().getTimeInMillis() > Calendar.getInstance()
				.getTimeInMillis()) {
			throw new RentExpirationException(rent.getRentId());
		}
	}

	public static void validateNotNull(String string, Calendar startDate)
			throws InputValidationException {
		if (startDate == null) {
			throw new InputValidationException(
					"Invalid: startDate" + startDate.getTime() + "is null.");
		}
	}
}
