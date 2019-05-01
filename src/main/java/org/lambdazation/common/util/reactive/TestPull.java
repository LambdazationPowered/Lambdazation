package org.lambdazation.common.util.reactive;

import java.util.function.Consumer;

import org.lambdazation.common.util.data.Product;
import org.lambdazation.common.util.data.Unit;

public final class TestPull {
	public static void main(String[] args) {
		Product<Consumer<Unit>, Source<Unit>> product = Source.newSource();
		Consumer<Unit> fire = product.left;
		Source<Unit> source = product.right;

		// @formatter:off
		Flow<Unit> flow =
			Flow.input(source).compose(
			ticked ->
			Flow.pull(() -> System.currentTimeMillis()).compose(
			currentTime ->
			Combinator.replace(currentTime, ticked).compose(
			displayTime ->
			Flow.output(displayTime.fmap(time -> () -> System.out.println("Time: " + time))))));
		// @formatter:on

		Reactive.react(flow);

		while (true) {
			fire.accept(Unit.UNIT);
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}
