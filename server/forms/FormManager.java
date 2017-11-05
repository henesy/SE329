package server.forms;
import java.util.Map;

public class FormManager {
	private static FormManager instance;
	
	private FormManager() {
		
	}
	
	private void registerForms() {
		
	}
	
	public static FormManager getInstance() {
		if(instance != null) {
			instance = new FormManager();
		}
		return instance;
	}

	public String generateResponse(String requestString) {
		JSONResponse response = null;
		try {
			JSONRequest request = parseRequest(requestString);
			response = processRequest(request);
		} catch (RequestParseException e) {
			response = new InvalidFormReceivedResponse();
		}
		String responseString = response.toJSON();
		return responseString;
	}
	
	private JSONRequest parseRequest(String request) throws RequestParseException {
		return null;
	}
	
	private JSONResponse processRequest(JSONRequest request) {
		return null;
	}
	
	
}
