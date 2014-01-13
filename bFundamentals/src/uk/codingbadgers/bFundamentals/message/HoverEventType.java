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

public enum HoverEventType {
	SHOW_TOOLTIP("show_text"),
	SHOW_ITEM("show_item"),
	SHOW_ACHIEVEMENT("show_achievement"),
	;

	private static final Map<String, HoverEventType> BY_ID = new HashMap<String, HoverEventType>();
	
	static {
		for (HoverEventType type : values()) {
			BY_ID.put(type.id, type);
		}
	}
	
	private String id;

	private HoverEventType(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public static class HoverEventSerializer implements JsonSerializer<HoverEventType>, JsonDeserializer<HoverEventType> {

		@Override
		public HoverEventType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			if (!json.isJsonPrimitive()) {
				return null;
			}
			
			return BY_ID.get(json.getAsString());
		}

		@Override
		public JsonElement serialize(HoverEventType src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(src.id.toLowerCase());
		}
		
	}
}
