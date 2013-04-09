package siet.lcis;

import java.util.Date;

public class Transition<T> {
	
	public T mValue;
	public Date mDate;
	
	public Transition(T pValue, Date pDate) {
		this.mValue = pValue;
		this.mDate = pDate;
	}
}
