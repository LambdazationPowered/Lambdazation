package org.lambdazation.common.util.reactive;

import java.util.List;
import java.util.function.Consumer;

import org.lambdazation.common.util.data.Unit;

public final class Reactive {
	private boolean responsive;
	private boolean processing;
	private final List<Port<?>> ports;

	Reactive(boolean responsive, boolean processing, List<Port<?>> ports) {
		this.responsive = responsive;
		this.processing = processing;
		this.ports = ports;
	}

	public void resume() {
		if (responsive)
			return;
		responsive = true;
		ports.forEach(Port::attach);
	}

	public void suspend() {
		if (!responsive)
			return;
		responsive = true;
		ports.forEach(Port::detach);
	}

	public State state() {
		return processing ? State.PROCESSING : responsive ? State.RESUMED : State.SUSPENDED;
	}

	public static Reactive build(Flow<Unit> flow) {
		// TODO NYI
		throw new AbstractMethodError();
	}

	static final class Port<A> {
		final Source<A> source;
		final Consumer<A> callback;
		Source.Handler<A> handler;

		Port(Source<A> source, Consumer<A> callback) {
			this.source = source;
			this.callback = callback;
			this.handler = null;
		}

		void attach() {
			if (handler != null)
				throw new IllegalStateException();
			handler = source.attach(callback);
		}

		void detach() {
			if (handler == null)
				throw new IllegalStateException();
			handler.detach();
			handler = null;
		}
	}

	public enum State {
		/**
		 * Reactive is ready for processing external input
		 */
		RESUMED,
		/**
		 * Reactive is processing external input
		 */
		PROCESSING,
		/**
		 * Reactive is suspended so will not respond to any external input
		 */
		SUSPENDED
	}
}
