package siet.lcis;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class KnowledgeString extends Knowledge {
	
	protected String mValue = "";
	public ConcurrentLinkedQueue<Transition<String>> mTransitionList = new ConcurrentLinkedQueue<Transition<String>>();
	
	public KnowledgeString(String pID)
	{
		super(pID);
	}
	
	public KnowledgeString(String pID, String pValue)
	{
		super(pID);
		appendValue(pValue);
	}

	public void appendValue(String pValue)
	{
		// TODO cleanup transition list from obsolete transitions
		
		// No need to add a transition when state does not change
		if (!(mIsValid && pValue.equals(mValue)))
		{
			Date now = new Date();
			mTransitionList.offer(new Transition<String>(pValue, now));
			mValue = pValue;
		}
		
		mIsValid = true;
	}
	
	public void clearHistory()
	{
		mTransitionList.clear();
	}
}
