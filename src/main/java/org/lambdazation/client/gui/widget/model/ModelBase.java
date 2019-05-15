package org.lambdazation.client.gui.widget.model;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.lambdazation.common.util.data.Unit;
import org.lambdazation.common.util.reactive.Behavior;
import org.lambdazation.common.util.reactive.Flow;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelBase {
	public static final class Attribute<M extends ModelBase, A> {
		final Function<M, A> getter;
		final BiConsumer<M, A> setter;

		Attribute(Function<M, A> getter, BiConsumer<M, A> setter) {
			this.getter = getter;
			this.setter = setter;
		}

		public Flow<Behavior<A>> pull(M model) {
			return Flow.pull(() -> getter.apply(model));
		}

		public Flow<Unit> push(M model, Behavior<A> behavior) {
			return Flow.push(behavior, value -> setter.accept(model, value));
		}
	}

	protected static <M extends ModelBase, A> Attribute<M, A> attribute(Function<M, A> getter, BiConsumer<M, A> setter) {
		return new Attribute<>(getter, setter);
	}
}
