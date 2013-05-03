package siet.lcis;

import java.util.Map;

public abstract class Condition {
	
	protected String mID = "";
	public Condition(String pId) {
		mID = pId;
	}

	public abstract boolean match(WorldModel pWM);
	
	public abstract Map<String, Long> getKnowledgesHistoryTimes();

}
