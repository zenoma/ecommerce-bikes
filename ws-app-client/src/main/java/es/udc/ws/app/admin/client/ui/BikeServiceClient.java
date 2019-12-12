package es.udc.ws.app.admin.client.ui;

import es.udc.ws.app.admin.client.service.ClientBikeServiceFactory;
import es.udc.ws.app.admin.client.service.dto.ClientBikeDto;
import es.udc.ws.app.admin.client.service.ClientBikeService;

import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.exceptions.InputValidationException;

import java.util.Calendar;



public class BikeServiceClient {

	public static void main(String[] args) {
		
		if(args.length == 0) {
            printUsageAndExit();
        }
		ClientBikeService clientBikeService = ClientBikeServiceFactory.getService();
		
		if("-addBike".equalsIgnoreCase(args[0])) {
			validateArgs(args, 6, new int[] {4, 5});
			System.out.println("Despues validate");
			
			//addBike <name> <description> <availabilityDate> <price> <units>
			
			try {
                Long bikeId = clientBikeService.addBike(args[1], args[2], 
                		stringToCalendar(args[3]), Float.valueOf(args[4]), Short.valueOf(args[5])).getBikeId();

                System.out.println("Bike " + bikeId + " created sucessfully");

            } catch (NumberFormatException | InputValidationException ex) {
            	System.out.println("Error 1");
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
            	System.out.println("Error 2");
                ex.printStackTrace(System.err);
            }
			
		}  else if("-updateBike".equalsIgnoreCase(args[0])) {
	           validateArgs(args, 7, new int[] {1, 5, 6});

	           // -updateBike <id> <name> <description> <availabilityDate> <price> <units>

	           try {
	                clientBikeService.updateBike(Long.valueOf(args[1]),
	                        args[2], args[3], stringToCalendar(args[4]),
	                        Float.valueOf(args[5]), Short.valueOf(args[6]));

	                System.out.println("Bike " + args[1] + " updated sucessfully");

	            } catch (NumberFormatException | InstanceNotFoundException ex) {
	                ex.printStackTrace(System.err);
	            } catch (Exception ex) {
	                ex.printStackTrace(System.err);
	            }
		} else if("-findBike".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {1});

            // -findBike <bikeId>
            try {
                ClientBikeDto bike = clientBikeService.findBike(Long.valueOf(args[1]));
                    System.out.println("Id: " + bike.getBikeId() +
                            ", ModelName: " + bike.getModelName() +
                            ", Description: " + bike.getDescription() +
                            ", Start Date: " + bike.getStartDate() +
                            ", Price: " + bike.getPrice() +
                            ", Available Number: " + bike.getAvailableNumber() +
                            ", Number Of Rents: " + bike.getNumberOfRents() +
                            ", Average Score: " + bike.getTotalScore());
                
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
		}
		
	}
	
	public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    -addBike <name> <description> <availabilityDate> <price> <units>\n" +
                "    -updateBike <id> <name> <description> <availabilityDate> <price> <units>\n" +
                "    -findBike <bikeId>\n");
    }
    
	public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
		System.out.println("Primero validate");
		if (expectedArgs != args.length) {
			System.out.println("Segundo validate");
			printUsageAndExit();
		}
		System.out.println("Cuarto validate");
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
		calendar.set(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[1])-1, Integer.parseInt(tokens[2]));
		
		return calendar;
	}
	
}
