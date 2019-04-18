package org.lambdazation.common.util.reactive;

import java.util.function.Function;

import org.lambdazation.common.util.data.Product;
import org.lambdazation.common.util.data.Unit;
import org.lambdazation.common.util.eval.Lazy;

/**
 * Flow is intended for modeling reactive flow in monadic ways.
 * <p>
 * Typeclass:
 * <ul>
 * <li>Functor Flow</li>
 * <li>Applicative Flow</li>
 * <li>Monad Flow</li>
 * <li>MonadFix Flow</li>
 * </ul>
 *
 * @param <A>
 *            Type of monadic result
 */
public abstract class Flow<A> {
	Flow() {

	}

	static class Fmap<A, B> extends Flow<B> {
		final Flow<A> parent;
		final Function<A, B> f;

		Fmap(Flow<A> parent, Function<A, B> f) {
			this.parent = parent;
			this.f = f;
		}
	}

	static class Apply<A, B> extends Flow<B> {
		final Flow<A> parent;
		final Flow<Function<A, B>> flow;

		Apply(Flow<A> parent, Flow<Function<A, B>> flow) {
			this.parent = parent;
			this.flow = flow;
		}
	}

	static class Compose<A, B> extends Flow<B> {
		final Flow<A> parent;
		final Function<A, Flow<B>> f;

		Compose(Flow<A> parent, Function<A, Flow<B>> f) {
			this.parent = parent;
			this.f = f;
		}
	}

	static class Pure<A> extends Flow<A> {
		final A a;

		Pure(A a) {
			this.a = a;
		}
	}

	static class Efix<A, B> extends Flow<B> {
		final Function<Event<A>, Flow<Product<Event<A>, B>>> f;

		Efix(Function<Event<A>, Flow<Product<Event<A>, B>>> f) {
			this.f = f;
		}
	}

	static class Bfix<A, B> extends Flow<B> {
		final Function<Behavior<A>, Flow<Product<Behavior<A>, B>>> f;

		Bfix(Function<Behavior<A>, Flow<Product<Behavior<A>, B>>> f) {
			this.f = f;
		}
	}

	static class Store<A> extends Flow<Behavior<A>> {
		final A a;
		final Event<A> event;

		Store(A a, Event<A> event) {
			this.a = a;
			this.event = event;
		}
	}

	static class Retrieve<A, B> extends Flow<Event<B>> {
		final Behavior<Function<A, B>> behavior;
		final Event<A> event;

		Retrieve(Behavior<Function<A, B>> behavior, Event<A> event) {
			this.behavior = behavior;
			this.event = event;
		}
	}

	static class Input<A> extends Flow<Event<A>> {
		final Source<A> source;

		Input(Source<A> source) {
			this.source = source;
		}
	}

	static class Output extends Flow<Unit> {
		final Event<Runnable> event;

		Output(Event<Runnable> event) {
			this.event = event;
		}
	}

	static class LazyEvent<A> extends Event<A> {
		final Lazy<Event<A>> lazy;

		public LazyEvent(Lazy<Event<A>> lazy) {
			this.lazy = lazy;
		}
	}

	static class LazyBehavior<A> extends Behavior<A> {
		final Lazy<Behavior<A>> lazy;

		public LazyBehavior(Lazy<Behavior<A>> lazy) {
			this.lazy = lazy;
		}
	}

	public <B> Flow<B> fmap(Function<A, B> f) {
		return new Fmap<>(this, f);
	}

	public <B> Flow<B> apply(Flow<Function<A, B>> flow) {
		return new Apply<>(this, flow);
	}

	public <B> Flow<B> compose(Function<A, Flow<B>> f) {
		return new Compose<>(this, f);
	}

	public static <A> Flow<A> pure(A a) {
		return new Pure<>(a);
	}

	public static <A, B> Flow<B> efix(Function<Event<A>, Flow<Product<Event<A>, B>>> f) {
		return new Efix<>(f);
	}

	public static <A, B> Flow<B> bfix(Function<Behavior<A>, Flow<Product<Behavior<A>, B>>> f) {
		return new Bfix<>(f);
	}

	public static <A> Flow<Behavior<A>> store(A a, Event<A> event) {
		return new Store<>(a, event);
	}

	public static <A, B> Flow<Event<B>> retrieve(Behavior<Function<A, B>> behavior, Event<A> event) {
		return new Retrieve<>(behavior, event);
	}

	public static <A> Flow<Event<A>> input(Source<A> source) {
		return new Input<>(source);
	}

	public static Flow<Unit> output(Event<Runnable> event) {
		return new Output(event);
	}
}
