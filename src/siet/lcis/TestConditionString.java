package siet.lcis;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConditionString {

	KnowledgeString knowledge = new KnowledgeString("test");
	ConditionString condition = new ConditionString("test condtion", "test", "etat1");
	
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
		knowledge.appendValue("");
		assertTrue(!condition.match(knowledge));
		knowledge.appendValue("etat0");
		assertTrue(!condition.match(knowledge));
		knowledge.appendValue("etat1");
		assertTrue(condition.match(knowledge));
	}
	
	@Test
	public void generalCaseTest() throws InterruptedException
	{
		knowledge.setHistoryLength(10);
		knowledge.clearHistory();
		condition.setHistoryLength(3);
		condition.setAccumulatedTimeThreshold(1);
		
		knowledge.appendValue("etat0");
		Thread.sleep(100);
		assertTrue(!condition.match(knowledge));
		knowledge.appendValue("etat1");
		Thread.sleep(800);
		assertTrue(!condition.match(knowledge));
		knowledge.appendValue("etat0");
		Thread.sleep(100);
		assertTrue(!condition.match(knowledge));
		knowledge.appendValue("etat1");
		Thread.sleep(300);
		assertTrue(condition.match(knowledge));
	}
}
