package es.udc.ws.bikes.model.bike;

import java.util.Calendar;

public class Bike {

	private Long bikeId; // No actualizable
	private String modelName;
	private String description;
	private Calendar startDate; // Fecha y hora desde que se permite alquilar,
								// Parcialmente actualizable
	private float price;
	private int availableNumber;
	private Calendar adquisitionDate; // Fecha y hora de alta del modelo, No
										// actualizable
	private int numberOfRents; // No actualizable
	private double averageScore; // No actualizable

	// Constructor

	public Bike(String modelName, String description, Calendar startDate,
			float price, int availableNumber, Calendar adquisitionDate,
			int numberOfRents, double averageScore) {
		this.modelName = modelName;
		this.description = description;
		this.startDate = startDate;
		this.price = price;
		this.availableNumber = availableNumber;
		this.adquisitionDate = adquisitionDate;
		this.numberOfRents = numberOfRents;
		this.averageScore = averageScore;
	}

	public Bike(Long bikeId, String modelName, String description,
			Calendar startDate, float price, int availableNumber,
			Calendar adquisitionDate, int numberOfRents, double averageScore) {
		this(modelName, description, startDate, price, availableNumber,
				adquisitionDate, numberOfRents, averageScore);
		this.bikeId = bikeId;
	}

	public Bike(Long bikeId, String modelName, String description,
			Calendar startDate, float price, int availableNumber,
			int numberOfRents, double averageScore) {
		this.bikeId = bikeId;
		this.modelName = modelName;
		this.description = description;
		this.startDate = startDate;
		this.price = price;
		this.availableNumber = availableNumber;
		this.numberOfRents = numberOfRents;
		this.averageScore = averageScore;
	}

	public Bike(Long bikeId, String modelName, String description,
			Calendar startDate, float price, int availableNumber) {
		this.bikeId = bikeId;
		this.modelName = modelName;
		this.description = description;
		this.startDate = startDate;
		this.price = price;
		this.availableNumber = availableNumber;
	}

	// getters
	public Long getBikeId() {
		return bikeId;
	}

	public String getModelName() {
		return modelName;
	}

	public String getDescription() {
		return description;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public float getPrice() {
		return price;
	}

	public int getAvailableNumber() {
		return availableNumber;
	}

	public Calendar getAdquisitionDate() {
		return adquisitionDate;
	}

	public int getNumberOfRents() {
		return numberOfRents;
	}

	public double getAverageScore() {
		return averageScore;
	}

	// setters
	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setAvailableNumber(int availableNumber) {
		this.availableNumber = availableNumber;
	}

	public void setAdquisitionDate(Calendar adquisitionDate) {
		this.adquisitionDate = adquisitionDate;
	}

	public void setNumberOfRents(int numberOfRents) {
		this.numberOfRents = numberOfRents;
	}

	public void setAverageScore(double scoreAverage) {
		this.averageScore = scoreAverage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((adquisitionDate == null) ? 0 : adquisitionDate.hashCode());
		result = prime * result + availableNumber;
		long temp;
		temp = Double.doubleToLongBits(averageScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((bikeId == null) ? 0 : bikeId.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((modelName == null) ? 0 : modelName.hashCode());
		result = prime * result + numberOfRents;
		result = prime * result + Float.floatToIntBits(price);
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
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
		Bike other = (Bike) obj;
		if (adquisitionDate == null) {
			if (other.adquisitionDate != null)
				return false;
		} else if (!adquisitionDate.equals(other.adquisitionDate))
			return false;
		if (availableNumber != other.availableNumber)
			return false;
		if (Double.doubleToLongBits(averageScore) != Double
				.doubleToLongBits(other.averageScore))
			return false;
		if (bikeId == null) {
			if (other.bikeId != null)
				return false;
		} else if (!bikeId.equals(other.bikeId))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (modelName == null) {
			if (other.modelName != null)
				return false;
		} else if (!modelName.equals(other.modelName))
			return false;
		if (numberOfRents != other.numberOfRents)
			return false;
		if (Float.floatToIntBits(price) != Float.floatToIntBits(other.price))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

}
