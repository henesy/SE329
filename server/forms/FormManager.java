package server.forms;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class FormManager {
	private static FormManager instance;
	
	private FormManager() {
		
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
	
	private JSONRequest parseRequest(String requestString) throws RequestParseException {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		JSONRequest request = gson.fromJson(requestString, JSONRequest.class);
		if(request.action != null && request.action != "") {
			throw new RequestParseException();
		}
		if(request.action == LotInformationReport.myAction) {
			request = gson.fromJson(requestString, LotInformationReport.class);
		}
		else if(request.action == LotInformationRequest.myAction) {
			request = gson.fromJson(requestString, LotInformationRequest.class);
		}
		else if(request.action == NewLotReport.myAction) {
			request = gson.fromJson(requestString, NewLotReport.class);
		}
		return request;
	}
	
	private JSONResponse processRequest(JSONRequest request) {
		JSONResponse response = request.getResponder();
		return response;
	}
	
	
}
