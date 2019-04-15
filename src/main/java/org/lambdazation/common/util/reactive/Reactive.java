package org.lambdazation.common.util.reactive;

import org.lambdazation.common.util.data.Unit;

public final class Reactive {
	public void resume() {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public void suspend() {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public State state() {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public static Reactive build(Flow<Unit> flow) {
		// TODO NYI
		throw new AbstractMethodError();
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
