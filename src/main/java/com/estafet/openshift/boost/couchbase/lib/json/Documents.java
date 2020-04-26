package com.estafet.openshift.boost.couchbase.lib.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Documents {

	private final JSONObject jo;

	public Documents(JSONObject jo) {
		this.jo = jo;
	}

	@SuppressWarnings("unchecked")
	public List<Document> getDocuments() {
		List<Document> documents = new ArrayList<Document>();
		JSONArray documentsArray = (JSONArray) jo.get("documents");
		Iterator<JSONObject> documentsIterator = documentsArray.iterator();
		while (documentsIterator.hasNext()) {
			JSONObject document = documentsIterator.next();
			documents.add(new Document(document));
		}
		return documents;
	}

}
