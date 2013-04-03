package siet.lcis;

import java.util.ArrayList;
import java.util.Iterator;

public class WorldModel extends ArrayList<Knowledge> {

	public void push(Knowledge knowledge)
	{
		boolean existing = false;
		for (Iterator<Knowledge> kIt = this.iterator(); kIt.hasNext();) {
			Knowledge k = kIt.next();
			if (k.mAttribute == knowledge.mAttribute)
			{
				k.mValue = knowledge.mValue;
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
