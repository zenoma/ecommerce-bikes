package es.udc.ws.bikes.model.rent;

import java.util.Calendar;

public class Rent {

	//Attributes
	long rentId;
	String userEmail;
	String modelName;
	Integer creditCard;//puede que necesite otro tipo por la longitud del código de als tarjetas
	Calendar startRentDate;
	Calendar finishRentDate;
	int numberOfBikes;
	Calendar rentDate;


	public Rent(String userEmail, String modelName, Integer creditCard, Calendar startRentDate, Calendar finishRentDate,
			int numberOfBikes) {
		this.userEmail = userEmail;
		this.modelName = modelName;
		this.creditCard = creditCard;
		this.startRentDate = startRentDate;
		this.finishRentDate = finishRentDate;
		this.numberOfBikes = numberOfBikes;
	}

	// Getters & Setters
	public long getRentID() {
		return rentId;
	}


	public void setRentID(long rentId) {
		this.rentId = rentId;
	}


	public String getUserEmail() {
		return userEmail;
	}


	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	public String getModelName() {
		return modelName;
	}


	public void setModelName(String modelName) {
		this.modelName = modelName;
	}


	public Integer getCreditCard() {
		return creditCard;
	}


	public void setCreditCard(Integer creditCard) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creditCard == null) ? 0 : creditCard.hashCode());
		result = prime * result + ((finishRentDate == null) ? 0 : finishRentDate.hashCode());
		result = prime * result + ((modelName == null) ? 0 : modelName.hashCode());
		result = prime * result + numberOfBikes;
		result = prime * result + ((rentDate == null) ? 0 : rentDate.hashCode());
		result = prime * result + (int) (rentId ^ (rentId >>> 32));
		result = prime * result + ((startRentDate == null) ? 0 : startRentDate.hashCode());
		result = prime * result + ((userEmail == null) ? 0 : userEmail.hashCode());
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
		Rent other = (Rent) obj;
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
		if (modelName == null) {
			if (other.modelName != null)
				return false;
		} else if (!modelName.equals(other.modelName))
			return false;
		if (numberOfBikes != other.numberOfBikes)
			return false;
		if (rentDate == null) {
			if (other.rentDate != null)
				return false;
		} else if (!rentDate.equals(other.rentDate))
			return false;
		if (rentId != other.rentId)
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
