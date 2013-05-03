package siet.lcis;

import java.util.Map;

public class Rule {

	Condition mCondition;
	Action mAction;
	boolean mActive;
	
	public Rule(Condition condition, Action action)
	{
		mCondition = condition;
		mAction = action;
		mActive = false;
	}

	public boolean match(WorldModel pWorldModel) {

		boolean matchFound = mCondition.match(pWorldModel);
		if (matchFound)
		{
			mActive = true;
		}
		return matchFound;
	}

	public Map<String, Long> getKnowledgesHistoryLengths()
	{
		return mCondition.getKnowledgesHistoryTimes();
	}
}
