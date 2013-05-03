package siet.lcis;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AtomicCondition extends Condition {
	
	protected String mKnowledgeID = "";
	/**
	 * Length of the monitored time lapse.
	 */
	protected long mHistoryLength = 0;
	/**
	 * The condition will be verified if the accumulated
	 * time during which the value of the monitored {@link Knowledge}
	 * is greater than this threshold.
	 */
	protected long mAccumulatedTimeThreshold = 0;

	public AtomicCondition(String pID, String pKnowledgeID)
	{
		super(pID);
		mKnowledgeID = pKnowledgeID;
	}
	
	/**
	 * 
	 * @param pHLength Length of the monitored time lapse.
	 */
	public void setHistoryLength(int pHLength)
	{
		mHistoryLength = pHLength;
	}
	
	public void setAccumulatedTimeThreshold(int pATT)
	{
		mAccumulatedTimeThreshold = pATT;
	}
	
	/**
	 * Compute the accumulated duration during which the state
	 * of the transition in pTransList matches pValue.
	 * Please ensure JUnit "AllTests" test pass if you modify this method.
	 * 
	 * @param pTransList a list of boolean {@link Transition}s
	 * @param pValue the value to be matched in the {@link Transition}s
	 * @return the accumulated time in seconds during which the value represented in
	 * pTransList matches pValue
	 */
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

	@Override
	public Map<String, Long> getKnowledgesHistoryTimes() {
		Map<String, Long> result = new HashMap<String, Long>();
		result.put(mKnowledgeID, mHistoryLength);
		return result;
	}
}
