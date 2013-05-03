package siet.lcis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RuleBase extends HashSet<Rule> {
	
	protected HashMap<String, Long> mMaxKnowledgeHistoryLengths = new HashMap<String, Long>();
	protected WorldModel mWorldModel;
	
	public void setWorldModel(WorldModel pWM)
	{
		mWorldModel = pWM;
	}
	
	public Map<String, Long> getMaxKnowledgeLengths()
	{
		return mMaxKnowledgeHistoryLengths;
	}
	
	@Override
	public boolean add(Rule pRuleToAdd)
	{
		boolean oneMaxChanged = false;
		Map<String, Long> lRuleHistoryLengths = pRuleToAdd.getKnowledgesHistoryLengths();
		// Add rule's knowledges to our list of monitored knowledges
		for (String knowledgeIdIt : lRuleHistoryLengths.keySet())
		{

			if (mMaxKnowledgeHistoryLengths.containsKey(knowledgeIdIt))
			{
				// knowledge already referenced in the base, update max history length
				if (lRuleHistoryLengths.get(knowledgeIdIt) > mMaxKnowledgeHistoryLengths.get(knowledgeIdIt))
				{
					mMaxKnowledgeHistoryLengths.put(knowledgeIdIt, lRuleHistoryLengths.get(knowledgeIdIt));
					oneMaxChanged = true;
				}
			}
			else
			{
				// Knowledge not yet referenced in the base, insert in our max history lengths map.
				mMaxKnowledgeHistoryLengths.put(knowledgeIdIt, lRuleHistoryLengths.get(knowledgeIdIt));
				oneMaxChanged = true;
			}
		}
		
		if (oneMaxChanged)
		{
			mWorldModel.updateKnowledgesHistLengths(mMaxKnowledgeHistoryLengths);
		}
		
		return super.add(pRuleToAdd);
	}
	
	// TODO implement the remove method.
}
