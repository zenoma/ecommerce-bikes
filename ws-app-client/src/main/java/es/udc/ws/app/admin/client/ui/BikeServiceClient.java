package es.udc.ws.app.admin.client.ui;

import es.udc.ws.app.admin.client.service.ClientBikeServiceFactory;
import es.udc.ws.app.admin.client.service.dto.ClientBikeDto;
import es.udc.ws.app.user.client.service.exception.UpdateReservedBikeException;
import es.udc.ws.app.admin.client.service.ClientBikeService;

import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.exceptions.InputValidationException;

import java.util.Calendar;

public class BikeServiceClient {

	public static void main(String[] args) {

		if (args.length == 0) {
			printUsageAndExit();
		}
		ClientBikeService clientBikeService = ClientBikeServiceFactory.getService();

		if ("-addBike".equalsIgnoreCase(args[0])) {
			validateArgs(args, 6, new int[] { 4, 5 }, "-addBike");

			// addBike <name> <description> <availabilityDate> <price> <units>

			try {

				Long bikeId = clientBikeService.addBike(args[1], args[2], stringToCalendar(args[3]),
						Float.valueOf(args[4]), Short.valueOf(args[5])).getBikeId();

				System.out.println("Bike " + bikeId + " created sucessfully");

			} catch (InputValidationException ex) {
				System.out.println("ERROR: ");
				System.out.println(ex.getMessage());
			} catch (NumberFormatException ex) {
				System.out.println(ex);
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-updateBike".equalsIgnoreCase(args[0])) {
			validateArgs(args, 7, new int[] { 1, 5, 6 }, "-updateBike");

			// -updateBike <id> <name> <description> <availabilityDate> <price> <units>

			try {

				clientBikeService.updateBike(Long.valueOf(args[1]), args[2], args[3], stringToCalendar(args[4]),
						Float.valueOf(args[5]), Short.valueOf(args[6]));

				System.out.println("Bike " + args[1] + " updated sucessfully");

			} catch (InputValidationException ex) {
				System.out.println("ERROR:");
				System.out.println(ex.getMessage());
			} catch (InstanceNotFoundException ex) {
				System.out.println("ERROR:");
				System.out.println(ex.getMessage());
			} catch (NumberFormatException ex) {
				ex.printStackTrace(System.err);
			}catch (UpdateReservedBikeException ex) {
				System.out.println("ERROR:");
				System.out.println(ex.getMessage());
			}catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else if ("-findBike".equalsIgnoreCase(args[0])) {
			validateArgs(args, 2, new int[] { 1 }, "-findBike");

			// -findBike <bikeId>
			try {
				ClientBikeDto bike = clientBikeService.findBike(Long.valueOf(args[1]));
				System.out.println("Id: " + bike.getBikeId() + " \nModelName: " + bike.getModelName()
						+ " \nDescription: " + bike.getDescription() + " \nStart Date: " + bike.getStartDate().getTime()
						+ " \nPrice: " + bike.getPrice() + " \nAvailable Number: " + bike.getAvailableNumber()
						+ " \nNumber Of Rents: " + bike.getNumberOfRents() + " \nAverage Score: "
						+ bike.getAverageScore());

			} catch (RuntimeException ex) {
				System.out.println("ERROR:");
				System.out.println(ex.getMessage());
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else {
			System.out.println("Operation not implemented");
			printUsageAndExit();
		}

	}

	public static void printUsageAndExit() {
		printUsage();
		System.exit(-1);
	}

	public static void printUsage() {
		System.err.println("Usage:\n" + "    -addBike <name> <description> <availabilityDate> <price> <units>\n"
				+ "    -updateBike <id> <name> <description> <availabilityDate> <price> <units>\n"
				+ "    -findBike <bikeId>\n");
	}

	public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments, String method) {

		if (expectedArgs != args.length) {
			System.out.println("Please check your input format:\n");
			switch (method) {
			case "-addBike":
				System.out.println("-addBike <name> <description> <availabilityDate> <price> <units>");
				break;
			case "-updateBike":
				System.out.println("-updateBike <id> <name> <description> <availabilityDate> <price> <units>");
				break;
			case "-findBike":
				System.out.println("-findBike <bikeId>");
				break;
			default:
				printUsageAndExit();
			}
			System.exit(-1);
		}

		for (int i = 0; i < numericArguments.length; i++) {
			int position = numericArguments[i];
			try {
				Double.parseDouble(args[position]);
			} catch (NumberFormatException n) {
				printUsageAndExit();
			}
		}
	}

	public static Calendar stringToCalendar(String s) {

		String delims = "[-,']";
		String[] tokens = s.split(delims);
		Short month = Short.valueOf(tokens[1]);
		Short day = Short.valueOf(tokens[0]);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(tokens[2]), month - 1, day);

		return calendar;
	}

}
