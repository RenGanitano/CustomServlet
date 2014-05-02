package webLayer;

public class SessionInfo {

	static int lastSessionId = 0;

	static int sessionID;
	long time;

	public SessionInfo() {
		
	}
	public SessionInfo(long tm) {
		sessionID = lastSessionId++;
		time = tm;
	}

	public boolean expired(long tm) {
		System.out.println(tm + " : " + time);
		return ((tm - time) / 60000) >= 20.00;
	}

	public void updateTime(long tm) {
		time = tm;
	}
	
	public long getTime(){
		return time;
	}
}
