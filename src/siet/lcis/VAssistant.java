package siet.lcis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class VAssistant extends Thread {

	WorldModel mWorldModel = new WorldModel();
	RuleBase mRuleBase = new RuleBase();
	
	protected Map<String, CommHandlerTask> mServiceMap = new HashMap<String, CommHandlerTask>();
	
	public ConcurrentLinkedQueue<Stimulus> mPerceptions = new ConcurrentLinkedQueue<Stimulus>();
	
	public VAssistant()
	{
		mRuleBase.add(new Rule(new ConditionInt("Canicule", "temperature", 30, Integer.MAX_VALUE), new Action()));
		ConditionBool isCanicule = new ConditionBool("Canicule Bool", "temperature", true);
		isCanicule.setHistoryLength(10);
		isCanicule.setAccumulatedTimeThreshold(5);
		mRuleBase.add(new Rule(isCanicule, new Action()));
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
				System.out.println("Action ["+r.mCondition.mID+"] is triggered");
				r.mAction.execute();
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
			int temp = Integer.parseInt(stim.rawText());
			boolean b = temp > 30;
			mWorldModel.push(new KnowledgeBool("temperature", b));
			stim = mPerceptions.poll();
		} 
		
		if (!mServiceMap.isEmpty())
		{
			for (String serviceName : mServiceMap.keySet())
			{
				if (serviceName.equals("siet.lcis.DisplayMessagesActivity"))
				{
					CommHandlerTask task = mServiceMap.get(serviceName);
					if (task != null)
					{
						task.writeToStream(mWorldModel.toString());
					}
					else
					{
						System.err.println("ERROR (Internal): No Communication handler task available.");
					}
				}
			}
		}
	}

	public void registerService(String pServiceId, CommHandlerTask pCommHandlerTask)
	{
		mServiceMap.put(pServiceId, pCommHandlerTask);
		System.out.println("New service registered: ["+pServiceId+"]");
	}

	public void unregisterService(String mSenderId)
	{
		mServiceMap.remove(mSenderId);
	}
}
