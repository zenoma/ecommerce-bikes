package es.udc.ws.app.restservice.exceptions;

public class NotAllowedException extends Exception{
	private String texto;

	public NotAllowedException(String texto) {
		this.texto = texto;
	}

	@Override
	public String getMessage() {
		return texto;
	}
}
