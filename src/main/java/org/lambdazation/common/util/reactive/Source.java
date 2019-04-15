package org.lambdazation.common.util.reactive;

import java.util.function.Consumer;

public interface Source<A> {
	Handler<A> attach(Consumer<A> callback);

	interface Handler<A> {
		void detach();
	}
}
