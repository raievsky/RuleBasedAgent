package siet.lcis;

import java.util.Date;
import java.util.Iterator;

public class ConditionBool extends Condition {

	protected boolean mValue;
	protected long mHistoryLength = 0;
	protected long mAccumulatedTimeThreshold = 0;
	
	public ConditionBool(String id, String knowledgeID, boolean pValue)
	{
		super(id, knowledgeID);
		mValue = pValue;
	}
	
	/**
	 * 
	 * @param pHLength Length of the history of kept values 
	 */
	public void setHistoryLength(long pHLength)
	{
		mHistoryLength = pHLength;
	}
	
	public void setAccumulatedTimeThreshold(long pATT)
	{
		mAccumulatedTimeThreshold = pATT;
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


	private long computeAccumulatedTime(KnowledgeBool k) {
		
//		System.err.println("ConditionBool computeAccumulatedTime not yet implemened.");
//		return 0;
		final long lHistoryLengthMilli = mHistoryLength * 1000;
		
		if (!k.mTransitionList.isEmpty())
		{
			long lAccumulatedTime = 0;
			Iterator<Transition<Boolean>> it = k.mTransitionList.iterator();
			Transition<Boolean> trans = it.next();
			Date now = new Date();
			boolean lTransitionsSkipped = false;
			boolean lMatchingAtBeginningOfHistory = trans.mValue == mValue;

			// Skip all transitions that occurred before the beginning of the monitored time lapse
			while (it.hasNext() && 
					(now.getTime() - trans.mDate.getTime()) > lHistoryLengthMilli)
			{
				trans = (Transition<Boolean>) it.next();
				lTransitionsSkipped = true;
				lMatchingAtBeginningOfHistory = (trans.mValue == mValue);
			}

			if ((now.getTime() - trans.mDate.getTime()) < lHistoryLengthMilli)
			{
				if (trans.mValue != mValue && lTransitionsSkipped)
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
				if (trans.mValue == mValue)
				{
					lAccumulatedTime += next.mDate.getTime() - trans.mDate.getTime();
				}
				trans = next;
			}
			
			
			// trans is the last transition
			if (trans.mValue == mValue)
			{
				lAccumulatedTime += now.getTime() - trans.mDate.getTime();
			}
			
			return lAccumulatedTime / 1000;
//			
//			
//			
//			
//			if ((now.getTime() - trans.mDate.getTime()) > lHistoryLengthMilli )
//			{
//				// No transition in the monitored time lapse
//				if (basicMatch(k))
//				{
//					return mHistoryLength;
//				}
//			}
//			else
//			{
//				// At least one transition in the monitored time lapse
//				// and knowledge was valid before this transition.
//				if (trans.mValue != mValue && lTransitionsSkipped)
//				{
//					// The first transition in the monitored time lapse is toward
//					// the non-matching state initialize accumulated time accordingly.
//					lAccumulatedTime = trans.mDate.getTime() - (now.getTime() - lHistoryLengthMilli);
//				}
//				
//				while (it.hasNext())
//				{
//					Transition<Boolean> lNextTrans = (Transition<Boolean>) it.next();
//					if (it.hasNext())
//					{
//						// Not on the last transition, increment accumulated time.
//						lAccumulatedTime += lNextTrans.mDate.getTime() - trans.mDate.getTime();
//					}
//					trans = lNextTrans;
//				}
//				
//				// trans is the last transition
//				if (trans.mValue == mValue)
//				{
//				}
//				return lAccumulatedTime / 1000;
//			}
		}
		else
		{
			System.err.println("Error: trying to compute accumulated time on an invalid knowledge.");
		}
		return 0;
	}

	private boolean basicMatch(KnowledgeBool k) {
		
		return k.isValid() && k.currentValue() == mValue;
	}

	public static void main(String[] args) throws InterruptedException {

		KnowledgeBool kb = new KnowledgeBool("test", false);
		
		ConditionBool cb = new ConditionBool("rule1", "test", true);
		cb.setHistoryLength(0);
		cb.setAccumulatedTimeThreshold(4);
		
		kb.appendValue(false);
		Thread.sleep(2000);
		kb.appendValue(true);
		Thread.sleep(2000);
		
		System.out.println("2 over 4 seconds of satisfaction");
		
		if (cb.match(kb))
		{
			System.out.println("condition satisfied.");
		}
		else
		{
			System.out.println("condition not satisfied.");
		}
		
		
		kb.appendValue(false);
		Thread.sleep(1000);
		kb.appendValue(true);
		Thread.sleep(3000);
		kb.appendValue(false);
		Thread.sleep(1000);
		System.out.println("5 over 4 seconds of satisfaction");
		
		if (cb.match(kb))
		{
			System.out.println("condition satisfied.");
		}
		else
		{
			System.out.println("condition not satisfied.");
		}
		
		
		
	}

}
