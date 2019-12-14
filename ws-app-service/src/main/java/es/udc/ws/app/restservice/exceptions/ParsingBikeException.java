package es.udc.ws.app.restservice.exceptions;

public class ParsingBikeException extends Exception{
	private String texto;

	public ParsingBikeException(String texto) {
		this.texto = texto;
	}

	@Override
	public String getMessage() {
		return texto;
	}
}
