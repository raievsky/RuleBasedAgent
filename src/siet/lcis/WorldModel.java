package siet.lcis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WorldModel extends ArrayList<Knowledge> {

	protected RuleBase mRuleBase;
	
	public void setRuleBase(RuleBase pRuleBase)
	{
		mRuleBase = pRuleBase;
	}
	
	public void push(Knowledge knowledge)
	{
		boolean existing = false;
		for (Iterator<Knowledge> kIt = this.iterator(); kIt.hasNext();) {
			Knowledge k = kIt.next();
			if (k.mID == knowledge.mID)
			{
				if (k.getClass() == knowledge.getClass())
				{
					if (k instanceof KnowledgeInt)
					{
						((KnowledgeInt) k).appendValue(((KnowledgeInt) knowledge).mValue);
					}
					else if (k instanceof KnowledgeString)
					{
						System.err.println("Not yet implemented: WorldModel push of KnowledgeString");
//						((KnowledgeString) k).mValue = ((KnowledgeString) knowledge).mValue;
					}
					else if (k instanceof KnowledgeBool)
					{
						((KnowledgeBool) k).appendValue(((KnowledgeBool) knowledge).mValue);
					}
				} else {
					System.err.println("Two knowledges with same ID don't have same type.");
				}
				existing = true;
				break;
			}
		}
		if (!existing)
		{
			this.add(knowledge);
			updateKnowledgesHistLengths(mRuleBase.getMaxKnowledgeLengths());
		}
	}

	public void updateKnowledgesHistLengths(Map<String, Long> pMaxKnowledgeHistLengths)
	{
		for (Knowledge kit : this)
		{
			if (pMaxKnowledgeHistLengths.containsKey(kit.mID))
			{
				kit.setHistoryLength(pMaxKnowledgeHistLengths.get(kit.mID));
			}
		}
	}

}
