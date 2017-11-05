package server;
import java.net.Socket;
import java.net.ServerSocket;

public class Main {
	public static void main(String[] args) {
		ServerSocket server = null;
		// Set up a ServerSocket
		while(true) {
			Socket socket = null;
			// Accept a client connection, and create a ThreadedConnection for it using the created socket.
			ThreadedConnection connection = new ThreadedConnection(socket);
			connection.start();
		}
	}
}
