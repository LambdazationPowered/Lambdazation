package org.lambdazation.common.util;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class StreamIterable<T> implements Iterable<T> {
	private final Stream<T> stream;

	private StreamIterable(Stream<T> stream) {
		this.stream = stream;
	}

	@Override
	public Iterator<T> iterator() {
		return stream.iterator();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		stream.forEach(action);
	}

	@Override
	public Spliterator<T> spliterator() {
		return stream.spliterator();
	}

	public static final <T> Iterable<T> of(Stream<T> stream) {
		return new StreamIterable<>(stream);
	}
}
