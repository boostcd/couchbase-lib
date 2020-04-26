package com.estafet.openshift.boost.couchbase.lib.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.test.context.TestContext;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.estafet.openshift.boost.commons.lib.properties.PropertyUtils;
import com.estafet.openshift.boost.couchbase.lib.annotation.BucketSetup;

public class CouchbaseTestData {

	private final JSONObject jo;

	public CouchbaseTestData(JSONObject jo) {
		this.jo = jo;
	}

	public String getBucket() {
		return (String)jo.get("bucket");
	}

	public String getPassword() {
		return (String)jo.get("password");
	}
	
	public void update() {
		String host = PropertyUtils.instance().getProperty("couchbase.host");
		Cluster cluster = CouchbaseCluster.create(host);
		Bucket bucket = cluster.openBucket(getBucket(), getPassword());
		for(Document document : getDocuments()) {
			bucket.remove(document.getId());
			document.save(bucket);
		}
		cluster.disconnect();
	}

	@SuppressWarnings("unchecked")
	private List<Document> getDocuments() {
		List<Document> documents = new ArrayList<Document>();
		JSONArray documentsArray = (JSONArray) jo.get("documents");
		Iterator<JSONObject> documentsIterator = documentsArray.iterator();
		while (documentsIterator.hasNext()) {
			JSONObject document = documentsIterator.next();
			documents.add(new Document(document));
		}
		return documents;
	}

	public static CouchbaseTestData read(TestContext testContext) {
		InputStream in = null;
		try {
			BucketSetup bucketSetup = testContext.getTestMethod().getDeclaredAnnotation(BucketSetup.class);
			in = testContext.getClass().getClassLoader().getResourceAsStream(bucketSetup.value());
			JSONObject jo = (JSONObject) new JSONParser().parse(new InputStreamReader(in));
			return new CouchbaseTestData(jo);
		} catch (IOException | ParseException e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}	
			}
		}
	}

}
