package siet.lcis.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class SIETJSONRequest {

	public String type;
	public String description;
	public List<SIETJSONItem> items = new ArrayList<SIETJSONItem>();
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
//		gsonBuilder.registerTypeAdapter(SIETJSONItem.class, new SIETJSONItem.SIETJSONItemDeserializer());
//		gsonBuilder.registerTypeAdapter(SIETJSONItemContent.class, new SIETJSONItemContent.SIETJSONItemContentDeserializer());
		Gson gson = gsonBuilder.create();
		
		
		SIETJSONRequest sr = new SIETJSONRequest();
		sr.type = "get";
		sr.description = "meteo";
		
		SIETJSONItem item = new SIETJSONItem();
		item.subtype = "meteo";
		item.description = "demande de la météo";
		Date now = new Date();
		item.pubdate = now.getTime();
		item.validity = item.pubdate + 100000;
		
		SIETJSONItemContentMeteo meteoContent = new SIETJSONItemContentMeteo();
		meteoContent.town = "Paris";
		meteoContent.weather = "sunny";
		meteoContent.temperature = 39;
		
		item.content.add(meteoContent);
		
		sr.items.add(item);
		
		String frame = gson.toJson(sr);
		
		System.out.println(frame);
		
		
		frame = "{\"type\":\"get\",\"description\":\"meteo\",\"items\":"+ 
				   "[{\"subtype\":\"meteo\","+
				   "\"description\":\"demande de la météo\"," +
				   "\"pubdate\":1368608843192," +
				   "\"validity\":1368608943192," +
				   "\"content\":" +
				       "[{\"town\":\"Paris\"," +
				         "\"weather\":\"sunny\"," +
				         "\"temperature\":39}]}]}";
		
		SIETJSONRequest deserializedRequest = gson.fromJson(frame, SIETJSONRequest.class);
		
		SIETJSONItem rItem = deserializedRequest.items.get(0);
		SIETJSONItemContent rItemContent = rItem.content.get(0);
		
		if (rItem.subtype.equalsIgnoreCase("meteo"))
		{
//			SIETJSONItemContentMeteo meteo = gson.fromJson(rItemContent.contentString.get(0), SIETJSONItemContentMeteo.class);
//			SIETJSONItemContentMeteo meteo = (SIETJSONItemContentMeteo) rItemContent;
//			SIETJSONItemContentMeteo meteo = gson.fromJson(rItemContent.contentElement, SIETJSONItemContentMeteo.class);
//			System.out.println("request's town:"+ meteo.town);
			System.out.println("request's town:"+ rItemContent.town);
		}
	}

}
/*
{
	"type" : "",
	"Description" : "",
	"items" :
	   [
	           {
	        	   "subtype" : "",
	        	   "Description" : "",
	        	   "Pubdate" : "",
	        	   "validity" : "",
	        	   "content" : ""
	           },
	           {
	        	   "subtype" : "",
	        	   "Description" : "",
	        	   "Pubdate" : "",
	        	   "validity" : "",
	        	   "content" : ""
	           }
	   ]
}
*/