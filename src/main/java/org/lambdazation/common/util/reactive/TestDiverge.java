package org.lambdazation.common.util.reactive;

import java.util.function.Consumer;

import org.lambdazation.common.util.data.Product;
import org.lambdazation.common.util.data.Unit;

public final class TestDiverge {
	public static void main(String[] args) {
		Product<Consumer<Unit>, Source<Unit>> product = Source.newSource();
		Consumer<Unit> fire = product.left;
		Source<Unit> source = product.right;

		// @formatter:off
		Flow<Unit> flow =
			Flow.input(source).compose(
			inputA ->
			Flow.<Unit, Event<Unit>> efix(
				eventA ->
				Flow.pure(Product.ofProductBoth(Event.mappend((unit1, unit2) -> Unit.UNIT, inputA, eventA))))
			.compose(
			eventA ->
			Flow.output(eventA.replace(() -> System.out.println("123")))));
		// @formatter:on

		Reactive reactive = Reactive.build(flow);
		reactive.resume();
		
		fire.accept(Unit.UNIT);
	}
}
