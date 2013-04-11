package siet.lcis;

public class ConditionBool extends Condition {

	protected boolean mValue;
	
	public ConditionBool(String id, String knowledgeID, boolean pValue)
	{
		super(id, knowledgeID);
		mValue = pValue;
	}
	
	@Override
	public boolean match(WorldModel pWM) {
		boolean matchFound = false;

		int ki = 0;
		Knowledge k;
		while (!matchFound && ki < pWM.size())
		{
			k = pWM.get(ki);
			if (k instanceof KnowledgeBool)
			{
				if (((KnowledgeBool) k).mID == mKnowledgeID )
				{
					matchFound = match((KnowledgeBool) k);
				}
			}
			ki++;
		}
		return matchFound;
	}

	public boolean match(KnowledgeBool pKBool)
	{
		if (pKBool.isValid())
		{
			if (mHistoryLength == 0)
			{
				return basicMatch(pKBool);
			}
			else
			{
				long accumulatedTime = computeAccumulatedTime(pKBool);
				return accumulatedTime >= mAccumulatedTimeThreshold;
			}
		}
		else
		{
			return false;
		}
	}

	private long computeAccumulatedTime(KnowledgeBool k)
	{
		return computeMatchingTime(k.mTransitionList, mValue);
	}

	private boolean basicMatch(KnowledgeBool k)
	{
		return k.isValid() && k.currentValue() == mValue;
	}
}
