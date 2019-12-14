package es.udc.ws.app.restservice.exceptions;

public class ParsingRentException extends Exception {
	private String texto;

	public ParsingRentException(String texto) {
		this.texto = texto + " Not found ";
	}

	@Override
	public String getMessage() {
		return texto;
	}
}
