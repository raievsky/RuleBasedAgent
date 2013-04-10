package siet.lcis;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
					matchFound = basicMatch((KnowledgeInt) k);
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

	private long computeAccumulatedTime(KnowledgeInt k)
	{
		if (k.isValid())
		{
			List<Transition<Boolean>> lMachingTransitions = filterIntTransitions(k);
			return computeMatchingTime(lMachingTransitions, true);
		}
		
		return 0;
	}

	private List<Transition<Boolean>> filterIntTransitions(KnowledgeInt k)
	{
		List<Transition<Boolean>> result = new LinkedList<Transition<Boolean>>(); 
		Iterator<Transition<Integer>> lKnowledgeTransitionsIt = (Iterator<Transition<Integer>>) k.mTransitionList.iterator();
		Transition<Integer> trans = lKnowledgeTransitionsIt.next();
		boolean lPrevMatch = basicMatch(trans.mValue);
		result.add(new Transition<Boolean>(lPrevMatch, trans.mDate));
		
		while (lKnowledgeTransitionsIt.hasNext())
		{
			trans = (Transition<Integer>) lKnowledgeTransitionsIt.next();
			boolean currentMatch = basicMatch(trans.mValue);
			if (lPrevMatch != currentMatch)
			{
				result.add(new Transition<Boolean>(currentMatch, trans.mDate));
				lPrevMatch = currentMatch;
			}
		}
		
		return result;
	}

	private boolean basicMatch(Integer pValue) {
		return pValue > mMin && pValue <= mMax;
	}

	private boolean basicMatch(KnowledgeInt k) {
		return k.mValue > mMin && k.mValue <= mMax;
	}
	

}
