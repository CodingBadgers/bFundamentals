package uk.codingbadgers.bFundamentals.message;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public enum ClickEventType {

	RUN_COMMAND("run_command"),
	SUGGEST_COMMAND("suggest_command"),
	OPEN_URL("open_url"),
	OPEN_FILE("open_file"),
	;

	private static final Map<String, ClickEventType> BY_ID = new HashMap<String, ClickEventType>();
	
	static {
		for (ClickEventType type : values()) {
			BY_ID.put(type.id, type);
		}
	}
	
	private String id;

	private ClickEventType(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public static class ClickEventSerializer implements JsonSerializer<ClickEventType>, JsonDeserializer<ClickEventType> {

		@Override
		public ClickEventType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			if (!json.isJsonPrimitive()) {
				return null;
			}
			
			return BY_ID.get(json.getAsString());
		}

		@Override
		public JsonElement serialize(ClickEventType src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(src.id.toLowerCase());
		}
		
	}
}