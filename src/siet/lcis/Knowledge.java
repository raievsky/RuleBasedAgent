package siet.lcis;

public abstract class Knowledge {
	protected String mID;
	protected long mHistoryLength = 0;
	protected boolean mIsValid = false;
//	T mValue;
	
	public Knowledge(String pID)//, T pValue)
	{
		this.mID = pID;
//		this.mValue = pValue;
	}
	
	// Note, this function should only be used for testing purpose.
	// RuleBase and WorldModel classes are responsible for modifying
	// history lengths.
	public void setHistoryLength(long pLength)
	{
		mHistoryLength = pLength;
	}
	
	public boolean isValid()
	{
		return mIsValid;
	}
	
	abstract void clearHistory();
	
//	@Override
//	public String toString() {
//		return "Knowledge [mID=" + mID + ", mValue=" + mValue
//				+ "]";
//	}
}
