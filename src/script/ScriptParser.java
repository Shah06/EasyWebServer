package script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * A very simple server side scripting language
 * @author atharva
 *
 */
public class ScriptParser {
	
	private HashMap<String, String> params = new HashMap<String, String>();
	private BufferedReader reader;
	private PrintWriter writer;
	private String fileName;
	private File buildFile;
	
	public ScriptParser(String fileName) {
		this(fileName, null);
	}
	
	public ScriptParser(String fileName, HashMap<String, String> params) {
		try {
			this.fileName = fileName;
			reader = new BufferedReader(new FileReader(fileName));
			buildFile = new File(fileName + ".ewso");
			writer = new PrintWriter(new FileOutputStream(buildFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			this.params = params;
		}
	}
	
	public static boolean isScript(String fileName) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			if (br.readLine().equals("<?EWS>")) {
				br.close();
				return true;
			} else {
				br.close();
				return false;
			}
		} catch (IOException e) {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e1) {
					e.printStackTrace();
				}
			}
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @return a string to the file generated
	 * @throws IOException
	 */
	public String buildFile() throws IOException {
		// check if file contains <?EWS> on first line
		if (reader.readLine().equals("<?EWS>")) {
			
			// process the file
			String s;
			while ((s = reader.readLine()) != null) {
				// <?start>param1<?end>
				if (s.contains("<?start>")) {
					// replace the name parameter with its value
					String param = s.substring(s.indexOf("<?start>")+8, s.indexOf("<?end>"));
					if (params.containsKey(param)) {
						String value = params.get(param).replaceAll("%20", " "); // removes spaces
						writer.println(s.substring(0, s.indexOf("<?start")) + value + s.substring(s.indexOf("<?end>")+6));
					} else {
						writer.println(s.substring(0, s.indexOf("<?start")) + "NULL" + s.substring(s.indexOf("<?end>")+6));
					}
					writer.flush();
				} else {
					writer.println(s);
					writer.flush();
				}
			}
			
			return buildFile.getAbsolutePath();
			
		} else {
			// not EWS script compliant
			return this.fileName; 
		}
	}
	
}
