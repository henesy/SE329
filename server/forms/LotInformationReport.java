package server.forms;

public class LotInformationReport extends JSONRequest {
	public static String myAction = "lotInfoReport";
	public int id;
	public String description;
	
	public LotInformationReport() {
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
