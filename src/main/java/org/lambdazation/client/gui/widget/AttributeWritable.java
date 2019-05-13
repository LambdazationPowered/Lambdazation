package org.lambdazation.client.gui.widget;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.lambdazation.common.util.data.Unit;
import org.lambdazation.common.util.reactive.Event;
import org.lambdazation.common.util.reactive.Flow;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface AttributeWritable<W, A> extends AttributeReadable<W, A> {
	void set(W widget, A value);

	default Flow<Unit> write(W widget, Event<A> event) {
		return Flow.output(event.fmap(value -> () -> set(widget, value)));
	}

	static <W, A> AttributeWritable<W, A> of(Function<W, A> getter, BiConsumer<W, A> setter) {
		return new AttributeWritable<W, A>() {
			@Override
			public A get(W widget) {
				return getter.apply(widget);
			}

			@Override
			public void set(W widget, A value) {
				setter.accept(widget, value);
			}
		};
	}
}
