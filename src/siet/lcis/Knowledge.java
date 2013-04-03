package siet.lcis;

public class Knowledge {
	String mAttribute;
	String mValue;
	
	/**
	 * @param pAttribute
	 * @param pValue
	 */
	public Knowledge(String pAttribute, String pValue) {
		this.mAttribute = pAttribute;
		this.mValue = pValue;
	}

	@Override
	public String toString() {
		return "Knowledge [mAttribute=" + mAttribute + ", mValue=" + mValue
				+ "]";
	}
}
