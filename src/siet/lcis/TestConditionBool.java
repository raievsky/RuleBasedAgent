package siet.lcis;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConditionBool {

	KnowledgeBool mkb = new KnowledgeBool("test");
	
	ConditionBool mTruecb = new ConditionBool("match true value", "test", true);
	ConditionBool mFalsecb = new ConditionBool("match false value", "test", false);
	
	@Test
	public void knowledgeNotValidTest()
	{
		assertTrue(!mTruecb.match(mkb));
		assertTrue(!mFalsecb.match(mkb));
	}
	
	@Test
	public void simpleValueTest() {
		mTruecb.setHistoryLength(0);
		mTruecb.setAccumulatedTimeThreshold(0);
		mkb.setHistoryLength(0);
		
		mkb.appendValue(false);
		assertTrue("Boolean knowledge value should be false", !mTruecb.match(mkb));
		mkb.appendValue(true);
		assertTrue("Boolean knowledge value should be true", mTruecb.match(mkb));
		
		mFalsecb.setHistoryLength(0);
		mFalsecb.setAccumulatedTimeThreshold(0);
		mkb.appendValue(true);
		assertTrue("Boolean knowledge value should be false", !mFalsecb.match(mkb));
		mkb.appendValue(false);
		assertTrue("Boolean knowledge value should be true", mFalsecb.match(mkb));
	}

	// Testing condition matching with only one transition
	@Test
	public void singleTransitionWithinHisoryTest() throws InterruptedException {
		mTruecb.setHistoryLength(5);
		mTruecb.setAccumulatedTimeThreshold(2);
		mFalsecb.setHistoryLength(5);
		mFalsecb.setAccumulatedTimeThreshold(2);
		mkb.setHistoryLength(200);
		
		// Transition ending in matching state
		// condition matching true value
		mkb.appendValue(true);
		Thread.sleep(100);
		assertTrue(!mTruecb.match(mkb));
		
		Thread.sleep(2000);
		assertTrue("Only one transition to matching state", mTruecb.match(mkb));
		
		// condition matching false value
		mFalsecb.setHistoryLength(5);
		mFalsecb.setAccumulatedTimeThreshold(2);
		mkb.clearHistory();
		mkb.appendValue(false);
		Thread.sleep(100);
		assertTrue(!mFalsecb.match(mkb));
		
		Thread.sleep(2000);
		assertTrue("Only one transition to matching state", mFalsecb.match(mkb));
	
		// Transition ending not matching state
		// condition matching a true value
		mkb.clearHistory();
		mkb.appendValue(false);
		Thread.sleep(100);
		assertTrue(!mTruecb.match(mkb));
		Thread.sleep(2000);
		assertTrue(!mTruecb.match(mkb));
		// condition matching a false value
		mkb.clearHistory();
		mkb.appendValue(true);
		Thread.sleep(100);
		assertTrue(!mFalsecb.match(mkb));
		Thread.sleep(2000);
		assertTrue(!mFalsecb.match(mkb));
	}
	
	// Testing condition matching with two transitions
	@Test
	public void TwoTransitionsWithinHisoryTest() throws InterruptedException {
		mTruecb.setHistoryLength(5);
		mTruecb.setAccumulatedTimeThreshold(2);
		mFalsecb.setHistoryLength(5);
		mFalsecb.setAccumulatedTimeThreshold(2);
		mkb.setHistoryLength(200);
		
	   // First transition ending in matching state
	      // condition matching true value
		    // not matching
		mkb.clearHistory();
		mkb.appendValue(true);
		Thread.sleep(100);
		mkb.appendValue(false);
		Thread.sleep(100);
		assertTrue(!mTruecb.match(mkb));
		Thread.sleep(2000);
		assertTrue(!mTruecb.match(mkb));
		    // matching
		mkb.clearHistory();
		mkb.appendValue(true);
		Thread.sleep(2100);
		mkb.appendValue(false);
		assertTrue(mTruecb.match(mkb));
		Thread.sleep(100);
		assertTrue(mTruecb.match(mkb));
		
	      // condition matching false value
		    // not matching
		mkb.clearHistory();
		mkb.appendValue(false);
		Thread.sleep(100);
		mkb.appendValue(true);
		Thread.sleep(100);
		assertTrue(!mFalsecb.match(mkb));
		Thread.sleep(2000);
		assertTrue(!mFalsecb.match(mkb));
		    // matching
		mkb.clearHistory();
		mkb.appendValue(false);
		Thread.sleep(2100);
		mkb.appendValue(true);
		assertTrue(mFalsecb.match(mkb));
		Thread.sleep(100);
		assertTrue(mFalsecb.match(mkb));
	
	   // First transition ending in NOT matching state
	      // condition matching true value
		mkb.clearHistory();
		mkb.appendValue(false);
		Thread.sleep(100);
		mkb.appendValue(true);
		Thread.sleep(100);
		assertTrue(!mTruecb.match(mkb));
		Thread.sleep(2000);
		assertTrue(mTruecb.match(mkb));
		    // not matching
		mkb.clearHistory();
		mkb.appendValue(false);
		Thread.sleep(2100);
		mkb.appendValue(true);
		assertTrue(!mTruecb.match(mkb));
		
	      // condition matching false value
		mkb.clearHistory();
		mkb.appendValue(true);
		Thread.sleep(100);
		mkb.appendValue(false);
		Thread.sleep(100);
		assertTrue(!mFalsecb.match(mkb));
		Thread.sleep(2000);
		assertTrue(mFalsecb.match(mkb));
		    // not matching
		mkb.clearHistory();
		mkb.appendValue(true);
		Thread.sleep(2100);
		mkb.appendValue(false);
		assertTrue(!mFalsecb.match(mkb));
	
	}
	
	
	// Testing condition matching with more than two transitions
	@Test
	public void MoreThanTwoTransitionsWithinHisoryTest() throws InterruptedException {
		mTruecb.setHistoryLength(5);
		mTruecb.setAccumulatedTimeThreshold(2);
		mFalsecb.setHistoryLength(5);
		mFalsecb.setAccumulatedTimeThreshold(2);
		mkb.setHistoryLength(200);
	
	   // First transition ending in matching state
	      // condition matching true value
		mkb.clearHistory();
		mkb.appendValue(true);
		Thread.sleep(100);
		mkb.appendValue(false);
		Thread.sleep(100);
		mkb.appendValue(true);
		Thread.sleep(100);
		assertTrue(!mTruecb.match(mkb));
		mkb.appendValue(false);
		Thread.sleep(100);
		assertTrue(!mTruecb.match(mkb));
		mkb.appendValue(true);
		Thread.sleep(2000);
		assertTrue(mTruecb.match(mkb));
	      // condition matching false value
		mkb.clearHistory();
		mkb.appendValue(false);
		Thread.sleep(100);
		mkb.appendValue(true);
		Thread.sleep(100);
		mkb.appendValue(false);
		Thread.sleep(100);
		assertTrue(!mFalsecb.match(mkb));
		mkb.appendValue(true);
		Thread.sleep(100);
		assertTrue(!mFalsecb.match(mkb));
		mkb.appendValue(false);
		Thread.sleep(2000);
		assertTrue(mFalsecb.match(mkb));
	
	   // First transition ending in NOT matching state
	      // condition matching true value
		mkb.clearHistory();
		mkb.appendValue(false);
		Thread.sleep(100);
		mkb.appendValue(true);
		Thread.sleep(100);
		mkb.appendValue(false);
		Thread.sleep(100);
		assertTrue(!mTruecb.match(mkb));
		mkb.appendValue(true);
		Thread.sleep(2000);
		assertTrue(mTruecb.match(mkb));
		mkb.appendValue(false);
		Thread.sleep(100);
		assertTrue(mTruecb.match(mkb));
	      // condition matching false value
		mkb.clearHistory();
		mkb.appendValue(true);
		Thread.sleep(100);
		mkb.appendValue(false);
		Thread.sleep(100);
		mkb.appendValue(true);
		Thread.sleep(100);
		assertTrue(!mFalsecb.match(mkb));
		mkb.appendValue(false);
		Thread.sleep(2000);
		assertTrue(mFalsecb.match(mkb));
		mkb.appendValue(true);
		Thread.sleep(100);
		assertTrue(mFalsecb.match(mkb));
	}
	
	
	// Testing condition matching with only one transition,
	// occurring before the beginning of the monitored
	// time lapse.
	@Test
	public void SingleTransitionOutsideHisoryTest() throws InterruptedException {
		mTruecb.setHistoryLength(3);
		mTruecb.setAccumulatedTimeThreshold(2);
		mFalsecb.setHistoryLength(3);
		mFalsecb.setAccumulatedTimeThreshold(2);
		mkb.setHistoryLength(200);

		mkb.clearHistory();
		mkb.appendValue(true);
		Thread.sleep(3500);
		assertTrue(mTruecb.match(mkb));
		assertTrue(!mFalsecb.match(mkb));

		mkb.clearHistory();
		mkb.appendValue(false);
		Thread.sleep(3500);
		assertTrue(mFalsecb.match(mkb));
		assertTrue(!mTruecb.match(mkb));
	}
	
	// Testing condition matching with two transitions,
	// one transition occurring before the beginning of the monitored
	// time lapse and one after.
	@Test
	public void twoTransitionsOutsideHisoryTest() throws InterruptedException {
		mTruecb.setHistoryLength(3);
		mTruecb.setAccumulatedTimeThreshold(2);
		mFalsecb.setHistoryLength(3);
		mFalsecb.setAccumulatedTimeThreshold(2);
		mkb.setHistoryLength(200);

		mkb.clearHistory();
		mkb.appendValue(false);
		Thread.sleep(2500);
		mkb.appendValue(true);
		Thread.sleep(500);
		assertTrue(!mTruecb.match(mkb));
		assertTrue(mFalsecb.match(mkb));

		mkb.clearHistory();
		mkb.appendValue(false);
		Thread.sleep(500);
		mkb.appendValue(true);
		Thread.sleep(2250);
		assertTrue(mTruecb.match(mkb));
		assertTrue(!mFalsecb.match(mkb));
		
		mkb.clearHistory();
		mkb.appendValue(true);
		Thread.sleep(2500);
		mkb.appendValue(false);
		Thread.sleep(500);
		assertTrue(mTruecb.match(mkb));
		assertTrue(!mFalsecb.match(mkb));
		
		mkb.clearHistory();
		mkb.appendValue(true);
		Thread.sleep(500);
		mkb.appendValue(false);
		Thread.sleep(2250);
		assertTrue(!mTruecb.match(mkb));
		assertTrue(mFalsecb.match(mkb));
	}
	
	// Testing condition matching with two transitions,
	// one transition occurring before the beginning of the monitored
	// time lapse and one after.
	@Test
	public void moreThanTwoTransitionsOutsideHisoryTest() throws InterruptedException {
		mTruecb.setHistoryLength(3);
		mTruecb.setAccumulatedTimeThreshold(2);
		mFalsecb.setHistoryLength(3);
		mFalsecb.setAccumulatedTimeThreshold(2);
		mkb.setHistoryLength(200);

		mkb.clearHistory();
		mkb.appendValue(true);
		Thread.sleep(100);
		mkb.appendValue(false);
		Thread.sleep(2500);
		mkb.appendValue(true);
		Thread.sleep(500);
		assertTrue(!mTruecb.match(mkb));
		assertTrue(mFalsecb.match(mkb));

		mkb.clearHistory();
		mkb.appendValue(true);
		Thread.sleep(100);
		mkb.appendValue(false);
		Thread.sleep(500);
		mkb.appendValue(true);
		Thread.sleep(2250);
		assertTrue(mTruecb.match(mkb));
		assertTrue(!mFalsecb.match(mkb));
		
		mkb.clearHistory();
		mkb.appendValue(false);
		Thread.sleep(100);
		mkb.appendValue(true);
		Thread.sleep(2500);
		mkb.appendValue(false);
		Thread.sleep(500);
		assertTrue(mTruecb.match(mkb));
		assertTrue(!mFalsecb.match(mkb));
		
		mkb.clearHistory();
		mkb.appendValue(false);
		Thread.sleep(100);
		mkb.appendValue(true);
		Thread.sleep(500);
		mkb.appendValue(false);
		Thread.sleep(2250);
		assertTrue(!mTruecb.match(mkb));
		assertTrue(mFalsecb.match(mkb));
	}
}
