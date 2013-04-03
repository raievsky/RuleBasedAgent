package siet.lcis;

import java.util.Iterator;

public class Condition {
	
	String mVarName = "temperature";
	int mMin = 30;
	int mMax = 10000;

	public boolean match(WorldModel pWM)
	{
		boolean matchFound = false;
		Iterator<Knowledge> kIt = pWM.iterator();
		Knowledge k;
		while (!matchFound && kIt.hasNext())
		{
			k = kIt.next();
			
			matchFound = k.mAttribute == mVarName &&
					Integer.parseInt(k.mValue) > mMin;
		}
		return matchFound;
	}
	
}
