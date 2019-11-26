package es.udc.ws.bikes.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.bikes.dto.ServiceBikeDto;
import es.udc.ws.bikes.model.bike.Bike;

public class BikeToBikeDtoConversor {

	public static List<ServiceBikeDto> toBikeDtos(List<Bike> movies) {
		List<ServiceBikeDto> movieDtos = new ArrayList<>(movies.size());
		for (int i = 0; i < movies.size(); i++) {
			Bike bike = movies.get(i);
			movieDtos.add(toBikeDto(bike));
		}
		return movieDtos;
	}

	public static ServiceBikeDto toBikeDto(Bike bike) {
		return new ServiceBikeDto(bike.getBikeId(), bike.getModelName(), bike.getDescription(), bike.getStartDate(),
				bike.getPrice(), bike.getAvailableNumber(), bike.getNumberOfRents(), bike.getAverageScore());
	}

	public static Bike toBike(ServiceBikeDto bike) {
		return new Bike(bike.getBikeId(),bike.getModelName(), bike.getDescription(), bike.getStartDate(), 
				bike.getPrice(),bike.getAvailableNumber(), bike.getNumberOfRents(), bike.getAverageScore());
	}

}
