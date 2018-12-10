package com.ken.gcp.quiz.utils;

import com.google.cloud.datastore.StringValue;

public final class MyValueUtils {

	private MyValueUtils() {
		// blank
	}

	public static StringValue noIndexString(final String string) {
		return StringValue.newBuilder(string != null ? string : "").setExcludeFromIndexes(true).build();
	}

}
