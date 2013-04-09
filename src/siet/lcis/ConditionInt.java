package siet.lcis;

import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import sun.nio.cs.HistoricallyNamedCharset;

public class ConditionInt extends Condition {
	
	protected int mMin;
	protected int mMax;
	protected long mHistoryLength = 0;
	protected long mAccumulatedTimeThreshold = 0;
	
	public ConditionInt(String id, String knowledgeID, int min, int max)
	{
		super(id, knowledgeID);
		mMin = min;
		mMax = max;
	}
	
	/**
	 * 
	 * @param pHLength Length of the history of kept values 
	 */
	public void setHistoryLength(int pHLength)
	{
		mHistoryLength = pHLength;
	}
	
	public void setAccumulatedTimeThreshold(int pATT)
	{
		mAccumulatedTimeThreshold = pATT;
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
				if (k.mHistoryLength == 0)
				{
					matchFound = ((KnowledgeInt) k).mID == mKnowledgeID &&
							((KnowledgeInt) k).mValue > mMin &&
							((KnowledgeInt) k).mValue <= mMax;
				}
				else
				{
					long accumulatedTime = computeAccumulatedTime((KnowledgeInt) k);
					matchFound = accumulatedTime >= mAccumulatedTimeThreshold;

				}
			}
			ki++;
		}
		return matchFound;
	}

	private long computeAccumulatedTime(KnowledgeInt k) {
		
		System.err.println("ConditionInt computeAccumulatedTime not yet implemened.");
		return 0;
		
		/*
		if (!k.mTransitionList.isEmpty()) 
		{
			Iterator<Transition<Integer>> it = k.mTransitionList.iterator();
			Transition<Integer> trans = it.next();
			Date now = new Date();
			
			// Skip all transitions that occurred before the beginning of the monitored time lapse
			while (((now.getTime() - trans.mDate.getTime()) * 1000) > mHistoryLength && it.hasNext())
			{
				trans = (Transition<java.lang.Integer>) it.next();
			}
			
			if ((now.getTime() - trans.mDate.getTime()) * 1000 > mHistoryLength )
			{
				// No transition in the monitored time lapse
				if (basicMatch(k))
				{
					return mHistoryLength;
				}
			}
			else
			{
				// At least one transition in the monitored time lapse
//				if (!trans.mValue) {
//					
//				}

			}
			
			if (it.hasNext())
			{
				// there is at least two transitions in the list.
			}
			
		}
		else
		{
			// No transition in the knowledge's transition list,
			// return the duration of the monitored time lapse
			// if the knowledge matches the condition.
			if (basicMatch(k))
			{
				return mHistoryLength;
			}
		}
		return 0;
		*/
	}

	private boolean basicMatch(KnowledgeInt k) {
		return k.mValue > mMin && k.mValue <= mMax;
	}
	

}
