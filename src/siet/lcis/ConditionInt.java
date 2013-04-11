package siet.lcis;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
				matchFound = match((KnowledgeInt) k);
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

	/**
	 * Produce a list of boolean transitions by filtering the 
	 * list of {@link Transition}<Ìnteger> of a {@link KnowledgeInt}.
	 * Each transition of the resulting list denotes a point in time where
	 * the value of the {@link KnowledgeInt} changed its matching state
	 * against the condition.
	 * 
	 * @param k Knowledge which {@link Transition} list will be checked.
	 * @return A list of boolean {@link Transition} representing the evolution of
	 * the matching state of the value against the condition.
	 */
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
				// Current transition value matching state is different from
				// previous value matching state, add a boolean transition to
				// the resulting list.
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

	public boolean match(KnowledgeInt pKnowledge)
	{
		if (pKnowledge.mID.equals(mKnowledgeID) && pKnowledge.isValid())
		{
			if (mHistoryLength == 0)
			{
				return basicMatch(pKnowledge);
			}
			else if (pKnowledge.mHistoryLength >= mHistoryLength)
			{
				long accumulatedTime = computeAccumulatedTime((KnowledgeInt) pKnowledge);
				return accumulatedTime >= mAccumulatedTimeThreshold;
			}
			else
			{
				System.err.println("Internal Error: knowledge's history length shorter than condition's. This must not happen.");
			}
		}
		
		return false;
	}
}
