package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.dto.ServiceBikeDto;
import es.udc.ws.app.model.bike.Bike;
import es.udc.ws.app.model.bikeservice.BikeServiceFactory;
import es.udc.ws.app.model.bikeservice.exceptions.NumberOfBikesException;
import es.udc.ws.app.model.bikeservice.exceptions.UpdateReservedBikeException;
import es.udc.ws.app.restservice.exceptions.ParsingBikeException;
import es.udc.ws.app.restservice.json.JsonServiceBikeDtoConversor;
import es.udc.ws.app.restservice.json.JsonServiceExceptionConversor;
import es.udc.ws.app.serviceutil.BikeToBikeDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class BikesServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path != null && path.length() > 0) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("POST Invalid Request: " + 
								"invalid path " +path)),
					null);
		}

		ServiceBikeDto bikeDto;
		try {
			bikeDto = JsonServiceBikeDtoConversor.toServiceBikeDtoPost(req.getInputStream());
		}catch (ParsingBikeException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
					JsonServiceExceptionConversor.toParsingBikeException(
							ex), null);
			return;
		}
		Bike  bike = BikeToBikeDtoConversor.toBike(bikeDto);
		try {
			bike = BikeServiceFactory.getService().addBike(bike.getModelName(), 
					bike.getDescription(), bike.getStartDate(), 
					bike.getPrice(), bike.getAvailableNumber());
		}catch (InputValidationException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
					JsonServiceExceptionConversor.toInputValidationException(ex), null);
			return;
		}catch (NumberOfBikesException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, 
					JsonServiceExceptionConversor.toNumberOfBikesException(ex), null);
			return;
		}
		bikeDto = BikeToBikeDtoConversor.toBikeDto(bike);
		
		String bikeURL = ServletUtils.normalizePath(req.getRequestURI().toString()) + "/" 
				+ bike.getBikeId();
		Map<String, String> headers = new HashMap<>(1);
		headers.put("Location", bikeURL);
		
		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED, 
				JsonServiceBikeDtoConversor.toObjectNode(bikeDto), headers);
		
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path == null || path.length() == 0) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("PUT Invalid Request: " 
									+ "no id on url")),
					null);
		}
		String bikeIdAsString = path.substring(1);
		Long bikeId;
		try {
			bikeId = Long.valueOf(bikeIdAsString);
		} catch (NumberFormatException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("PUT Invalid Request: " 
									+ "invalid bike id, not a number '" 
									+ bikeIdAsString + "'")),
					null);
			return;
		}
		ServiceBikeDto bikeDto;
		try {
			bikeDto = JsonServiceBikeDtoConversor.toServiceBikeDtoPut(req.getInputStream());
			bikeDto.setBikeId(bikeId);
		} catch (ParsingException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("PUT Invalid Request: " + 
									"Json not well formed")), null);
			return;
		}
		if (!bikeId.equals(bikeDto.getBikeId())) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND, 
				JsonServiceExceptionConversor.toInputValidationException(
						new InputValidationException("PUT Invalid Request: " + 
								"invalid bikeId from url or dto")),
				null);
			return;
		}
		Bike bike = BikeToBikeDtoConversor.toBike(bikeDto);
	
		try {
			BikeServiceFactory.getService().updateBike(bike.getBikeId(), 
					bike.getModelName() , bike.getDescription(), bike.getStartDate(), 
					bike.getPrice(), bike.getAvailableNumber());
		} catch ( InputValidationException  ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
				JsonServiceExceptionConversor.toInputValidationException(
						new InputValidationException("PUT Invalid Request: " + 
								"invalid data to update")),
				null);
			return;
		} 
		catch (InstanceNotFoundException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
				JsonServiceExceptionConversor.toInstanceNotFoundException(ex), null);
			return;
		}catch (UpdateReservedBikeException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, 
					 JsonServiceExceptionConversor.toUpdateReservedBikeException(
						 ex),
				null);
			return;
		}catch (NumberOfBikesException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, 
					 JsonServiceExceptionConversor.toNumberOfBikesException(
						 ex),
				null);
			return;
		}
		
		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
	}
	

	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path == null || path.length() == 0) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("DELETE Invalid Request: " + 
									"invalid bike id")),
					null);
			return;
		}
		String bikeIdAsString = path.substring(1);
		//System.out.println(bikeIdAsString);
		Long bikeId;
		try {
			bikeId = Long.valueOf(bikeIdAsString);
		} catch (NumberFormatException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("DELETE Invalid Request: " 
									+ "invalid bike id, not a number '" 
									+ bikeIdAsString + "'")),
					null);
			return;
		}
		try {
			BikeServiceFactory.getService().removeBike(bikeId);
		}catch (InstanceNotFoundException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
					JsonServiceExceptionConversor.toInstanceNotFoundException(ex), null);
			return;
		}
		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);*/
	}
	
	
	 protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// FIXME Añadir fechas OBLIGATORIAS
		String path = ServletUtils.normalizePath(req.getPathInfo());
		System.out.println(path.length());
		if (path == null || path.length() == 0) {
				String keywords = req.getParameter("keywords");
				Calendar calendar = getDate(req.getParameter("date"));
				List<Bike> bikes = BikeServiceFactory.getService()
						.findBikes(keywords, calendar);
				List<ServiceBikeDto> bikesDto = BikeToBikeDtoConversor
						.toBikeDtos(bikes);
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
						JsonServiceBikeDtoConversor.toArrayNode(bikesDto), null);
		}
		else {
			String bikeIdString = path.substring(1);
			Long bikeId;
			if (bikeIdString != null) {
				bikeId = Long.valueOf(bikeIdString);
				try {
					Bike bike = BikeServiceFactory.getService().findBike(bikeId);
					ServiceBikeDto bikeDto = BikeToBikeDtoConversor.toBikeDto(bike);
					ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
							JsonServiceBikeDtoConversor.toObjectNode(bikeDto), null);
				} catch (InstanceNotFoundException ex) {
					ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
							JsonServiceExceptionConversor.toInstanceNotFoundException(ex), null);
					return;
				}
			}else {
				ServletUtils.writeServiceResponse(resp,
						HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(
								new InputValidationException("GET Invalid Request: " +" invalid path : " + path)),
						null);
			}
	 	}
	 }
		
	private static Calendar getDate(String date) {
		String[] dateSplit = date.split("-");
		
		int day = Integer.valueOf(dateSplit[0]);
		int month = Integer.valueOf(dateSplit[1]);
		int year = Integer.valueOf(dateSplit[2]);
		
		Calendar dateAvailableAux = Calendar.getInstance();
		dateAvailableAux.set(Calendar.DAY_OF_MONTH,day);
		dateAvailableAux.set(Calendar.MONTH,month - 1);
		dateAvailableAux.set(Calendar.YEAR,year);
		
		return dateAvailableAux;
	}

}
