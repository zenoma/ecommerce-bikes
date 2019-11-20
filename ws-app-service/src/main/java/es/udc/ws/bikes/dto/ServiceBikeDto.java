package es.udc.ws.bikes.dto;

public class ServiceBikeDto {
	// TODO Repasar campos creacion Dto, comprobar que sean los que corresponden
	private Long bikeId;
	private String modelName;
	private String description;
	private float price;
	private int availableNumber;
	
	public ServiceBikeDto() {	
	}

	public ServiceBikeDto(Long bikeId, String modelName, String description, float price, int availableNumber) {
		this.bikeId = bikeId;
		this.modelName = modelName;
		this.description = description;
		this.price = price;
		this.availableNumber = availableNumber;
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

	@Override
	public String toString() {
		return "ServiceBikeDto [bikeId=" + bikeId + ", modelName=" + modelName + ", description=" + description
				+ ", price=" + price + ", availableNumber=" + availableNumber + "]";
	}
	
	
}
