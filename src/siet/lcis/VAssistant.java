package siet.lcis;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class VAssistant extends Thread {

	WorldModel mWorldModel = new WorldModel();
	RuleBase mRuleBase = new RuleBase();
	
	public ConcurrentLinkedQueue<Stimulus> mPerceptions = new ConcurrentLinkedQueue<Stimulus>();
	
	public VAssistant()
	{
		mRuleBase.add(new Rule(new Condition(), new Action()));
	}
	
	public void run()
	{
		// Initialize communication thread
		CommHandler lCommhandler = new CommHandler(this);
		lCommhandler.start();
		
		do {
			try
			{
				Thread.sleep(500);
				updateWorldModel();
				checkRules();
				executeActiveActions();
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}

		} while (true);
	}


	private void executeActiveActions()
	{
		/**
		 * Execute actions of active rules
		 */
		/**
		 * It will be needed to add a decision making process.
		 * Priority between rules ?
		 * Utility of different actions ?
		 * 
		 */
		
		for (Iterator<Rule> ruleIt = mRuleBase.iterator(); ruleIt.hasNext();)
		{
			Rule r = ruleIt.next();
			if (r.mActive)
			{
				System.out.println("an action is triggered");
				r.mActive = false;
			}
		}
	}


	private void checkRules()
	{
		/**
		 * Mark rules which condition match current perceived situation as active.
		 */
		for (Iterator<Rule> ruleIt = mRuleBase.iterator(); ruleIt.hasNext();)
		{
			Rule r = ruleIt.next();
			r.match(mWorldModel);
		}
		
	}


	private void updateWorldModel()
	{
		/**
		 * Update world model according to current perceptions
		 */
		Stimulus stim = null;
		if (!mPerceptions.isEmpty())
		{
			stim = mPerceptions.poll();
		}
		
		while (stim != null)
		{
			mWorldModel.push(new Knowledge("temperature", stim.rawText()));
			stim = mPerceptions.poll();
		} 
		System.out.println(mWorldModel.toString());
	}
}
