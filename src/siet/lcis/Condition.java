package siet.lcis;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public abstract class Condition {
	
	protected String mID = "";
	protected String mKnowledgeID = "";
	protected long mHistoryLength = 0;
	protected long mAccumulatedTimeThreshold = 0;

	public Condition(String pID, String pKnowledgeID)
	{
		mID = pID;
		mKnowledgeID = pKnowledgeID;
	}
	
	public abstract boolean match(WorldModel pWM);
	
	protected long computeMatchingTime(Iterable<Transition<Boolean>> pTransList, boolean pValue)
	{
		Iterator<Transition<Boolean>> it = pTransList.iterator();
		if (it.hasNext())
		{
			final long lHistoryLengthMilli = mHistoryLength * 1000;
			long lAccumulatedTime = 0;
			Transition<Boolean> trans = it.next();
			Date now = new Date();
			boolean lTransitionsSkipped = false;
			boolean lMatchingAtBeginningOfHistory = trans.mValue == pValue;

			// Skip all transitions that occurred before the beginning of the monitored time lapse
			while (it.hasNext() && 
					(now.getTime() - trans.mDate.getTime()) > lHistoryLengthMilli)
			{
				trans = (Transition<Boolean>) it.next();
				lTransitionsSkipped = true;
				lMatchingAtBeginningOfHistory = (trans.mValue == pValue);
			}

			if ((now.getTime() - trans.mDate.getTime()) < lHistoryLengthMilli)
			{
				if (trans.mValue != pValue && lTransitionsSkipped)
				{
					// The first transition in the monitored time lapse is toward
					// the non-matching state and the knowledge was valid at the 
					// beginning of history, initialize accumulated time accordingly.
					lAccumulatedTime = trans.mDate.getTime() - (now.getTime() - lHistoryLengthMilli);
				}
			}
			else
			{
				// No transition in the monitored time lapse
				if (lMatchingAtBeginningOfHistory)
				{
					return mHistoryLength;
				}
				else
				{
					return 0;
				}
			}

			// Process remaining transitions
			while (it.hasNext())
			{
				Transition<Boolean> next = (Transition<Boolean>) it.next();
				if (trans.mValue == pValue)
				{
					lAccumulatedTime += next.mDate.getTime() - trans.mDate.getTime();
				}
				trans = next;
			}

			// trans is the last transition
			if (trans.mValue == pValue)
			{
				lAccumulatedTime += now.getTime() - trans.mDate.getTime();
			}

			return lAccumulatedTime / 1000;
		}
		else
		{
			System.err.println("Error: trying to compute accumulated matching time on an invalid transition list.");
		}
		return 0;
	}
	
}
