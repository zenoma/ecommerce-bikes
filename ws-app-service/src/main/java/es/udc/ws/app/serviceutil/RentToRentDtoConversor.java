package es.udc.ws.app.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.dto.ServiceRentDto;
import es.udc.ws.app.model.rent.Rent;

public class RentToRentDtoConversor {

	public static ServiceRentDto toRentDto(Rent rent) {

		return new ServiceRentDto(rent.getRentId(), rent.getUserEmail(),
				rent.getBikeId(), rent.getCreditCard(), rent.getStartRentDate(),
				rent.getFinishRentDate(), rent.getNumberOfBikes(),
				rent.getRentDate(), rent.getPrice());
	}
	
	public static Rent toRent(ServiceRentDto rent) {

		return new Rent(rent.getRentId(), rent.getUserEmail(),
				rent.getBikeId(), rent.getCreditCard(), rent.getStartRentDate(),
				rent.getFinishRentDate(), rent.getNumberOfBikes(),
				rent.getRentDate(), rent.getPrice());
	}
	
	public static ServiceRentDto toRentDto(Long rent) {

		return new ServiceRentDto(rent);
	}
	
	public static List<ServiceRentDto> toRentDtos(List<Rent> rents){
		List<ServiceRentDto> rentsDto = new ArrayList<ServiceRentDto>(rents.size());
		for (int i = 0; i < rents.size(); i++) {
			Rent rent = rents.get(i);
			rentsDto.add(toRentDto(rent));
		}
		return rentsDto;
	}
}
