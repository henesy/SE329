package server.forms;

public class InvalidFormReceivedResponse extends JSONResponse {
	public static String myAction = "invalidFormReceivedResponse";
	public String message = null;
	
	public InvalidFormReceivedResponse() {
		super(myAction);
	}
}
