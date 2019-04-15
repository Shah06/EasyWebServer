public class Main {
	
	public static void main(String[] args) {
		
		HttpServer server = new HttpServer();
		Thread serverThread = new Thread(server);
		serverThread.start();
		
	}
	
}