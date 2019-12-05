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
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class BikesServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Bike Post
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
		try {
			bike = BikeServiceFactory.getService().addBike(bikeDto.getModelName(), 
					bikeDto.getDescription(), bikeDto.getStartDate(), 
					bikeDto.getPrice(), bikeDto.getAvailableNumber());
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
		//super.doPut(req, resp);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Bike Delete
		//super.doDelete(req, resp);
	}
	/*
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Bike Get
		Long bikeId;
		Bike bike;
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path != null && (req.getParameter("keywords")!=null) && (req.getParameter("dateAvailable")!=null)) {
			// FIXME Añadir fechas OBLIGATORIAS
			String keywords = req.getParameter("keywords");
			Calendar dateAvailable = null;
			dateAvailable = getDate(req.getParameter("dateAvailable"));
			List<Bike> bikes = BikeServiceFactory.getService()
					.findBikes(keywords, dateAvailable);
			List<ServiceBikeDto> bikesDto = BikeToBikeDtoConversor
					.toBikeDtos(bikes);
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
					JsonServiceBikeDtoConversor.toArrayNode(bikesDto), null);
		} else if (path != null && (req.getParameter("id")!=null)){
			String bikeIdString = req.getParameter("id");
			try {
				bikeId = Long.valueOf(bikeIdString);
			}catch (NumberFormatException ex) {
				ServletUtils.writeServiceResponse(resp,
						HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(
								new InputValidationException("Invalid bike Id: " +path)),
						null);
				return;
			}
			try {
				bike = BikeServiceFactory.getService().findBike(bikeId);
			}catch(InstanceNotFoundException ex){
				ServletUtils.writeServiceResponse(resp,
						HttpServletResponse.SC_NOT_FOUND,
						JsonServiceExceptionConversor.toInputValidationException(
								new InputValidationException("Bike Id not found: " +path)),
						null);
				return;
			}
		}else {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " +path)),
					null);
		}
		super.doGet(req, resp);
	}*/
	
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
		super.doGet(req, resp);
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
