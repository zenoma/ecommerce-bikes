package es.udc.ws.bikes.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class NumberOfBikesException extends Exception {

	private Long bikeId;
	private int numberOfBikes;

	public NumberOfBikesException(Long bikeId, int numberOfBikes) {
		super("Bike with id =\"" + bikeId
				+ "\" has not enough existences(Requested =\"" + numberOfBikes
				+ "\")");
		this.bikeId = bikeId;
		this.numberOfBikes = numberOfBikes;
	}

	public int getNumberOfBikes() {
		return numberOfBikes;
	}

	public void setNumberOfBikes(int numberOfBikes) {
		this.numberOfBikes = numberOfBikes;
	}

	public Long getBikeId() {
		return bikeId;
	}

	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}

}
