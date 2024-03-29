package es.udc.ws.app.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.dto.ServiceBikeDto;
import es.udc.ws.app.model.bike.Bike;

public class BikeToBikeDtoConversor {

	public static List<ServiceBikeDto> toBikeDtos(List<Bike> bikes) {
		List<ServiceBikeDto> bikesDto = new ArrayList<>(bikes.size());
		for (int i = 0; i < bikes.size(); i++) {
			Bike bike = bikes.get(i);
			bikesDto.add(toBikeDto(bike));
		}
		return bikesDto;
	}

	public static ServiceBikeDto toBikeDto(Bike bike) {
		double averageScore = 0;
		if (bike.getNumberOfScores() != 0) {
			averageScore = bike.getTotalScore() / bike.getNumberOfScores();
		}
		return new ServiceBikeDto(bike.getBikeId(), bike.getModelName(), bike.getDescription(), bike.getStartDate(),
				bike.getPrice(), bike.getAvailableNumber(),bike.getNumberOfRents(),averageScore);
	}

	public static Bike toBike(ServiceBikeDto bike) {
		return new Bike(bike.getBikeId(),bike.getModelName(), bike.getDescription(), bike.getStartDate(), 
				bike.getPrice(),bike.getAvailableNumber());
	}

}
