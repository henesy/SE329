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
		writeToDatabase();
		return new FormReceivedResponse();
	}
	
	private void writeToDatabase() {
		//TODO
	}
}