package com.noklin;

import java.util.Collection;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class Jsonizer {
	
	public JsonValue toJsonValue(Object src){
		return asJson(new JsonElement(src));
	}

	private JsonValue asJson(JsonElement entry){
		JsonValue jsonValue = null;
		switch(entry.getType()){
		case ARRAY:
			jsonValue = fromCollection(entry.asCollection());
			break;
		case BOOLEAN:
			jsonValue = entry.asBoolean().booleanValue() ? JsonValue.TRUE : JsonValue.FALSE;
			break;
		case NULL:
			jsonValue = JsonValue.NULL;
			break;
		case NUMBER:
			jsonValue = fromNumber(entry.asNumber());
			break;
		case OBJECT:
			jsonValue = fromMap(entry.asMap());
			break;
		case STRING:
			jsonValue = Json.createValue(entry.asString());
			break;
		}
		return jsonValue;
	}
	
	private JsonValue fromNumber(Number num){
		if(num instanceof Double){
			return Json.createValue(num.doubleValue());
		}else if(num instanceof Float){
			return Json.createValue(num.floatValue());
		}else{
			return Json.createValue(num.longValue());
		}
	}

	private JsonValue fromCollection(Collection<JsonElement> entries){
		if(entries.isEmpty()){
			return JsonValue.EMPTY_JSON_ARRAY;
		}
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		entries.forEach(entry -> arrayBuilder.add(asJson(entry)));
		return arrayBuilder.build();
	}

	private JsonValue fromMap(Map<String,JsonElement> map){
		if(map.isEmpty()){
			return JsonValue.EMPTY_JSON_OBJECT;
		}
		JsonObjectBuilder builder = Json.createObjectBuilder();
		map.forEach((k,v) -> {
			JsonValue value = asJson(v);
			if(!JsonValue.NULL.equals(value)){
				builder.add(k, asJson(v));
			}
		});
		return builder.build();
	}
}