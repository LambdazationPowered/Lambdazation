package org.lambdazation.common.network.message.field.feature;

public interface FeatureLimit {
	default int limit() {
		return -1;
	}
}
