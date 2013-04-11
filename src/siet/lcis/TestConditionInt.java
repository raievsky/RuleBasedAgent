package siet.lcis;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConditionInt {

	KnowledgeInt knowledge = new KnowledgeInt("test");
	ConditionInt condition = new ConditionInt("test condtion", "test", 2, 10);
	
	@Test
	public void knowledgeNotValidTest()
	{
		assertTrue(!condition.match(knowledge));
	}
	
	@Test
	public void simpleValueTest()
	{
		knowledge.setHistoryLength(10);
		knowledge.clearHistory();
		condition.setHistoryLength(0);
		condition.setAccumulatedTimeThreshold(0);
		knowledge.appendValue(1);
		assertTrue(!condition.match(knowledge));
		knowledge.appendValue(11);
		assertTrue(!condition.match(knowledge));
		knowledge.appendValue(3);
		assertTrue(condition.match(knowledge));
	}
	
	@Test
	public void generalCaseTest() throws InterruptedException
	{
		knowledge.setHistoryLength(10);
		knowledge.clearHistory();
		condition.setHistoryLength(3);
		condition.setAccumulatedTimeThreshold(1);
		
		knowledge.appendValue(1);
		Thread.sleep(100);
		assertTrue(!condition.match(knowledge));
		knowledge.appendValue(3);
		Thread.sleep(800);
		assertTrue(!condition.match(knowledge));
		knowledge.appendValue(1);
		Thread.sleep(100);
		assertTrue(!condition.match(knowledge));
		knowledge.appendValue(3);
		Thread.sleep(300);
		assertTrue(condition.match(knowledge));
	}
}
