package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.List;

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
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class BikesServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Bike Post
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

	@Override
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

}
