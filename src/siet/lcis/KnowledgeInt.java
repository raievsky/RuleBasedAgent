package siet.lcis;

public class KnowledgeInt extends Knowledge
{
	protected int mValue;

	public KnowledgeInt(String pID, int pValue) {
		super(pID);
		this.mValue = pValue;
	}

	@Override
	public String toString() {
		return "Knowledge [mID=" + mID + ", mValue=" + mValue
				+ "]";
	}
}
