package org.lambdazation.common.util.reactive;

import java.util.function.Consumer;

@FunctionalInterface
public interface Source<A> {
	Handler<A> attach(Consumer<A> callback);

	@FunctionalInterface
	interface Handler<A> {
		void detach();
	}
}
