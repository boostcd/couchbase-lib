package com.estafet.openshift.boost.couchbase.lib.spring;

public class CouchbaseBucketFactoryBean {

	private String host;
	private String bucket;
	private String password;
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public CouchbaseBucket getCouchbaseBucket() {
		return CouchbaseBucket.builder()
				.setBucket(bucket)
				.setPassword(password)
				.setHost(host)
				.build();
	}
	
	
}
