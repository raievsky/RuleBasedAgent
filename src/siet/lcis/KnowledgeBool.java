package siet.lcis;

public class KnowledgeBool extends Knowledge {

	protected boolean mValue;

	public KnowledgeBool(String pID, boolean pValue) {
		super(pID);
		this.mValue = pValue;
	}

	@Override
	public String toString() {
		return "Knowledge [mID=" + mID + ", mValue=" + mValue
				+ "]";
	}
}
