package org.lambdazation.client.gui.widget;

import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lambdazation.common.util.data.Maybe;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectSortedMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WidgetContainer<M extends ModelBase> extends WidgetBase<M> {
	private final Object2ObjectSortedMap<WidgetBase<?>, Component> components;
	private Maybe<Component> focus;

	public WidgetContainer(M model) {
		super(model);

		this.components = new Object2ObjectLinkedOpenHashMap<>();
		this.focus = Maybe.ofNothing();
	}

	@Override
	protected void onFoucs() {
		super.onFoucs();
	}

	@Override
	protected void onUnfocus() {
		super.onUnfocus();
		if (focus.isJust()) {
			Component component = focus.asJust().value();
			focus = null;
			component.widget.onUnfocus();
		}
	}

	@Override
	public void draw(DrawContext ctx) {
		super.draw(ctx);
		// TODO NYI
	}

	@Override
	public Action onKeyboardKey(InputContext ctx, int key, boolean pressed) {
		Action action = super.onKeyboardKey(ctx, key, pressed);
		// TODO NYI
		return Action.CONTINUE.override(action);
	}

	@Override
	public Action onKeyboardChar(InputContext ctx, char input) {
		Action action = super.onKeyboardChar(ctx, input);
		// TODO NYI
		return Action.CONTINUE.override(action);
	}

	@Override
	public Action onMouseButton(InputContext ctx, int button, boolean pressed) {
		Action action = super.onMouseButton(ctx, button, pressed);
		// TODO NYI
		return Action.CONTINUE.override(action);
	}

	@Override
	public Action onMouseMove(InputContext ctx, double deltaX, double deltaY) {
		Action action = super.onMouseMove(ctx, deltaX, deltaY);
		// TODO NYI
		return Action.CONTINUE.override(action);
	}

	@Override
	public Action onMouseWheel(InputContext ctx, double delta) {
		Action action = super.onMouseWheel(ctx, delta);
		// TODO NYI
		return Action.CONTINUE.override(action);
	}

	private static final class Component {
		final WidgetBase<?> widget;
		double x;
		double y;

		private Component(final WidgetBase<?> widget, double x, double y) {
			this.widget = widget;
			this.x = x;
			this.y = y;
		}
	}
}
