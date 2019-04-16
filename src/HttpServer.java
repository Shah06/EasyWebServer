import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Runnable {
	
	private ServerSocket ss;
	private final int PORTNUMBER;
	
	public HttpServer(int portNumber) {
		this.PORTNUMBER = portNumber;
	}
	
	public HttpServer() {
		this(80);
	}
	
	public void run() {
		try {
			ss = new ServerSocket(PORTNUMBER);
			System.out.println("Started EasyWebServer on " + InetAddress.getLocalHost() + ":" + PORTNUMBER + "\n");
			while (true) {
				// accept a client
				Socket incoming = ss.accept();
				
				// handle a client
				Thread clientConnection = new Thread(new ClientHandler(incoming, "test.html"));
				System.out.println("REQUEST ID: " + clientConnection.getId());
				clientConnection.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
