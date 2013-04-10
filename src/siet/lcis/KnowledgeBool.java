package siet.lcis;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class KnowledgeBool extends Knowledge {

	protected boolean mValue;
	public ConcurrentLinkedQueue<Transition<Boolean>> mTransitionList = new ConcurrentLinkedQueue<Transition<Boolean>>();

	public KnowledgeBool(String pID) {
		super(pID);
	}
	
	public KnowledgeBool(String pID, boolean pValue) {
		super(pID);
		appendValue(pValue);
	}

	@Override
	public String toString() {
		return "Knowledge [mID=" + mID + ", mValue=" + mValue
				+ "]";
	}

	public boolean currentValue() {
		assert(mIsValid);
		if (!mIsValid) {
			System.err.println("Error: trying to get the value of an invalid knowledge.");
		}
		return mValue;
	}
	
	public void appendValue(boolean pValue)
	{
		// TODO cleanup transition list from obsolete transitions
		
		// No need to add a transition when state does not change
		if (!(pValue == mValue && mIsValid))
		{
			Date now = new Date();
			mTransitionList.offer(new Transition<Boolean>(pValue, now));
			mValue = pValue;
		}
		
		mIsValid = true;
	}
	
	public void clearHistory()
	{
		mTransitionList.clear();
		mIsValid = false;
	}
	
}
