package siet.lcis;

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
}
