package com.estafet.openshift.boost.couchbase.lib.annotation;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.estafet.openshift.boost.couchbase.lib.testdata.CouchbaseTestData;

public class CouchbaseTestExecutionListener extends AbstractTestExecutionListener {

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		CouchbaseTestData.read(testContext).update();
	}

}
