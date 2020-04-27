package com.estafet.openshift.boost.couchbase.lib.spring;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.couchbase.client.java.Bucket;
import com.estafet.openshift.boost.couchbase.lib.annotation.BucketSetup;
import com.estafet.openshift.boost.couchbase.lib.json.Document;
import com.estafet.openshift.boost.couchbase.lib.json.Documents;

public class CouchbaseTestExecutionListener extends AbstractTestExecutionListener {

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		CouchbaseBucket couchbaseBucket = testContext.getApplicationContext().getBean(CouchbaseBucketFactoryBean.class).getCouchbaseBucket();
		Bucket bucket = couchbaseBucket.getBucket();
		bucket.bucketManager().flush();
		for (Document document : getDocuments(testContext)) {
			document.save(bucket);
		}
		couchbaseBucket.disconnect();
	}

	private List<Document> getDocuments(TestContext testContext) {
		InputStream in = null;
		try {
			BucketSetup bucketSetup = testContext.getTestMethod().getDeclaredAnnotation(BucketSetup.class);
			in = testContext.getClass().getClassLoader().getResourceAsStream(bucketSetup.value());
			JSONObject jo = (JSONObject) new JSONParser().parse(new InputStreamReader(in));
			return new Documents(jo).getDocuments();
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
