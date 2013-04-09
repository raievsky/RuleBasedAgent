package siet.lcis;

import java.util.Iterator;

public abstract class Condition {
	
	protected String mID = "";
	protected String mKnowledgeID = "";

	public Condition(String pID, String pKnowledgeID)
	{
		mID = pID;
		mKnowledgeID = pKnowledgeID;
	}
	
	public abstract boolean match(WorldModel pWM);
}
