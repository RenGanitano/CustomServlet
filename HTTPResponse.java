package webLayer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class HTTPResponse {

	HashMap<String, String> headerlist = new HashMap();
	int sessionID;
	String content = "";
	String url;
	String action;
	int conlength;
	String responseLine = null;

	public HTTPResponse() {
	}
	
	public HTTPResponse(String urls, String actions, HashMap headerList) {
		url = urls;
		action = actions;
		
		responseLine = "HTTP/1.1 200 OK";
		

		headerList.put("Date: ", getServerTime());
		headerList.put("Server: ", "HumberServer");
		headerList.put("Connection: ", "close");
		    			
	}
	
	public  String getServerTime() {
	    Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    return dateFormat.format(calendar.getTime());
	}
	
		
	public void write(String html) {
		content += html;		
		
	}
	
	public String getResponse() {
		return content;
	}
	
	public HashMap getHeaderlist(){
		return headerlist;
	}

	public String getAction(){
		return action;
	}
	public String getUrl(){
		return url;
	}
	public int getID(){
		return sessionID;
	}
}
