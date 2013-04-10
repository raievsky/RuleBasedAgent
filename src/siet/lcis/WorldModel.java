package siet.lcis;

import java.util.ArrayList;
import java.util.Iterator;

public class WorldModel extends ArrayList<Knowledge> {

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
						((KnowledgeInt) k).mValue = ((KnowledgeInt) knowledge).mValue;
					}
					else if (k instanceof KnowledgeString)
					{
						((KnowledgeString) k).mValue = ((KnowledgeString) knowledge).mValue;
					}
					else if (k instanceof KnowledgeBool)
					{
						((KnowledgeBool) k).appendValue(((KnowledgeBool) knowledge).mValue);
						System.out.println("pushing stimulus in world model");
//						((KnowledgeBool) k).mValue = ((KnowledgeBool) knowledge).mValue;
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
		}
	}

}
