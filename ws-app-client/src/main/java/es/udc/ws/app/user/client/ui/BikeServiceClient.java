package es.udc.ws.app.user.client.ui;

import es.udc.ws.app.user.client.service.ClientBikeServiceFactory;
import es.udc.ws.app.user.client.service.dto.ClientBikeDto;
import es.udc.ws.app.user.client.service.dto.ClientRentDto;
import es.udc.ws.app.user.client.service.exception.NumberOfBikesException;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.user.client.service.ClientBikeService;

public class BikeServiceClient {

	public static void main(String[] args) {

		if (args.length == 0) {
			printUsageAndExit();
		}
		ClientBikeService clientBikeService = ClientBikeServiceFactory
				.getService();
		if ("-findBikes".equalsIgnoreCase(args[0])) {
			validateArgs(args, 3, new int[] {}, args[0]);
			try {
				List<ClientBikeDto> bikes = clientBikeService.findBikes(args[1],
						stringToCalendar(args[2]));
				System.out.println("Found " + bikes.size()
						+ " bikes(s) with keywords '" + args[1] + "' and date '"
						+ stringToCalendar(args[2]).getTime() + "'");
				for (int i = 0; i < bikes.size(); i++) {
					ClientBikeDto bikesDto = bikes.get(i);
					System.out.println("[Id: " + bikesDto.getBikeId()
							+ ", ModelName: " + bikesDto.getModelName()
							+ ", Description: " + bikesDto.getDescription()
							+ ", Start Date: "
							+ bikesDto.getStartDate().getTime() + ", Price: "
							+ bikesDto.getPrice() + ", Available Number: "
							+ bikesDto.getAvailableNumber()
							+ ", Number Of Rents: "
							+ bikesDto.getNumberOfRents() + ", Average Score: "
							+ bikesDto.getTotalScore() + "]");
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else if ("-reserve".equalsIgnoreCase(args[0])) {
			validateArgs(args, 7, new int[] { 2, 6 }, args[0]);

			Long rentId;
			try {
				rentId = clientBikeService.rentBike(args[1], args[3],
						Long.parseLong(args[2]), stringToCalendar(args[4]),
						stringToCalendar(args[5]), Short.valueOf(args[6]));
				System.out.println("\nBike with ID '" + args[2]
						+ "' rented sucessfully. Rent Code: '" + rentId
						+ "'\n");
			}catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-rateReservation".equalsIgnoreCase(args[0])) {
			validateArgs(args, 4, new int[] { 1, 3 }, args[0]);

			try {
				clientBikeService.rateRent(Long.valueOf(args[1]),
						Short.valueOf(args[3]), args[2]);
				System.out.println("\nThe user '" + args[2]
						+ "' has succesfully rated the rent '" + args[1]
						+ "'  with score '" + args[3] + "'\n");
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-findReservations".equalsIgnoreCase(args[0])) {
			validateArgs(args, 2, new int[] {}, args[0]);

			try {
				List<ClientRentDto> rents = clientBikeService
						.findRents(args[1]);
				System.out.println("Found " + rents.size()
						+ " rent(s) with email '" + args[1] + "'");
				for (int i = 0; i < rents.size(); i++) {
					ClientRentDto rentDto = rents.get(i);
					System.out.println("Id: " + rentDto.getRentId()
							+ ", Email: " + rentDto.getUserEmail()
							+ ", Duration: "
							+ getRentDays(rentDto.getFinishRentDate(),
									rentDto.getStartRentDate())
							+ ", Credit Card: " + rentDto.getCreditCard()
							+ ", Number of Rented Bikes: "
							+ rentDto.getNumberOfBikes() + ", Rent Date: "
							+ rentDto.getRentDate().getTime() + ", Price: "
							+ rentDto.getPrice() + ", Rent Score: "
							+ rentDto.getRentScore());
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}
	}

	public static void validateArgs(String[] args, int expectedArgs,
			int[] numericArguments, String method) {

		if (expectedArgs != args.length) {
			System.out.println("Please check your input format:\n");
			switch (method) {
			case "-findBikes":
				System.out.println("-findBikes <keywords> <date>");
				break;
			case "-reserve":
				System.out.println(
						"-reserve <userEmail> <bikeId> <creditCardNumber> <startDate> <endDate> <units>");
				break;
			case "-rateReservation":
				System.out.println(
						"-rateReservation <id|code> <userEmail> <points>");
				break;
			case "-findReservations":
				System.out.println(" -findReservations <userEmail>");
				break;
			default:
				printUsageAndExit();
				break;
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

	public static void printUsageAndExit() {
		printUsage();
		System.exit(-1);
	}

	public static void printUsage() {
		System.err.println("Usage:\n" + "    -findBikes <keywords> <date>\n"
				+ "    -reserve <userEmail> <bikeId> <creditCardNumber> <startDate> <endDate> <units>\n"
				+ "    -rateReservation <id|code> <userEmail> <points>\n"
				+ "    -findReservations <userEmail>\n");
	}

	public static int getRentDays(Calendar startDateRent,
			Calendar finishDateRent) {
		Long value = startDateRent.getTimeInMillis()
				- finishDateRent.getTimeInMillis();
		return (int) (value / (1000 * 60 * 60 * 24) + 1);
	}
}
