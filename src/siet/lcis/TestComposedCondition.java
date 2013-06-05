package siet.lcis;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestComposedCondition {

	KnowledgeString ks = new KnowledgeString("stringKnowledge");
	KnowledgeInt ki = new KnowledgeInt("intKnowledge");
	ConditionString conditionString = new ConditionString("string condtion", "stringKnowledge", "etat1");
	ConditionInt conditionInt = new ConditionInt("int condtion", "intKnowledge", 2, 10);
	ComposedCondition cc = new ComposedCondition("composed condition", "and", conditionString, conditionInt);
	WorldModel wm = new WorldModel();
	RuleBase rb = new RuleBase();
	
	@Before
	public void initializeWorld()
	{
		wm.setRuleBase(rb);
		rb.setWorldModel(wm);
	}
	
	@Test
	public void knowledgeNotValidTest()
	{
		assertTrue(!cc.match(wm));
	}
	
	@Test
	public void simpleValueTest()
	{
		ks.setHistoryLength(10);
		ki.setHistoryLength(10);
		ks.clearHistory();
		ki.clearHistory();
		
		conditionString.setHistoryLength(0);
		conditionString.setAccumulatedTimeThreshold(0);
		conditionInt.setHistoryLength(0);
		conditionInt.setAccumulatedTimeThreshold(0);
		
		ks.appendValue("");
		ki.appendValue(3);
		
		wm.push(ks);
		wm.push(ki);
		
		assertTrue(!cc.match(wm));
		
		ks.appendValue("etat0");
		wm.push(ks);
		assertTrue(!cc.match(wm));
		
		ks.appendValue("etat1");
		wm.push(ks);
		assertTrue(cc.match(wm));
		
		ki.appendValue(1);
		wm.push(ki);
		assertTrue(!cc.match(wm));
	}
	
	@Test
	public void generalCaseTest() throws InterruptedException
	{
		wm.clear();
		ks.setHistoryLength(10);
		ks.clearHistory();
		ki.setHistoryLength(10);
		ki.clearHistory();
		conditionString.setHistoryLength(3);
		conditionString.setAccumulatedTimeThreshold(1);
		conditionInt.setHistoryLength(3);
		conditionInt.setAccumulatedTimeThreshold(2);
		
		
		ks.appendValue("etat0");
		ki.appendValue(3);
		wm.push(ks);
		wm.push(ki);
		
		Thread.sleep(100);
		assertTrue(!cc.match(wm));
		
		ks.appendValue("etat1");
		Thread.sleep(800);
		assertTrue(!cc.match(wm));
		
		ks.appendValue("etat0");
		Thread.sleep(100);
		assertTrue(!cc.match(wm));
		
		ks.appendValue("etat1");
		Thread.sleep(300);
		
		// ki: 1300 matching time -> conditionInt not satisfied
		// ks: 1100 matching time -> conditionString satisfied
		assertTrue(!cc.match(wm));
		
		Thread.sleep(800);
		// ki: 2100 matching time -> conditionInt satisfied
		// ks: 1900 matching time -> conditionString satisfied
		assertTrue(cc.match(wm));
		
		ki.appendValue(1);
		wm.push(ki);
		Thread.sleep(1100);
		// ki: 1900 matching time -> conditionInt not satisfied
		// ks: 3000 matching time -> conditionString satisfied
		assertTrue(!cc.match(wm));
		
	}
}
