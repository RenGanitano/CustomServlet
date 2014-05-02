package webLayer;

public interface Servlet {

	public void doGet(HTTPRequest req, HTTPResponse res);

	public void doPost(HTTPRequest req, HTTPResponse res);

}
