package server.forms;

public class NewLotReport extends JSONRequest {
	public static String myAction = "newLotReport";
	public String latitude;
	public String longitude;
	public String description;
	
	public NewLotReport() {
		super(myAction);
	}

	@Override
	public JSONResponse getResponder() {
		// Add the report to the database
		return new FormReceivedResponse();
	}
}