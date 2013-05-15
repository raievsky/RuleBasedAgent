package siet.lcis;

/*
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
*/

public class SIETJSONItemContent {
	
	String level = null;
	String title = null;
	
	String town = null;
	String weather = null;
	Integer temperature = null;
	
/* essais parsing spécifique pour chaque type de content différent, un chiard pour ce que ça sert.
//	String contentString;
//	JsonElement contentElement;
	
//	public static final ThreadLocal<String> contentType = new ThreadLocal<String>();
	
	static public class SIETJSONItemContentDeserializer implements JsonDeserializer<SIETJSONItemContent>
	{
		@Override
		public SIETJSONItemContent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException
		{
			SIETJSONItemContent result = new SIETJSONItemContent();
//			result.contentString = json.getAsString();
			result.contentElement = json;
			return result;
		}
	}
*/
}
