package es.udc.ws.app.user.client.service.dto;

import java.util.Calendar;

public class ClientRentDto {

	private Long rentId;
	private String userEmail;
	private Long bikeId;
	private Long creditCard;
	private Calendar startRentDate;
	private Calendar finishRentDate;
	private int numberOfBikes;
	private Calendar rentDate;
	private float price;

	public ClientRentDto(Long rentId, String userEmail, Long bikeId,
			Long creditCard, Calendar startRentDate, Calendar finishRentDate,
			int numberOfBikes, Calendar rentDate, float price) {
		this(userEmail, bikeId, creditCard, startRentDate, finishRentDate,
				numberOfBikes);
		this.rentDate = rentDate;
		this.price = price;
		this.rentId = rentId;
	}

	public ClientRentDto(String userEmail, Long bikeId, Long creditCard,
			Calendar startRentDate, Calendar finishRentDate,
			int numberOfBikes) {
		this.userEmail = userEmail;
		this.bikeId = bikeId;
		this.creditCard = creditCard;
		this.startRentDate = startRentDate;
		this.finishRentDate = finishRentDate;
		this.numberOfBikes = numberOfBikes;
	}

	public Long getRentId() {
		return rentId;
	}

	public void setRentId(Long rentId) {
		this.rentId = rentId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Long getBikeId() {
		return bikeId;
	}

	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}

	public Long getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(Long creditCard) {
		this.creditCard = creditCard;
	}

	public Calendar getStartRentDate() {
		return startRentDate;
	}

	public void setStartRentDate(Calendar startRentDate) {
		this.startRentDate = startRentDate;
	}

	public Calendar getFinishRentDate() {
		return finishRentDate;
	}

	public void setFinishRentDate(Calendar finishRentDate) {
		this.finishRentDate = finishRentDate;
	}

	public int getNumberOfBikes() {
		return numberOfBikes;
	}

	public void setNumberOfBikes(int numberOfBikes) {
		this.numberOfBikes = numberOfBikes;
	}

	public Calendar getRentDate() {
		return rentDate;
	}

	public void setRentDate(Calendar rentDate) {
		this.rentDate = rentDate;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bikeId == null) ? 0 : bikeId.hashCode());
		result = prime * result
				+ ((creditCard == null) ? 0 : creditCard.hashCode());
		result = prime * result
				+ ((finishRentDate == null) ? 0 : finishRentDate.hashCode());
		result = prime * result + numberOfBikes;
		result = prime * result + Float.floatToIntBits(price);
		result = prime * result
				+ ((rentDate == null) ? 0 : rentDate.hashCode());
		result = prime * result + ((rentId == null) ? 0 : rentId.hashCode());
		result = prime * result
				+ ((startRentDate == null) ? 0 : startRentDate.hashCode());
		result = prime * result
				+ ((userEmail == null) ? 0 : userEmail.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientRentDto other = (ClientRentDto) obj;
		if (bikeId == null) {
			if (other.bikeId != null)
				return false;
		} else if (!bikeId.equals(other.bikeId))
			return false;
		if (creditCard == null) {
			if (other.creditCard != null)
				return false;
		} else if (!creditCard.equals(other.creditCard))
			return false;
		if (finishRentDate == null) {
			if (other.finishRentDate != null)
				return false;
		} else if (!finishRentDate.equals(other.finishRentDate))
			return false;
		if (numberOfBikes != other.numberOfBikes)
			return false;
		if (Float.floatToIntBits(price) != Float.floatToIntBits(other.price))
			return false;
		if (rentDate == null) {
			if (other.rentDate != null)
				return false;
		} else if (!rentDate.equals(other.rentDate))
			return false;
		if (rentId == null) {
			if (other.rentId != null)
				return false;
		} else if (!rentId.equals(other.rentId))
			return false;
		if (startRentDate == null) {
			if (other.startRentDate != null)
				return false;
		} else if (!startRentDate.equals(other.startRentDate))
			return false;
		if (userEmail == null) {
			if (other.userEmail != null)
				return false;
		} else if (!userEmail.equals(other.userEmail))
			return false;
		return true;
	}

}
