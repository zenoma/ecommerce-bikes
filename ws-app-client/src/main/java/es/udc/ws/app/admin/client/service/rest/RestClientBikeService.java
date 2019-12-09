package es.udc.ws.app.admin.client.service.rest;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import es.udc.ws.app.admin.client.service.ClientBikeService;
import es.udc.ws.app.admin.client.service.dto.ClientBikeDto;
import es.udc.ws.app.admin.client.service.rest.json.JsonClientBikeDtoConversor;
import es.udc.ws.app.admin.client.service.rest.json.JsonClientExceptionConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.exceptions.ParsingException;

public class RestClientBikeService implements ClientBikeService {
	
	private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientBikeService.endpointAddress";
    private String endpointAddress;

	@Override
	public ClientBikeDto addBike(String modelName, String description, Calendar startDate, float price,
			int availableNumber) {
		/*
		 try {

	            HttpResponse response = Request.Post(getEndpointAddress() + "bikes").
	                    bodyStream(toInputStream(bike), ContentType.create("application/json")).
	                    execute().returnResponse();

	            validateStatusCode(HttpStatus.SC_CREATED, response);

	            return JsonClientMovieDtoConversor.toClientMovieDto(response.getEntity().getContent()).getMovieId();

	        } catch (InputValidationException e) {
	            throw e;
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }

	    }
		*/
		return null;
	}

	@Override
	public void updateBike(Long bikeId, String modelName, String description, Calendar startDate, float price,
			int availableNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ClientBikeDto findBike(Long bikeId) {
		
		try {

            HttpResponse response = Request.Get(getEndpointAddress() + "bikes?bikeId="
                            + URLEncoder.encode(bikeId.toString(), "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonClientBikeDtoConversor.toClientBikeDto(response.getEntity()
                    .getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		
    }
	
	private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

	private void validateStatusCode(int successCode, HttpResponse response)
            throws InstanceNotFoundException,
            InputValidationException, ParsingException {

        try {

            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {

                case HttpStatus.SC_NOT_FOUND:
                    throw JsonClientExceptionConversor.fromInstanceNotFoundException(
                            response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonClientExceptionConversor.fromInputValidationException(
                            response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
	
}
