package server.forms;

public class FormReceivedResponse extends JSONResponse {
	public static String myAction = "formReceivedResponse";
	
	public FormReceivedResponse() {
		super(myAction);
	}
}
