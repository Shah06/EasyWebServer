import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class ClientHandler implements Runnable {

	Socket socket;
	FileInputStream fis;
	
	public ClientHandler (Socket socket, String filepath) {
		this.socket = socket;
		try {
			fis = new FileInputStream(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		if (null == fis) {
			System.out.println("ERROR.... FILE NOT FOUND");
			return;
		}
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter output = new PrintWriter(socket.getOutputStream());
			OutputStream sos = socket.getOutputStream();
			
			// send and receive data (implement concurrency)
			if (input != null) {
				
				// add header information to httpHeader
				String httpHeader = new String();
				String temp = input.readLine();
				while (!temp.equals("")) {
					httpHeader += temp + "\n";
					temp = input.readLine();
				}
				
				
				
				// test to print out the requested filename
				String[] headerLines = httpHeader.split("\\r?\\n");
				// headerLines[0] contains the GET request
				String getRequest = headerLines[0].substring(5, headerLines[0].lastIndexOf(" "));
				System.out.println(getRequest);
				// check for 404
				try {
					
					fis = new FileInputStream(getRequest);
					
					output.println("HTTP/1.1 200 OK");
					output.println("Server: Java HTTP Server from @shah06 : 1.0");
					output.println("Date: " + new Date());
					output.println();
					output.flush();
					
					// write the file out
					int available = fis.available();
					byte[] bytes = new byte[available];
					fis.read(bytes);
					sos.write(bytes);
					
				} catch (FileNotFoundException fnfe) {
					
					fis = new FileInputStream("404.html");
					
					output.println("HTTP/1.1 404 Not Found");
					output.println("Server: Java HTTP Server from @shah06 : 1.0");
					output.println("Date: " + new Date());
					output.println();
					output.flush();
					
					// write the 404 file out
					int available = fis.available();
					byte[] bytes = new byte[available];
					fis.read(bytes);
					sos.write(bytes);
					
				}
				
			}
			
			// close
			sos.close();
			input.close();
			output.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
