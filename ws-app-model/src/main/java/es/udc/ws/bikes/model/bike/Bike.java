package es.udc.ws.bikes.model.bike;

import java.util.Date;

public class Bike {
	//Attributes - Pueden faltar
	String modelName;
	String description;
	Date startDate;
	float price;
	int availableNumber;
	Date adquisitionDate;//Fecha de alta del modelo - nombre puede variar
	//getters
	public String getModelName() {
		return modelName;
	}
	public String getDescription() {
		return description;
	}
	public Date getStartDate() {
		return startDate;
	}
	public float getPrice() {
		return price;
	}
	public int getAvailableNumber() {
		return availableNumber;
	}
	public Date getAdquisitionDate() {
		return adquisitionDate;
	}
	//Los setters no se deberían generar ya que podrían concurrir en modificaciones de información indeseada
	//La información debería ser inicializada con métodos constructores
	// TODO constructores, hash, equals
}
