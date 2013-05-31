package siet.lcis;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import siet.lcis.json.SIETJSONItem;
import siet.lcis.json.SIETJSONItemContent;
import siet.lcis.json.SIETJSONRequest;

import com.google.gson.Gson;

public class VAssistant extends Thread {

	WorldModel mWorldModel = new WorldModel();
	RuleBase mRuleBase = new RuleBase();	
	
	protected Map<String, CommHandlerTask> mServiceMap = new HashMap<String, CommHandlerTask>();
	
	public ConcurrentLinkedQueue<Stimulus> mPerceptions = new ConcurrentLinkedQueue<Stimulus>();
	
	protected Gson mGson = new Gson();
	
	public VAssistant()
	{
		initialize();

	}
	
	private void initialize()
	{
		mWorldModel.setRuleBase(mRuleBase);
		mRuleBase.setWorldModel(mWorldModel);

		KnowledgeInt lKTemp = new KnowledgeInt("temperature");
		lKTemp.setHistoryLength(30);
		mWorldModel.push(lKTemp);

		// Create canicule condition
		ConditionInt isCaniculeInt = new ConditionInt("canicule int", "temperature", 30, Integer.MAX_VALUE);
		isCaniculeInt.setHistoryLength(20);
		isCaniculeInt.setAccumulatedTimeThreshold(10);

		// Create canicule alert message
		SIETJSONRequest caniculeFrame = new SIETJSONRequest();
		caniculeFrame.type = "put";
		caniculeFrame.description = "alerte météo";

		SIETJSONItem cfItem = new SIETJSONItem();
		cfItem.subtype = "meteoalert";
		cfItem.description = "";
		cfItem.validity = cfItem.pubdate + 100000;

		SIETJSONItemContent itemContent = new SIETJSONItemContent();
		itemContent.level = "high";
		itemContent.title = "heat wave";
		cfItem.content.add(itemContent);

		caniculeFrame.items.add(cfItem);

		String message = mGson.toJson(caniculeFrame);

		// Create and add rule
		// receiver id must match Virtual Assistant's one (e.g. siet.lcis.dummyvassistantwidget.MainActivity mId).
		mRuleBase.add(new Rule(isCaniculeInt , new SendMessage(this, "siet.lcis.VAssistantWidget", message)));

		////////////////////////////////////
		// Create a condition that monitors user interaction with hydrate activity 
		
		ConditionBool userInactive = new ConditionBool("user inactive", "user interaction", false);
		userInactive.setHistoryLength(10);
		userInactive.setAccumulatedTimeThreshold(10);
		
		ConditionBool hydrateActiveCondition = new ConditionBool("hydratation activity active", "hydrate shown", true);
		hydrateActiveCondition.setHistoryLength(0);
		hydrateActiveCondition.setAccumulatedTimeThreshold(0);
		
		// Create SendMessage action to execute when the condition is not satisfied
		SIETJSONRequest inactivityFrame = new SIETJSONRequest();
		inactivityFrame.type = "put";
		inactivityFrame.description = "levée de doute";

		SIETJSONItem inactivityItem = new SIETJSONItem();
		inactivityItem.subtype = "alert";
		inactivityItem.description = "user inactive";
		inactivityItem.validity = inactivityItem.pubdate + 100000;

		SIETJSONItemContent iiContent = new SIETJSONItemContent();
		iiContent.level = "high";
		iiContent.title = "inactivity";
		inactivityItem.content.add(iiContent);

		inactivityFrame.items.add(inactivityItem);

		message = mGson.toJson(inactivityFrame);
		
		
		// Compose two conditions to monitor both the hydrate activity status and user interaction status
		ComposedCondition userInteractsWithHydrate = new ComposedCondition("User interacts with hydrate activity", "and", hydrateActiveCondition, userInactive);
	
		mRuleBase.add(new Rule(userInteractsWithHydrate , new SendMessage(this, "siet.lcis.VAssistantWidget", message)));
		
		
		//		ConditionBool isCanicule = new ConditionBool("Canicule Bool", "temperature", true);
		//		isCanicule.setHistoryLength(10);
		//		isCanicule.setAccumulatedTimeThreshold(5);
		//		mRuleBase.add(new Rule(isCanicule, new SendMessage(this, "meteo", "canicule bool.")));	
	}

