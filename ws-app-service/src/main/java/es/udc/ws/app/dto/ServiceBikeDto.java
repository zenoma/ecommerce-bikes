package es.udc.ws.app.dto;

import java.util.Calendar;

public class ServiceBikeDto {

	private Long bikeId;
	private String modelName;
	private String description;
	private Calendar startDate;
	private float price;
	private int availableNumber;
	private int numberOfRents;
	private double totalScore;
	private int numberOfScores;
	private double averageScore;

	public ServiceBikeDto() {
	}

	/**
	 * Constructor para recibir del modelo.
	 * 
	 * @param bikeId
	 * @param modelName
	 * @param description
	 * @param startDate
	 * @param price
	 * @param availableNumber
	 * @param numberOfRents
	 * @param totalScore
	 */
	public ServiceBikeDto(Long bikeId, String modelName, String description,
			Calendar startDate, float price, int availableNumber,
			int numberOfRents, double totalScore, int numberOfScores) {

		this(bikeId, modelName, description, startDate, price, availableNumber);
		this.numberOfRents = numberOfRents;
		this.totalScore = totalScore;
		this.numberOfScores = numberOfScores;

	}

	/**
	 * Constructor para enviar al cliente.
	 * 
	 * @param bikeId
	 * @param modelName
	 * @param description
	 * @param startDate
	 * @param price
	 * @param availableNumber
	 * @param numberOfRents
	 * @param totalScore
	 */
	public ServiceBikeDto(Long bikeId, String modelName, String description,
			Calendar startDate, float price, int availableNumber,
			int numberOfRents, double averageScore) {

		this(bikeId, modelName, description, startDate, price, availableNumber);
		this.numberOfRents = numberOfRents;
		this.averageScore = averageScore;
	}

	/**
	 * Constructor para recibir del cliente y para mandar al modelo
	 * 
	 * @param bikeId
	 * @param modelName
	 * @param description
	 * @param startDate
	 * @param price
	 * @param availableNumber
	 */
	public ServiceBikeDto(Long bikeId, String modelName, String description,
			Calendar startDate, float price, int availableNumber) {
		this.bikeId = bikeId;
		this.modelName = modelName;
		this.description = description;
		this.startDate = startDate;
		this.price = price;
		this.availableNumber = availableNumber;
	}

	public double getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(int averageScore) {
		this.averageScore = averageScore;
	}

	public int getNumberOfScores() {
		return numberOfScores;
	}

	public void setNumberOfScores(int numberOfScores) {
		this.numberOfScores = numberOfScores;
	}

	public Long getBikeId() {
		return bikeId;
	}

	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getAvailableNumber() {
		return availableNumber;
	}

	public void setAvailableNumber(int availableNumber) {
		this.availableNumber = availableNumber;
	}

	public int getNumberOfRents() {
		return numberOfRents;
	}

	public void setNumberOfRents(int numberOfRents) {
		this.numberOfRents = numberOfRents;
	}

	public double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + availableNumber;
		result = prime * result + ((bikeId == null) ? 0 : bikeId.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((modelName == null) ? 0 : modelName.hashCode());
		result = prime * result + numberOfRents;
		result = prime * result + numberOfScores;
		result = prime * result + Float.floatToIntBits(price);
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		long temp;
		temp = Double.doubleToLongBits(totalScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		ServiceBikeDto other = (ServiceBikeDto) obj;
		if (availableNumber != other.availableNumber)
			return false;
		if (averageScore != other.averageScore)
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
		if (numberOfScores != other.numberOfScores)
			return false;
		if (Float.floatToIntBits(price) != Float.floatToIntBits(other.price))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (Double.doubleToLongBits(totalScore) != Double
				.doubleToLongBits(other.totalScore))
			return false;
		return true;
	}

}
