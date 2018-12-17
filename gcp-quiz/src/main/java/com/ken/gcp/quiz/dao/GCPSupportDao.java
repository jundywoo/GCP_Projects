package com.ken.gcp.quiz.dao;

import org.springframework.beans.factory.InitializingBean;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.KeyFactory;

public abstract class GCPSupportDao implements InitializingBean {

	private Datastore datastore;
	private KeyFactory keyFactory;

	public KeyFactory getKeyFactory() {
		return keyFactory;
	}

	protected Datastore getDatastore() {
		return datastore;
	}

	@Override
	public final void afterPropertiesSet() throws Exception {
		datastore = DatastoreOptions.getDefaultInstance().getService(); // Authorized Datastore service
		String kindName = getKindName();
		keyFactory = getDatastore().newKeyFactory().setKind(kindName);
		init();
	}

	protected abstract String getKindName();

	protected void init() {

	};

}
