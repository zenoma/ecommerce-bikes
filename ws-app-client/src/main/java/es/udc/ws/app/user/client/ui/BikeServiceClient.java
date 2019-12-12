package es.udc.ws.app.user.client.ui;

import es.udc.ws.app.user.client.service.ClientBikeServiceFactory;
import es.udc.ws.app.user.client.service.dto.ClientBikeDto;
import es.udc.ws.app.user.client.service.dto.ClientRentDto;

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
			validateArgs(args, 3, new int[] {});
			try {
				List<ClientBikeDto> bikes = clientBikeService.findBikes(args[1],
						stringToCalendar(args[2]));
				System.out.println(
						"Found " + bikes.size() + " bikes(s) with keywords '"
								+ args[1] + "' and date '" + args[2] + "'");
				for (int i = 0; i < bikes.size(); i++) {
					ClientBikeDto bikesDto = bikes.get(i);
					System.out.println("Id: " + bikesDto.getBikeId()
							+ ", ModelName: " + bikesDto.getModelName()
							+ ", Description: " + bikesDto.getDescription()
							+ ", Start Date: " + bikesDto.getStartDate()
							+ ", Price: " + bikesDto.getPrice()
							+ ", Available Number: "
							+ bikesDto.getAvailableNumber()
							+ ", Number Of Rents: "
							+ bikesDto.getNumberOfRents() + ", Average Score: "
							+ bikesDto.getAverageScore());
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else if ("-reserve".equalsIgnoreCase(args[0])) {
			validateArgs(args, 7, new int[] { 2, 6 });

			Long rentId;
			try {
				rentId = clientBikeService.rentBike(args[1],
						Long.parseLong(args[3]), Long.parseLong(args[2]),
						stringToCalendar(args[4]), stringToCalendar(args[5]),
						Short.valueOf(args[6]));
				System.out.println("Bike +" + args[2]
						+ "rented sucessfully with rent number " + rentId);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-rateReservation".equalsIgnoreCase(args[0])) {
			validateArgs(args, 4, new int[] { 1, 3 });

			try {
				clientBikeService.rateRent(Long.valueOf(args[1]),
						Short.valueOf(args[3]));
				System.out.println("Rent " + args[1]
						+ "rated sucessfully with the score number " + args[3]);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-findReservations".equalsIgnoreCase(args[0])) {
			validateArgs(args, 2, new int[] {});

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
							+ getRentDays(rentDto.getStartRentDate(),
									rentDto.getFinishRentDate())
							+ ", Credit Card: " + rentDto.getCreditCard()
							+ ", Number of Rented Bikes: "
							+ rentDto.getNumberOfBikes() + ", Rent Date: "
							+ rentDto.getRentDate() + ", Price: "
							+ rentDto.getPrice());
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}
	}

	public static void validateArgs(String[] args, int expectedArgs,
			int[] numericArguments) {
		if (expectedArgs != args.length) {
			printUsageAndExit();
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

		String delims = "[-]";
		String[] tokens = s.split(delims);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(tokens[2]),
				Integer.parseInt(tokens[1]) - 1, Integer.parseInt(tokens[2]));

		return calendar;
	}

	public static void printUsageAndExit() {
		printUsage();
		System.exit(-1);
	}

	public static void printUsage() {
		System.err.println("Usage:\n"
				+ "    -findBikes <keywords> <date>\n"
				+ "    -reserve <userEmail> <bikeId> <creditCardNumber> <startDate> <endDate> <units>\n"
				+ "    -rateReservation <id|code> <userEmail> <points>\n"
				+ "    -findReservations <userEmail>\n");
	}

	public static int getRentDays(Calendar startDateRent,
			Calendar finishDateRent) {
		Long value = startDateRent.getTimeInMillis()
				- finishDateRent.getTimeInMillis();
		return (int) (value / (1000 * 60 * 60 * 24));
	}
}
