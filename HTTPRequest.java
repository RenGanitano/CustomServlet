package webLayer;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HTTPRequest {

	String url;
	String action;
	String version;
	static HashMap<String, String> headerlist = new HashMap<String, String>();
	static HashMap<String, String> content = new HashMap<String, String>();
	static InputStream ia;
	int sessionID;
	InputStream contents;
	String contentstring;
	HTTPRequest req;
	HTTPResponse res;
	WebListener wb;
	String a = "";
	boolean parseFailed = false;

	public HTTPRequest() {
	}

	public HTTPRequest(int id) {
		// contents = its;
		sessionID = id;

	}

	public void parseRequest(InputStream is) throws IOException {
		contents = is;
		try {

			String[] aa;

			String line;
			String[] ans;
			BufferedReader bf = new BufferedReader(new InputStreamReader(is));
			String header = bf.readLine();

			if (header != null) {
				System.out.println("header = " + header); 
				ans = header.split(" ");

				url = ans[1];
				action = ans[0];
				version = ans[2];
			}
			else
			{
				//System.out.println("Null header");
				parseFailed = true;
				return;
			}

			int length = -1;

			while ((line = bf.readLine()) != null) {
				System.out.println("Processing header line:" + line);
				line = line.trim();
				if (line.equalsIgnoreCase("")) {
					System.out.println("exit on empty line");
					break;
				}
				aa = line.split(": ");
				headerlist.put(aa[0], aa[1]);
				System.out.println(aa[0] + "=" + aa[1]);

			}
			System.out.println("End of headers");

			String leng = headerlist.get("Content-Length");
			System.out.println("AAA" + leng);

			if (action.equalsIgnoreCase("POST")) {
				try {
					length = Integer.parseInt(leng);
				} catch (Exception e) {

				}

				String[] bb;
				
				String data = "";
				for (int i = 0; i < length; i++) {
					int a = bf.read();
					char ch = (char)a;
					data += ch;
				}

				System.out.println("reading content");
				System.out.flush();

				String[] contents = data.split("&");
				for (int i = 0; i < contents.length; i++) {

					bb = contents[i].split("=");
					content.put(bb[0], bb[1]);
					System.out.println(bb[0] + " " + bb[1]);

				}
			}

			res = new HTTPResponse(url, action, headerlist);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int getSessionID() {
		return sessionID;
	}

}
