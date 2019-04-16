package server;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

import script.ScriptParser;

public class ClientHandler implements Runnable {

	private Socket socket;
	private FileInputStream fis;
	
	private HashMap<String, String> parameters = new HashMap<String, String>();
	
	public ClientHandler (Socket socket, String filepath) {
		this.socket = socket;
		try {
			// check if file is EWS compliant
			fis = new FileInputStream(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, String> getParameters() {
		return parameters;
	}
	
	public void run() {
		
		BufferedReader input = null;
		PrintWriter output = null;
		OutputStream sos = null;
		try {
			
			// exception would be thrown here, if anywhere
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream());
			sos = socket.getOutputStream();
			
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
				System.out.println(httpHeader);
				String[] headerLines = httpHeader.split("\\r?\\n");
				// headerLines[0] contains the GET request
				String getRequest = headerLines[0].substring(5, headerLines[0].lastIndexOf(" "));
				// get request might have parameters, parse them out
				if (getRequest.contains("?")) {
					int qIndex = getRequest.indexOf("?");
					String[] unparsedParams = getRequest.substring(qIndex+1).split("&");
					for (String p : unparsedParams) {
						String[] parts = p.split("=");
						parameters.put(parts[0], parts[1]);
					}
					getRequest = getRequest.substring(0, qIndex);
				}
				
				try {
					
					// check if the file is ewso
					if (ScriptParser.isScript(getRequest)) {
						System.out.println("true");
						fis = new FileInputStream(new ScriptParser(getRequest, parameters).buildFile());
					} else {
						fis = new FileInputStream(getRequest);
					}
					
					// http header for OK
					output.println("HTTP/1.1 200 OK");
					output.println("Server: Java HTTP Server from @shah06 : 1.0");
					output.println("Date: " + new Date());
					output.println();
					output.flush();
					
				} catch (FileNotFoundException fnfe) {
					
					fis = new FileInputStream("404.html");
					
					// http header for 404
					output.println("HTTP/1.1 404 Not Found");
					output.println("Server: Java HTTP Server from @shah06 : 1.0");
					output.println("Date: " + new Date());
					output.println();
					output.flush();
					
				} finally {
					
					// write the file out
					int available = fis.available();
					byte[] bytes = new byte[available];
					fis.read(bytes);
					sos.write(bytes);
					
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			// close everything
			try {
				if (null != sos) {
					sos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				if (null != input) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (null != output) {
				output.close();
			}
			
			
			try {
				if (null != socket) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
}
