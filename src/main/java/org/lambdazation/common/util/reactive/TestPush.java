package org.lambdazation.common.util.reactive;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.lambdazation.common.util.data.Product;
import org.lambdazation.common.util.data.Unit;

public final class TestPush {
	public static void main(String[] args) {
		Product<Consumer<Unit>, Source<Unit>> product = Source.newSource();
		Consumer<Unit> fire = product.left;
		Source<Unit> source = product.right;

		Ref<String> ref = new Ref<>("A");

		// @formatter:off
		Flow<Unit> flow = Flow
			.pull(ref).compose(
			b -> Flow
			.push(b, ref.andThen(System.out::println)).then(Flow
			.input(source).compose(
			ticked -> Flow
			.output(ticked.replace(() -> System.out.println("Ticked"))))));
		// @formatter:on

		Reactive.react(flow);

		fire.accept(Unit.UNIT);
		ref.accept("B");
		fire.accept(Unit.UNIT);
		ref.accept("C");
		fire.accept(Unit.UNIT);
	}
}

class Ref<A> implements Supplier<A>, Consumer<A> {
	A value;

	Ref(A value) {
		this.value = value;
	}

	@Override
	public A get() {
		return value;
	}

	@Override
	public void accept(A value) {
		this.value = value;
	}
}