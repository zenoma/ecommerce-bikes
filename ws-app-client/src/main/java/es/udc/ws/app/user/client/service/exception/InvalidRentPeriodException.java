package es.udc.ws.app.user.client.service.exception;

@SuppressWarnings("serial")
public class InvalidRentPeriodException extends Exception {

	private String startDate;
	private String finishDate;

	public InvalidRentPeriodException(String startDate, String finishDate) {

		super("Invalid reservation period: Period between " + startDate
				+ " and " + finishDate + " is greater than 15 days.");
		this.setStartDate(startDate);
		this.setFinishDate(finishDate);
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

}
