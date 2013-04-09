package siet.lcis;

import java.util.concurrent.ConcurrentLinkedQueue;


public class KnowledgeInt extends Knowledge
{
	protected Integer mValue;
	public ConcurrentLinkedQueue<Transition<Integer>> mTransitionList = new ConcurrentLinkedQueue<Transition<Integer>>();

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
