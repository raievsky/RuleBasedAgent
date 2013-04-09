package siet.lcis;

public abstract class Knowledge {
	protected String mID;
	protected int mHistoryLength = 0;
	protected boolean mIsValid = false;
//	T mValue;
	
	public Knowledge(String pID)//, T pValue)
	{
		this.mID = pID;
//		this.mValue = pValue;
	}
	
	public void setHistoryLength(int pLength)
	{
		mHistoryLength = pLength;
	}
	
//	@Override
//	public String toString() {
//		return "Knowledge [mID=" + mID + ", mValue=" + mValue
//				+ "]";
//	}
}
