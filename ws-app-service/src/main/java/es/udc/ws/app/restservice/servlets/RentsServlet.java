package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.dto.ServiceRentDto;
import es.udc.ws.app.restservice.exceptions.NotAllowedException;
import es.udc.ws.app.restservice.exceptions.ParsingRentException;
import es.udc.ws.app.restservice.json.JsonServiceExceptionConversor;
import es.udc.ws.app.restservice.json.JsonServiceRentDtoConversor;
import es.udc.ws.app.serviceutil.RentToRentDtoConversor;
import es.udc.ws.app.model.bikeservice.BikeServiceFactory;
import es.udc.ws.app.model.bikeservice.exceptions.InvalidRentPeriodException;
import es.udc.ws.app.model.bikeservice.exceptions.InvalidUserRateException;
import es.udc.ws.app.model.bikeservice.exceptions.NumberOfBikesException;
import es.udc.ws.app.model.bikeservice.exceptions.RentAlreadyRatedException;
import es.udc.ws.app.model.bikeservice.exceptions.RentExpirationException;
import es.udc.ws.app.model.rent.Rent;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class RentsServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path != null && path.length() > 0) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException(
									"POST Invalid Request: " + "invalid path "
											+ path)),
					null);
			return;
		}

		ServiceRentDto rentDto;
		try {
			rentDto = JsonServiceRentDtoConversor
					.toServiceRentDtoPost(req.getInputStream());
		} catch (ParsingRentException ex) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toParsingRentException(ex),
					null);
			return;
		}
		Rent rent = RentToRentDtoConversor.toRent(rentDto);
		Long rentLong;
		try {
			rentLong = BikeServiceFactory.getService().rentBike(
					rent.getUserEmail(), rent.getCreditCard(), rent.getBikeId(),
					rent.getStartRentDate(), rent.getFinishRentDate(),
					rent.getNumberOfBikes());
		} catch (InputValidationException ex) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor
							.toInputValidationException(ex),
					null);
			return;
		} catch (NumberOfBikesException ex) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_FORBIDDEN,
					JsonServiceExceptionConversor.toNumberOfBikesException(ex),
					null);
			return;
		} catch (InstanceNotFoundException ex) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_NOT_FOUND,
					JsonServiceExceptionConversor
							.toInstanceNotFoundException(ex),
					null);
			return;
		} catch (InvalidRentPeriodException ex) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor
							.toInvalidRentPeriodException(ex),
					null);
			return;
		}
		rent.setRentId(rentLong);
		rentDto = RentToRentDtoConversor.toRentDto(rent);
		String rentURL = ServletUtils.normalizePath(
				req.getRequestURI().toString()) + "/" + rent.getRentId();

		Map<String, String> headers = new HashMap<>(1);
		headers.put("Location", rentURL);

		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
				JsonServiceRentDtoConversor.toObjectNode(rentDto), headers);
	}

	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());
		String rentIdAsString = path.substring(1);
		String scoreString = (req.getParameter("score"));
		String userEmail = (req.getParameter("userEmail"));

		int score = 0;
		if (scoreString != null) {
			score = Integer.valueOf(scoreString);
		} else {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException(
									"PUT Invalid Request: " + "invalid score")),
					null);
		}

		if (userEmail == null) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("PUT Invalid Request: "
									+ "invalid empty email")),
					null);
		}

		Long rentId;
		try {
			rentId = Long.valueOf(rentIdAsString);
		} catch (NumberFormatException ex) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("PUT Invalid Request: "
									+ "invalid bike id, not a number '"
									+ rentIdAsString + "'")),
					null);
			return;
		}

		try {
			BikeServiceFactory.getService().rateRent(rentId, score, userEmail);
		} catch (ParsingException | InputValidationException ex) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toParsingRentException(
							new ParsingRentException("PUT Invalid Request: "
									+ "invalid format")),
					null);
			return;
		} catch (InstanceNotFoundException e) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_NOT_FOUND,
					JsonServiceExceptionConversor
							.toInstanceNotFoundException(e),
					null);
			return;
		} catch (RentExpirationException e) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_FORBIDDEN,
					JsonServiceExceptionConversor.toRentExpirationException(e),
					null);
			return;
		} catch (RentAlreadyRatedException e) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_FORBIDDEN,
					JsonServiceExceptionConversor
							.toRentAlreadyRatedException(e),
					null);
			return;
		} catch (InvalidUserRateException e) {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInvalidUserRateException(e),
					null);
			return;
		}

		ServletUtils.writeServiceResponse(resp,
				HttpServletResponse.SC_NO_CONTENT, null, null);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ServletUtils.writeServiceResponse(resp,
				HttpServletResponse.SC_METHOD_NOT_ALLOWED,
				JsonServiceExceptionConversor
						.toNotAllowedException(new NotAllowedException(
								"This method is not supported")),
				null);

		return;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path == null || path.length() == 0) {
			String rentEmail = req.getParameter("userEmail");
			if (rentEmail != null) {
				List<Rent> rents = null;
				try {
					rents = BikeServiceFactory.getService()
							.findRents(rentEmail);
				} catch (InputValidationException ex) {
					ServletUtils.writeServiceResponse(resp,
							HttpServletResponse.SC_BAD_REQUEST,
							JsonServiceExceptionConversor
									.toInputValidationException(
											new InputValidationException(
													"GET Invalid Request: "
															+ "invalid user email")),
							null);
					return;
				}
				List<ServiceRentDto> rentsDto = RentToRentDtoConversor
						.toRentDtos(rents);
				ServletUtils.writeServiceResponse(resp,
						HttpServletResponse.SC_OK,
						JsonServiceRentDtoConversor.toArrayNode(rentsDto),
						null);
			}
		} else {
			ServletUtils.writeServiceResponse(resp,
					HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("GET Invalid Request: "
									+ "invalid path : " + path)),
					null);
			return;
		}
	}

}
