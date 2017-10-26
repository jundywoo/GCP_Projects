package com.ken.gcp.quiz.utils;

import com.google.cloud.datastore.StringValue;

public final class MyValueUtils {

	private MyValueUtils() {
		// blank
	}

	public static StringValue noIndexString(String string) {
		return StringValue.newBuilder(string).setExcludeFromIndexes(true).build();
	}

}
