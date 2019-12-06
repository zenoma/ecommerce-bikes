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
import es.udc.ws.app.restservice.json.JsonServiceBikeDtoConversor;
import es.udc.ws.app.restservice.json.JsonServiceExceptionConversor;
import es.udc.ws.app.serviceutil.BikeToBikeDtoConversor;
import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.bikes.model.bikeservice.BikeServiceFactory;
import es.udc.ws.bikes.model.bikeservice.exceptions.NumberOfBikesException;
import es.udc.ws.bikes.model.bikeservice.exceptions.UpdateReservedBikeException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class BikesServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// FIXME startDate se establece siempre a enero del año siguiente al que pongas
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path != null && path.length() > 0) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + 
								"invalid path " +path)),
					null);
		}
		
		ServiceBikeDto bikeDto;
		try {
			bikeDto = JsonServiceBikeDtoConversor.toServiceBikeDto(req.getInputStream());
		}catch (ParsingException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException(ex.getMessage())), null);
			return;
		}
		Bike  bike = BikeToBikeDtoConversor.toBike(bikeDto);
		Calendar date = bike.getStartDate();
		Integer yearBike = date.get(Calendar.YEAR);
		date.set(Calendar.MILLISECOND, 0);
		date.set(Calendar.SECOND, 0);
		bike.setStartDate(date);
		//System.out.println(yearBike);
		System.out.println(yearBike);
		try {
			bike = BikeServiceFactory.getService().addBike(bike.getModelName(), 
					bike.getDescription(), bike.getStartDate(), 
					bike.getPrice(), bike.getAvailableNumber());
		}catch (InputValidationException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
					JsonServiceExceptionConversor.toInputValidationException(ex), null);
			return;
		}catch (NumberOfBikesException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
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
		
		//super.doPost(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Bike Put
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path == null || path.length() == 0) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " 
									+ "nos bike id")),
					null);
		}
		String bikeIdAsString = path.substring(1);
		//System.out.println(bikeIdAsString);
		Long bikeId;
		try {
			bikeId = Long.valueOf(bikeIdAsString);
		} catch (NumberFormatException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " 
									+ "invalid bike id '" + bikeIdAsString + "'")),
					null);
			return;
		}
		ServiceBikeDto bikeDto;
		try {
			bikeDto = JsonServiceBikeDtoConversor.toServiceBikeDto(req.getInputStream());
		} catch (ParsingException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, JsonServiceExceptionConversor
					.toInputValidationException(new InputValidationException(ex.getMessage())), null);
			return;
		}
		//FIXME bikeDto no devuelve bien el dato
		if (!bikeId.equals(bikeDto.getBikeId())) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + 
									"invalid bikeId")),
					null);
			System.out.println(bikeId);
			System.out.println(bikeDto.getBikeId());
			return;
		}
		Bike bike = BikeToBikeDtoConversor.toBike(bikeDto);
	
		try {
			BikeServiceFactory.getService().updateBike(bike.getBikeId(), 
					bike.getModelName() , bike.getDescription(), bike.getStartDate(), 
					bike.getPrice(), bike.getAvailableNumber());
		} catch (InstanceNotFoundException | InputValidationException | 
					UpdateReservedBikeException | NumberOfBikesException e) {
			e.printStackTrace();
		}
		
		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Bike Delete
		//super.doDelete(req, resp);
	}
	
	 protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Bike Get
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path == null || path.length() == 0) {
			// FIXME Añadir fechas OBLIGATORIAS
			String keywords = req.getParameter("keywords");
			List<Bike> bikes = BikeServiceFactory.getService()
					.findBikes(keywords, null);
			List<ServiceBikeDto> bikesDto = BikeToBikeDtoConversor
					.toBikeDtos(bikes);
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
					JsonServiceBikeDtoConversor.toArrayNode(bikesDto), null);
		} else {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " +path)),
					null);
		}
		//super.doGet(req, resp);
	 }
		
	private static Calendar getDate(String date) {
		String[] dateSplit = date.split("-");
		
		int day = Integer.valueOf(dateSplit[0]);
		int month = Integer.valueOf(dateSplit[1]);
		int year = Integer.valueOf(dateSplit[2]);
		
		Calendar dateAvailableAux = Calendar.getInstance();
		dateAvailableAux.set(Calendar.DAY_OF_MONTH,day);
		dateAvailableAux.set(Calendar.MONTH,month);
		dateAvailableAux.set(Calendar.YEAR,year);
		
		return dateAvailableAux;
	}

}
