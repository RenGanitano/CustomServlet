package webLayer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class WebListener extends Socket {
	boolean DEBUG = true;

	ServerSocket scs;
	Socket conn;
	String content;
	int sessionID = SessionInfo.lastSessionId;
	String ipAddress;
	long time;
	static final String EOL = "\r\n";

	HTTPResponse res;
	HTTPRequest req;

	public WebListener() {

	}

	public static boolean shutdown = false;

	SessionTable sessionTable = new SessionTable();
	SessionInfo sessionInfo = new SessionInfo();

	private HashMap<String, Servlet> urlTable = new HashMap<String, Servlet>();

	public Servlet getServlet(String url) {
		Servlet s = urlTable.get(url);
		return s;
	}

	public void setUrlValue(String url, Servlet servlet) {
		urlTable.put(url, servlet);
	}

	public void listenSocket() throws IOException {
		// Create socket connection
		scs = new ServerSocket(8080);
		if (DEBUG) {
			if (scs == null)
				System.out.println("Failed to create server socket");
			else
				System.out.println("Listening on port 8080");
		}

		while (shutdown == false && (conn = scs.accept()) != null) {

			try {
				if (DEBUG) {
					System.out.println("got connection ...");
				}
				// checks if session is in table if not then creates new session
				sessionTable.get(conn.getInetAddress().toString(),
						System.currentTimeMillis());
				if (DEBUG) {
					System.out.println("connected from "
							+ conn.getInetAddress().toString());
					if (getServlet("C:/Users/Milan/Desktop/hi.html") == null) {
						System.out.println("no Servlet");
					}
				}

				if (sessionInfo.expired(System.currentTimeMillis())) {
					// checks if session is expired and removes expired session
					// ips
					sessionTable.sessMap.remove(conn.getInetAddress());
					System.out.println(sessionTable.sessMap
							.get("0:0:0:0:0:0:0:1").sessionID);
				} else {
					// updates time of requests
					sessionInfo.updateTime(System.currentTimeMillis());
				}

				// ERROR CLASS NOT FOUND

				InputStream in = conn.getInputStream();

				req = new HTTPRequest(sessionID);

				req.parseRequest(in);
				if(req.parseFailed)
				{
					conn.close();
					continue;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			Servlet servlet = getServlet(req.url);

			if (servlet == null) {
				getServlet("error url provided by presentation");
				if (DEBUG)
					System.out.println("no servlet found for " + req.url);
			} else {
				try {
					if (req.res.action.contains("GET")) {
						servlet.doGet(req, req.res);
						System.out.println("doal");

					} else if (req.res.action.contains("POST")) {
						servlet.doPost(req, req.res);
					}
				} catch (Exception e) {
					// get error 500 servlet
					if (DEBUG)
						System.out.println(e.getMessage());
				}
			}
			System.out.println("aaa" + req.res.getResponse());
			OutputStream out = conn.getOutputStream();
			if (DEBUG)
				System.out.println("sending response ...");
			out.write((req.res.responseLine + EOL).getBytes());
			Set<String> headKeys = req.res.headerlist.keySet();
			Iterator<String> iter = headKeys.iterator();
			while (iter.hasNext()) {
				String val = iter.next();
				out.write((val + req.res.headerlist.get(val) + EOL).getBytes());
				if (DEBUG)
					System.out.println(val + req.res.headerlist.get(val) + EOL);
			}

			byte[] resp = req.res.getResponse().getBytes();
			if (DEBUG)
				System.out.println(req.res.getResponse() + EOL);
			int contLen = resp.length;
			out.write(("Content-Length: " + contLen + EOL + EOL).getBytes());

			out.write(resp);
			out.flush();
			conn.close();
		}

		scs.close();
	}

	public static void main(String args[]) {
		WebListener wl = new WebListener();
		wl.setUrlValue("/enlighten/calais.asmx", new ServletClass());
		wl.setUrlValue("/hi.html", new ServletClass());
		wl.setUrlValue("/login_servlet", new Login_Servlet());
		wl.setUrlValue("/", new ServletClass());
		wl.setUrlValue("/pages/ExamRoom.html", new ExamSlot());
		wl.setUrlValue("/Teacher", new TeacherView());

		try {
			wl.listenSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
