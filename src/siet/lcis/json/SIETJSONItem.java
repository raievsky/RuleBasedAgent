package siet.lcis.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
*/

public class SIETJSONItem {
	public String subtype = "";
	public String description = "";
	public long pubdate = 0;
	public long validity = 0;
	
	public List<SIETJSONItemContent> content = new ArrayList<SIETJSONItemContent>();

	public SIETJSONItem()
	{
		Date now = new Date();
		pubdate = now.getTime();
	}
	
/* essais parsing spécifique pour chaque type de content différent, un chiard pour ce que ça sert.
	static public class SIETJSONItemDeserializer implements JsonDeserializer<SIETJSONItem>
	{
		
//		public static final ThreadLocal<String> contentType = new ThreadLocal<String>();
		
		@Override
		public SIETJSONItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException
		{
			SIETJSONItem result = context.deserialize(json, SIETJSONItem.class); // -> Infinite recursion
//			JsonParser jparser = new JsonParser();
//			JsonArray jarray = json.getAsJsonArray();
			
			SIETJSONItemContent itemContent = null;
			if (result.subtype.equalsIgnoreCase("meteo"))
			{
				itemContent = context.deserialize(result.content.get(0).contentElement , SIETJSONItemContentMeteo.class);
			}
			
			result.content.add(itemContent);
			
//			contentType.set(result.subtype);
			
			return result;
		}


	}
*/	
}

