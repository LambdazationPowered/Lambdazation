package org.lambdazation.common.util.reactive;

import java.util.function.Function;

import org.lambdazation.common.util.Functional;
import org.lambdazation.common.util.data.Product;

public interface Combinator {
	static <A> Flow<Event<A>> increment(A a, Event<Function<A, A>> event) {
		return Flow
			.<Product<Event<A>, Behavior<A>>> mfix(Product.unboxProduct(e -> b -> Flow
				.retrieve(b
					.fmap(Functional.reverse()), event)
				.compose(e0 -> Flow
					.store(a, e0)
					.fmap(Product.ofProduct(e0)))))
			.fmap(Product::left);
	}
}
