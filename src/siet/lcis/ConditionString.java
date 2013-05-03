package siet.lcis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConditionString extends AtomicCondition {
	
	protected ArrayList<String> mAcceptableValues = new ArrayList<String>();
	
	public ConditionString(String id, String knowledgeID, String pAcceptableValue)
	{
		super(id, knowledgeID);
		mAcceptableValues.add(pAcceptableValue);
	}
	
	public void addAcceptableValue(String pValue)
	{
		mAcceptableValues.add(pValue);
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
			if (k instanceof KnowledgeString)
			{
				matchFound = match((KnowledgeString) k);
			}
			ki++;
		}
		return matchFound;
	}

	private long computeAccumulatedTime(KnowledgeString ks)
	{
		if (ks.isValid())
		{
			List<Transition<Boolean>> lMachingTransitions = filterStringTransitions(ks);
			return computeMatchingTime(lMachingTransitions, true);
		}
		
		return 0;
	}

	/**
	 * Produce a list of boolean transitions by filtering the 
	 * list of {@link Transition}<String> of a {@link KnowledgeString}.
	 * Each transition of the resulting list denotes a point in time where
	 * the value of the {@link KnowledgeString} changed its matching state
	 * against the condition.
	 * 
	 * @param ks Knowledge which {@link Transition} list will be checked.
	 * @return A list of boolean {@link Transition} representing the evolution of
	 * the matching state of the value against the condition.
	 */
	private List<Transition<Boolean>> filterStringTransitions(KnowledgeString ks)
	{
		List<Transition<Boolean>> result = new LinkedList<Transition<Boolean>>(); 
		Iterator<Transition<String>> lKnowledgeTransitionsIt = (Iterator<Transition<String>>) ks.mTransitionList.iterator();
		
		Transition<String> trans = lKnowledgeTransitionsIt.next();
		boolean lPrevMatch = basicMatch(trans.mValue);
		result.add(new Transition<Boolean>(lPrevMatch, trans.mDate));
		
		while (lKnowledgeTransitionsIt.hasNext())
		{
			trans = (Transition<String>) lKnowledgeTransitionsIt.next();
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

	private boolean basicMatch(String pValue) {
		for (String s : mAcceptableValues)
		{
			if (pValue.equals(s))
			{
				return true;
			}
		}
		return false;
	}

	private boolean basicMatch(KnowledgeString k) {
		return basicMatch(k.mValue);
	}

	public boolean match(KnowledgeString pKnowledge)
	{
		if (pKnowledge.mID.equals(mKnowledgeID) && pKnowledge.isValid())
		{
			if (mHistoryLength == 0)
			{
				return basicMatch(pKnowledge);
			}
			else if (pKnowledge.mHistoryLength >= mHistoryLength)
			{
				long accumulatedTime = computeAccumulatedTime((KnowledgeString) pKnowledge);
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
