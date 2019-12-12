package es.udc.ws.app.admin.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ClientBikeServiceFactory {

	private final static String CLASS_NAME_PARAMETER = "AdminClientBikeServiceFactory.className";
	private static Class<ClientBikeService> serviceClass = null;

	private ClientBikeServiceFactory() {
	}

	@SuppressWarnings("unchecked")
	private synchronized static Class<ClientBikeService> getServiceClass() {

		if (serviceClass == null) {
			try {
				String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
				serviceClass = (Class<ClientBikeService>) Class.forName(serviceClassName);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return serviceClass;

	}

	public static ClientBikeService getService() {

		try {
			return (ClientBikeService) getServiceClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

}
