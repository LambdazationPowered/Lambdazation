package org.lambdazation.common.network.message.field.feature;

public interface FeatureVarying {
	default boolean varying() {
		return false;
	}
}
