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
			validateArgsAddBike(args);
			
			//addBike <name> <description> <availabilityDate> <price> <units>
			
			try {
				
                Long bikeId = clientBikeService.addBike(args[1], args[2],  
                		stringToCalendar(args[3]), Float.valueOf(args[4]), Short.valueOf(args[5])).getBikeId();

                System.out.println("Bike " + bikeId + " created sucessfully");
                
            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
			
		}  else if("-updateBike".equalsIgnoreCase(args[0])) {
	           validateArgsUpdateBike(args);

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
            validateArgsFindBike(args);

            // -findBike <bikeId>
            try {
                ClientBikeDto bike = clientBikeService.findBike(Long.valueOf(args[1]));
                    System.out.println("Id: " + bike.getBikeId() +
                            ", ModelName: " + bike.getModelName() +
                            ", Description: " + bike.getDescription() +
                            ", Start Date: " + bike.getStartDate().getTime() +
                            ", Price: " + bike.getPrice() +
                            ", Available Number: " + bike.getAvailableNumber() +
                            ", Number Of Rents: " + bike.getNumberOfRents() +
                            ", Average Score: " + bike.getAverageScore());
                
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
    
	public static void validateArgsAddBike(String[] args) {
		int expectedArgs = 6;
		int[] numericArguments = {4, 5};
		
		if (expectedArgs > args.length) {
			System.out.println("Inserted number of arguments less than necessary");
			System.out.println("Usage: -addBike <name> <description> <availabilityDate> <price> <units>");
			System.exit(-1);
		} 
		
		if (expectedArgs < args.length) {
			System.out.println("Inserted number of arguments greater than necessary");
			System.out.println("Usage: -addBike <name> <description> <availabilityDate> <price> <units>");
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
	
	public static void validateArgsUpdateBike(String[] args) {
		int expectedArgs = 7;
		int[] numericArguments = {1, 5, 6};
		
		if (expectedArgs > args.length) {
			System.out.println("Inserted number of arguments less than necessary");
			System.out.println("Usage: -updateBike <id> <name> <description> <availabilityDate> <price> <units>");
			System.exit(-1);
		} 
		
		if (expectedArgs < args.length) {
			System.out.println("Inserted number of arguments greater than necessary");
			System.out.println("Usage: -updateBike <id> <name> <description> <availabilityDate> <price> <units>");
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
	
	public static void validateArgsFindBike(String[] args) {
		int expectedArgs = 2;
		int[] numericArguments = {1};
		
		if (expectedArgs > args.length) {
			System.out.println("Inserted number of arguments less than necessary");
			System.out.println("Usage: -findBike <bikeId>");
			System.exit(-1);
		} 
		
		if (expectedArgs < args.length) {
			System.out.println("Inserted number of arguments greater than necessary");
			System.out.println("Usage: -findBike <bikeId>");
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
		calendar.set(Integer.parseInt(tokens[2]), month-1, day);
		
		return calendar;
	}
	
}
