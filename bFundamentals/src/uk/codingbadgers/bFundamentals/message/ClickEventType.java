package uk.codingbadgers.bFundamentals.message;

import java.lang.reflect.Type;

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
			
			return valueOf(json.getAsString().toUpperCase());
		}

		@Override
		public JsonElement serialize(ClickEventType src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(src.id.toLowerCase());
		}
		
	}
}