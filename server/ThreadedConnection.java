package server;
import java.net.Socket;
import server.forms.FormManager;

public class ThreadedConnection extends Thread {
	private Socket socket;
	
	public ThreadedConnection(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		String request = "";
		// Read request from the socket into request string
		
		FormManager formHandler = FormManager.getInstance();
		String response = formHandler.generateResponse(request);

		
		// Write response to socket
		
	}
	
}
