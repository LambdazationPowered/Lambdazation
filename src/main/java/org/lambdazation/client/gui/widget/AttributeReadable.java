package org.lambdazation.client.gui.widget;

import java.util.function.Function;

import org.lambdazation.common.util.reactive.Behavior;
import org.lambdazation.common.util.reactive.Flow;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface AttributeReadable<W, A> {
	A get(W widget);

	default Flow<Behavior<A>> read(W widget) {
		return Flow.pull(() -> get(widget));
	}

	static <W, A> AttributeReadable<W, A> of(Function<W, A> getter) {
		return new AttributeReadable<W, A>() {
			@Override
			public A get(W widget) {
				return getter.apply(widget);
			}
		};
	}
}
