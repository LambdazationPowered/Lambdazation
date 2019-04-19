package org.lambdazation.common.util.reactive;

import org.lambdazation.common.util.data.Unit;

public class Testcode {
	public static void main(String[] args) {
		Source<Unit> clieckedSource = callback -> () -> {};

		// @formatter:off
		Flow<Unit> flow = Flow
			.input(clieckedSource).compose(
			clickedEvent -> Combinator
			.increment(0, clickedEvent.fmap(unit -> count -> count + 1)).compose(
			countedEvent -> Flow
			.output(countedEvent.fmap(count -> () -> System.out.println(count)))));
		// @formatter:on

		Reactive reactive = Reactive.build(flow);
		reactive.resume();
	}
}
