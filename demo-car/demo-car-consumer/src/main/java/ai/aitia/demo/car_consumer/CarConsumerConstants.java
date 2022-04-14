package ai.aitia.demo.car_consumer;

public class CarConsumerConstants {
	
	//=================================================================================================
	// members
	
	public static final String BASE_PACKAGE = "ai.aitia";
	
	public static final String INTERFACE_SECURE = "HTTP-SECURE-JSON";
	public static final String INTERFACE_INSECURE = "HTTP-INSECURE-JSON";
	public static final String HTTP_METHOD = "http-method";
	
	
	/* -------------------------------------------------------------------- */
	public static final String ADMIN_INTERFACE_SERVICE_DEFINITION = "admin-interface";
	public static final String QUERY_INTERFACE_SERVICE_DEFINITION = "query-interface";
	public static final String ADMIN_INTERFACE_URI = "/pai";
	public static final String QUERY_INTERFACE_URI = "/pqi";
	public static final String ADMIN_TOKEN = "admin_token";
	/* -------------------------------------------------------------------- */
	
	
	public static final String CREATE_CAR_SERVICE_DEFINITION = "create-car";
	public static final String GET_CAR_SERVICE_DEFINITION = "get-car";
	public static final String REQUEST_PARAM_KEY_BRAND = "request-param-brand";
	public static final String REQUEST_PARAM_KEY_COLOR = "request-param-color";
	
	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	private CarConsumerConstants() {
		throw new UnsupportedOperationException();
	}

}
