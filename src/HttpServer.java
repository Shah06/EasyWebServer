import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Runnable {
	
	private ServerSocket ss;
	
	public void run() {
		try {
			ss = new ServerSocket(80);
			while (true) {
				// accept a client
				Socket incoming = ss.accept();
				
				// handle a client
				Thread clientConnection = new Thread(new ClientHandler(incoming, "test.html"));
				clientConnection.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
