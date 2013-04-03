package siet.lcis;

import java.util.Iterator;

public class ConditionInt extends Condition {
	
	protected int mMin;
	protected int mMax;
	
	public ConditionInt(String id, String knowledgeID, int min, int max)
	{
		super(id, knowledgeID);
		mMin = min;
		mMax = max;
	}

	@Override
	public boolean match(WorldModel pWM)
	{
		boolean matchFound = false;
		
		int ki = 0;
		Knowledge k;
		while (!matchFound && ki < pWM.size())
		{
			k = pWM.get(ki);
			if (k instanceof KnowledgeInt)
			{
				matchFound = ((KnowledgeInt) k).mID == mKnowledgeID &&
						((KnowledgeInt) k).mValue > mMin &&
						((KnowledgeInt) k).mValue < mMax;
			}
			ki++;
		}
		return matchFound;
	}
	

}
