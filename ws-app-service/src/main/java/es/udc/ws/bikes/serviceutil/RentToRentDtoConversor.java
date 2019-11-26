package es.udc.ws.bikes.serviceutil;

import es.udc.ws.bikes.dto.ServiceRentDto;
import es.udc.ws.bikes.model.rent.Rent;

public class RentToRentDtoConversor {

	public static ServiceRentDto toRentDto(Rent rent) {

		return new ServiceRentDto(rent.getRentId(), rent.getUserEmail(),
				rent.getBikeId(), rent.getCreditCard(), rent.getStartRentDate(),
				rent.getFinishRentDate(), rent.getNumberOfBikes(),
				rent.getRentDate(), rent.getPrice());
	}
}
