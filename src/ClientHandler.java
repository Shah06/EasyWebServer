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
				
				// print everything out
				String s = input.readLine();
				while (!s.equals("")) {
					System.out.println(s);
					s = input.readLine();
				}
				
				System.out.println("GOT HERE");
				output.println("HTTP/1.1 200 OK");
				output.println("Server: Java HTTP Server from @shah06 : 1.0");
				output.println("Date: " + new Date());
				output.println();
				output.flush();
				
				// write the file out
				System.out.println("Estimated filesize: " + fis.available() + " bytes");
				int available = fis.available();
				byte[] bytes = new byte[available];
				fis.read(bytes);
				sos.write(bytes);
				
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