	public void run()
	{
		// Initialize communication thread
		CommHandler lCommhandler = new CommHandler(this);
		lCommhandler.start();
		
		// Main loop
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
			// TODO parse stimuli to push appropriate knowledge to 
			// world model.
			
			SIETJSONRequest deserializedRequest = mGson.fromJson(stim.rawText(), SIETJSONRequest.class);

			SIETJSONItem rItem = deserializedRequest.items.get(0);
			SIETJSONItemContent rItemContent = rItem.content.get(0);

			if (rItem.subtype.equalsIgnoreCase("meteo"))
			{
				System.out.println("request's town:"+ rItemContent.town);
//				int temp = Integer.parseInt(stim.rawText());
				int temp = rItemContent.temperature;
				mWorldModel.push(new KnowledgeInt("temperature", temp));
			}
			else if (rItem.subtype.equalsIgnoreCase("knowledge"))
			{
				System.out.println("VAssistant updateWorldModel, received knowledge message. Knowledge id:"+ rItemContent.title);
				if (rItemContent.type.equalsIgnoreCase("bool"))
				{
					mWorldModel.push(new KnowledgeBool(rItemContent.title, rItemContent.boolValue));
					System.out.println("Knowledge value:"+ rItemContent.boolValue.toString());
				}
			}

			stim = mPerceptions.poll();
		} 
		
		// Send our world model to the debugging DisplayMessagesActivity service.
		if (!mServiceMap.isEmpty())
		{
			for (String serviceName : mServiceMap.keySet())
			{
				if (serviceName.equals("siet.lcis.DebugWorldModel"))
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
				else if (serviceName.equals("siet.lcis.VAssistantWidget"))
				{
					// TODO put this as the action of an always satisfied condition.
					
					// Send current temperature to assistant widget.
					for (Knowledge kit : mWorldModel)
					{
						if (kit.mID.equals("temperature") && kit.isValid())
						{
							// Create temperature message
							SIETJSONRequest meteoFrame = new SIETJSONRequest();
							meteoFrame.type = "put";
							meteoFrame.description = "météo";

							SIETJSONItem cfItem = new SIETJSONItem();
							cfItem.subtype = "meteo";
							cfItem.description = "";
							Date now = new Date();
							cfItem.pubdate = now.getTime();
							cfItem.validity = cfItem.pubdate + 100000;

							SIETJSONItemContent itemContent = new SIETJSONItemContent();
							itemContent.temperature = ((KnowledgeInt)kit).mValue;
							
							cfItem.content.add(itemContent);

							meteoFrame.items.add(cfItem);

							String message = mGson.toJson(meteoFrame);
							
							// System.out.println(message);
							
							CommHandlerTask task = mServiceMap.get(serviceName);
							if (task != null)
							{
								task.writeToStream(message);
							}
							else
							{
								System.err.println("ERROR (Internal): No Communication handler task available.");
							}
						}
					}
					
				}
			}
		}
	}

	/**
	 * Add a service to the map of available services.
	 * 
	 * @param pServiceId Added service identifier
	 * @param pCommHandlerTask {@link CommHandlerTask} which should be associated with
	 * the given pServiceId
	 */
	public void registerService(String pServiceId, CommHandlerTask pCommHandlerTask)
	{
		mServiceMap.put(pServiceId, pCommHandlerTask);
		System.out.println("New service registered: ["+pServiceId+"]");
	}

	public void unregisterService(String mSenderId)
	{
		mServiceMap.remove(mSenderId);
	}
	
	public CommHandlerTask getCommTask(String pServiceID)
	{
		CommHandlerTask result = mServiceMap.get(pServiceID);
		if (result == null)
		{
			System.out.println("Warning, receiver id not registered: ["+pServiceID+"]");
		}
		return result;
	}
}
