package siet.lcis;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;


public class KnowledgeInt extends Knowledge
{
	protected Integer mValue;
	public ConcurrentLinkedQueue<Transition<Integer>> mTransitionList = new ConcurrentLinkedQueue<Transition<Integer>>();

	public KnowledgeInt(String pID, int pValue) {
		super(pID);
		this.mValue = pValue;
	}

	public void appendValue(int pValue)
	{
		// TODO cleanup transition list from obsolete transitions
		
		// No need to add a transition when state does not change
		if (!(pValue == mValue && mIsValid))
		{
			Date now = new Date();
			mTransitionList.offer(new Transition<Integer>(pValue, now));
			mValue = pValue;
		}
		
		mIsValid = true;
	}
	@Override
	public String toString() {
		return "Knowledge [mID=" + mID + ", mValue=" + mValue
				+ "]";
	}
}
