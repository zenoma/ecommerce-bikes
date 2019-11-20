package es.udc.ws.bikes.dto;

import java.util.Calendar;

public class ServiceRentDto {
	//TODO Repasar campos creacion Dto, comprobar que sean los que corresponden
	private Long rentId;
	private Long bikeId;
	private Calendar startDate;
	private Calendar finishDate;
	private Calendar rentDate;
	private int numberOfBikes;
	
	public ServiceRentDto() {
	}

	public ServiceRentDto(Long rentId, Long bikeId, Calendar startDate, Calendar finishDate, Calendar rentDate,
			int numberOfBikes) {
		this.rentId = rentId;
		this.bikeId = bikeId;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.rentDate = rentDate;
		this.numberOfBikes = numberOfBikes;
	}

	public Long getRentId() {
		return rentId;
	}

	public void setRentId(Long rentId) {
		this.rentId = rentId;
	}

	public Long getBikeId() {
		return bikeId;
	}

	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Calendar finishDate) {
		this.finishDate = finishDate;
	}

	public Calendar getRentDate() {
		return rentDate;
	}

	public void setRentDate(Calendar rentDate) {
		this.rentDate = rentDate;
	}

	public int getNumberOfBikes() {
		return numberOfBikes;
	}

	public void setNumberOfBikes(int numberOfBikes) {
		this.numberOfBikes = numberOfBikes;
	}
}
