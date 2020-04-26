package com.estafet.openshift.boost.couchbase.lib.spring;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;

public class CouchbaseBucket {

	private Cluster cluster;
	private String bucket;
	private String password;
	
	public Bucket getBucket() {
		return cluster.openBucket(bucket, password);
	}
	
	public void disconnect() {
		cluster.disconnect();
	}
	
	public static CouchbaseBucketBuilder builder() {
		return new CouchbaseBucketBuilder();
	}
	
	public static class CouchbaseBucketBuilder {
		
		private String host;
		private String bucket;
		private String password;
		
		private CouchbaseBucketBuilder() {}

		public CouchbaseBucketBuilder setHost(String host) {
			this.host = host;
			return this;
		}

		public CouchbaseBucketBuilder setBucket(String bucket) {
			this.bucket = bucket;
			return this;
		}
		
		public CouchbaseBucketBuilder setPassword(String password) {
			this.password = password;
			return this;
		}
		
		public CouchbaseBucket build() {
			CouchbaseBucket couchbaseBucket = new CouchbaseBucket();
			couchbaseBucket.bucket = bucket;
			couchbaseBucket.password = password;
			couchbaseBucket.cluster = CouchbaseCluster.create(host);
			return couchbaseBucket;
		}
		
	}
	
}
