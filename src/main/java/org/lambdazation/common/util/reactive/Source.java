package org.lambdazation.common.util.reactive;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.lambdazation.common.util.Uninitialized;
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
		Consumer<A> proxy = value -> callbacks.values().forEach(callback -> callback.accept(value));
		Source<A> source = callback -> {
			Object key = new Object();
			callbacks.put(key, callback);
			return () -> callbacks.remove(key);
		};
		return Product.ofProduct(proxy, source);
	}

	static <A> Consumer<A> newSource(Uninitialized<Source<A>> uninitializedSource) {
		Map<Object, Consumer<A>> callbacks = new LinkedHashMap<>();
		Consumer<A> proxy = value -> callbacks.values().forEach(callback -> callback.accept(value));
		Source<A> source = callback -> {
			Object key = new Object();
			callbacks.put(key, callback);
			return () -> callbacks.remove(key);
		};
		uninitializedSource.init(source);
		return proxy;
	}

	static <A> Source<A> newSource(Function<Consumer<A>, Runnable> register) {
		Source<A> source = callback -> {
			Runnable unregister = register.apply(callback);
			return () -> unregister.run();
		};
		return source;
	}
}
