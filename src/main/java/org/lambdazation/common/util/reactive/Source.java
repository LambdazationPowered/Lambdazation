package org.lambdazation.common.util.reactive;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.lambdazation.common.util.data.Product;

@FunctionalInterface
public interface Source<A> {
	Handler<A> attach(Consumer<A> callback);

	@FunctionalInterface
	interface Handler<A> {
		void detach();
	}

	static <A> Product<Consumer<A>, Source<A>> newSource() {
		Map<Object, Consumer<A>> callbacks = new LinkedHashMap<>();
		Source<A> source = callback -> {
			Object key = new Object();
			callbacks.put(key, callback);
			return () -> callbacks.remove(key);
		};
		Consumer<A> proxy = value -> callbacks.values().forEach(callback -> callback.accept(value));
		return Product.ofProduct(proxy, source);
	}
}
