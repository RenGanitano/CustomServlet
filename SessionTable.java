package webLayer;

import java.util.HashMap;

public class SessionTable {

	HashMap<String, SessionInfo> sessMap = new HashMap<String, SessionInfo>();

	public SessionTable() {
	}

	public SessionInfo get(String IPadder, long tm) {
		SessionInfo inf = sessMap.get(IPadder);
		if (inf == null) {
			inf = new SessionInfo(tm);
			sessMap.put(IPadder, inf);
		}
		return inf;
	}

}
