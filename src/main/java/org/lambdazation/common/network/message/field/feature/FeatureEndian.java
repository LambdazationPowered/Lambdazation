package org.lambdazation.common.network.message.field.feature;

public interface FeatureEndian {
	default boolean networkEndian() {
		return true;
	}
}
