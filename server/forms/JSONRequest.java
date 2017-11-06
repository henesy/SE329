package server.forms;

public abstract class JSONRequest extends JSONForm {
	public JSONRequest(String action) {
		super(action);
	}
	
	public abstract JSONResponse getResponder();
}
