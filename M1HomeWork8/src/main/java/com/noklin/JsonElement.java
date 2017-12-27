package com.noklin;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JsonElement {

	static enum Type {
		OBJECT, ARRAY, NUMBER, STRING, NULL, BOOLEAN
	}

	private Object obj;

	JsonElement(Object obj) {
		this.obj = obj;
		recognizeType();
	}

	Collection<JsonElement> asCollection() {
		return obj.getClass().isArray() ? elementsFromArray() : elementsFromCollection();
	}

	private Collection<JsonElement> elementsFromCollection() {
		Collection<JsonElement> result = Collections.emptyList();
		Collection<?> collection = (Collection<?>) obj;
		for (Object object : collection) {
			if (result.isEmpty()) {
				result = new ArrayList<>();
			}
			result.add(new JsonElement(object));
		}
		return result;
	}

	private Collection<JsonElement> elementsFromArray() {
		Collection<JsonElement> result = Collections.emptyList();
		for (int i = 0; i < Array.getLength(obj); i++) {
			if (result.isEmpty()) {
				result = new ArrayList<>();
			}
			result.add(new JsonElement(Array.get(obj, i)));
		}
		return result;
	}

	Number asNumber() {
		return (Number) obj;
	}

	String asString() {
		return obj.toString();
	}

	private Map<String, JsonElement> mapOfElementsFromMap() {
		Map<String, JsonElement> result = Collections.emptyMap();
		Map<?, ?> map = (Map<?, ?>) obj;
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (result.isEmpty()) {
				result = new HashMap<>();
			}
			String name = String.valueOf(entry.getKey());
			if (name == null) {
				throw new NullPointerException("name == null");
			}
			result.put(name, new JsonElement(entry.getValue()));
		}
		return result;
	}

	private Map<String, JsonElement> mapOfElementsFromObject() {
		Map<String, JsonElement> map = Collections.emptyMap();
		Field[] fields = obj.getClass().getDeclaredFields();
		try {
			for (Field field : fields) {
				if(!transientTest(field)){
					if (map.isEmpty()) {
						map = new HashMap<>();
					}
					field.setAccessible(true);
					map.put(field.getName(), new JsonElement(field.get(obj)));
				}
			}
		} catch (IllegalAccessException ex) {
			/* NOP */
		}
		return map;
	}

	Map<String, JsonElement> asMap() {
		return obj instanceof Map<?, ?> ? mapOfElementsFromMap() : mapOfElementsFromObject();
	}

	private boolean transientTest(Field field) {
		return (Modifier.TRANSIENT & field.getModifiers()) != 0;
	}

	Boolean asBoolean() {
		return (Boolean) obj;
	}

	private Type type;
	Type getType() {
		return type;
	}

	private void recognizeType() {
		if (obj == null) {
			type = Type.NULL;
		} else if (obj instanceof Number) {
			type = Type.NUMBER;
		} else if (obj instanceof CharSequence || obj.getClass().isEnum() || obj instanceof Character) {
			type = Type.STRING;
		} else if (obj.getClass().isArray() || obj instanceof Collection<?>) {
			type = Type.ARRAY;
		} else if (obj instanceof Boolean) {
			type = Type.BOOLEAN;
		} else {
			type = Type.OBJECT;
		}
	}

	public String toString() {
		return "Entry [type=" + type + "]";
	}
}