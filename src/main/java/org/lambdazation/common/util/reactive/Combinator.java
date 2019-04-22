package org.lambdazation.common.util.reactive;

import java.util.function.Function;

import org.lambdazation.common.util.Functional;
import org.lambdazation.common.util.data.Product;

public interface Combinator {
	static <A> Flow<Event<A>> increment(A a, Event<Function<A, A>> event) {
		// @formatter:off
		return Flow
			.<A, Event<A>> bfix(
				b -> Flow
				.retrieve(b.fmap(Functional.reverse()), event).compose(
				e -> Flow
				.store(a, e).fmap(Product.ofProductRight(e))));
		// @formatter:on
	}

	static <A> Flow<Behavior<A>> accumulate(A a, Event<Function<A, A>> event) {
		// @formatter:off
		return Flow
			.<A, Behavior<A>> bfix(
				b -> Flow
				.retrieve(b.fmap(Functional.reverse()), event).compose(
				e -> Flow
				.store(a, e).fmap(Product.ofProductBoth())));
		// @formatter:on
	}

	static <A, B> Flow<Event<A>> replace(Behavior<A> behavior, Event<B> event) {
		return Flow.retrieve(behavior.fmap(Functional.constant()), event);
	}
}
