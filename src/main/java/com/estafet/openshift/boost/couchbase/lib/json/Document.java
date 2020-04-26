package com.estafet.openshift.boost.couchbase.lib.json;

import java.lang.reflect.Field;

import org.json.simple.JSONObject;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.repository.annotation.Id;

public class Document {

	private final JSONObject jo;
	
	public Document(JSONObject jo) {
		this.jo = jo;
	}

	public void save(Bucket bucket) {
		String id = getId();
		jo.remove(getIdProperty());
		bucket.upsert(JsonDocument.create(id, JsonObject.fromJson(jo.toJSONString())));
	}
	
	private String getIdProperty() {
		try {
			String clazz = (String)jo.get("_class");
			for (Field field : Class.forName(clazz).getDeclaredFields()) {
				if (field.getAnnotation(Id.class) != null) {
					return field.getName();
				}
			}
		} catch (SecurityException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}		
		throw new RuntimeException("Cannot find Id field");
	}

	public String getId() {
		String idProperty = getIdProperty();
		return (String)jo.get(idProperty);
	}
	
}
