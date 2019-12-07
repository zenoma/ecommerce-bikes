package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.dto.ServiceRentDto;
import es.udc.ws.app.restservice.json.JsonServiceExceptionConversor;
import es.udc.ws.app.serviceutil.RentToRentDtoConversor;
import es.udc.ws.bikes.model.bikeservice.BikeServiceFactory;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidRentPeriodException;
import es.udc.ws.bikes.model.bikeservice.exceptions.NumberOfBikesException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class RentsServlet extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Rent Post
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path != null && path.length() > 0) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("POST Invalid Request: " 
									+ "invalid path " + path)),
					null);
			return;
		}
		String bikeIdParameter = req.getParameter("bikeId");
		if (bikeIdParameter == null) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
            		JsonServiceExceptionConversor.toInputValidationException(
                            new InputValidationException("POST Invalid Request: " 
                            		+ "parameter 'bikeId' is mandatory")),
                    null);
            return;
		}
		Long bikeId;
		try {
			bikeId = Long.valueOf(bikeIdParameter);
		}catch(NumberFormatException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
            		JsonServiceExceptionConversor.toInputValidationException(
            				new InputValidationException("POST Invalid Request: " 
            						+ "parameter 'bikeId' is invalid '" + bikeIdParameter 
            						+ "'")),
                    null);
			return;
		}
		String userEmail = req.getParameter("userEmail");
		if (userEmail == null) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
            		JsonServiceExceptionConversor.toInputValidationException(
                            new InputValidationException("POST Invalid Request: " 
                            		+ "parameter 'userEmail' is mandatory")),
                    null);
            return;
		}
		String creditCardNumberParameter = req.getParameter("creditCardNumber");
		if (creditCardNumberParameter == null) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
            		JsonServiceExceptionConversor.toInputValidationException(
                            new InputValidationException("POST Invalid Request: " 
                            		+ "parameter 'creditCardNumber' is mandatory")),
                    null);
            return;
		}
		Long creditCardNumber;
		try {
			creditCardNumber = Long.valueOf(creditCardNumberParameter);
		}catch(NumberFormatException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
            		JsonServiceExceptionConversor.toInputValidationException(
            				new InputValidationException("POST Invalid Request: " 
            						+ "parameter 'creditCardNumber' is invalid '" 
            						+ creditCardNumberParameter + "'")),
                    null);
			return;
		}
		String startRentDateString = req.getParameter("startRentDate");
		Calendar startRentDate;
		try {
			startRentDate = getDate(startRentDateString);
		}catch(InputValidationException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
            		JsonServiceExceptionConversor.toInputValidationException(
            				new InputValidationException("Invalid Request: " 
            						+ "parameter 'startRentDate' is invalid '" + 
            						startRentDateString + "'")),
                    null);
			return;
		}
		
		String finishRentDateString = req.getParameter("finishRentDate");
		Calendar finishRentDate;
		try {
			finishRentDate = getDate(finishRentDateString);
		}catch(InputValidationException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
            		JsonServiceExceptionConversor.toInputValidationException(
            				new InputValidationException("Invalid Request: " 
            						+ "parameter 'finishRentDate' is invalid '" + 
            						finishRentDateString + "'")),
                    null);
			return;
		}
		
		String numberOfBikesParameter = req.getParameter("numberOfBikes");
		if (numberOfBikesParameter == null) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
            		JsonServiceExceptionConversor.toInputValidationException(
                            new InputValidationException("POST Invalid Request: " 
                            		+ "parameter 'numberOfBikes' is mandatory")),
                    null);
            return;
		}
		int numberOfBikes;
		try {
			numberOfBikes = Integer.valueOf(numberOfBikesParameter);
		}catch(NumberFormatException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
            		JsonServiceExceptionConversor.toInputValidationException(
            				new InputValidationException("POST Invalid Request: " 
            						+ "parameter 'numberOfBikes' is invalid '" 
            						+ numberOfBikesParameter + "'")),
                    null);
			return;
		}
		
		Long rentLong;
		try {
			rentLong = BikeServiceFactory.getService().rentBike(userEmail, 
					creditCardNumber, bikeId, 
					startRentDate, finishRentDate, numberOfBikes);
		}catch(InstanceNotFoundException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
            		JsonServiceExceptionConversor.toInstanceNotFoundException(ex), null);
            return;
		}catch(InvalidRentPeriodException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
            		JsonServiceExceptionConversor.toInvalidRentPeriodException(ex), null);
            return;
		}catch (NumberOfBikesException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
            		JsonServiceExceptionConversor.toNumberOfBikesException(ex), null);
            return;
		}catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
            		JsonServiceExceptionConversor.toInputValidationException(ex), null);
            return;
        }
		//FIXME Hay que poder devolver el alquiler que se ha creado de alguna manera
		//actualmente no se puede
	}
	/*
	 * Los alquileres no se deberian de poder modificar
	 * */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Rent Put
		//super.doPut(req, resp);
	}
	/*
	 * Los alquileres no se deberian de poder eliminar
	 * */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Rent Delete
		//super.doDelete(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Rent Get
		//super.doGet(req, resp);
	}
	
	private static Calendar getDate(String date) throws InputValidationException {
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
