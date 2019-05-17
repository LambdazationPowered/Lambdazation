package org.lambdazation.client.gui.widget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lambdazation.client.gui.widget.view.ViewButton;
import org.lambdazation.common.util.Uninitialized;
import org.lambdazation.common.util.data.Unit;
import org.lambdazation.common.util.reactive.Flow;
import org.lambdazation.common.util.reactive.Reactive;
import org.lambdazation.common.util.reactive.Source;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class WidgetButton<M extends ModelBase, V extends ViewButton<M>> extends WidgetBase<M, V> {
	private final Runnable onButtonActivated;

	private final Consumer<Unit> activeButtonProxy;

	public WidgetButton(M model, V view, Runnable onButtonActivated) {
		super(model, view);
		this.onButtonActivated = onButtonActivated;

		Uninitialized<Source<Unit>> sourceActiveButton = new Uninitialized<>();
		this.activeButtonProxy = Source.newSource(sourceActiveButton);

		// @formatter:off
        Flow<Unit> flow = Flow
            .input(sourceActiveButton.get()).compose(
            eventActiveButton -> Flow
            .output(eventActiveButton.replace(this.onButtonActivated)));
        // @formatter:on

		Reactive.react(flow);
	}

	@Override
	public Action onKeyboardKey(InputContext ctx, int key, boolean pressed) {
		Action action = Action.CONTINUE;

		if (key == GLFW.GLFW_KEY_ENTER && !pressed) {
			if (getView().isFocused()) {
				activeButtonProxy.accept(Unit.UNIT);
				action = Action.HANDLE;
			}
		}

		if (!action.handleInput)
			action = super.onKeyboardKey(ctx, key, pressed);
		return action;
	}

	@Override
	public Action onMouseButton(InputContext ctx, int button, boolean pressed) {
		Action action = Action.CONTINUE;

		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && !pressed) {
			if (ctx.mouseContext.localX <= getView().getWidth() && ctx.mouseContext.localY <= getView().getHeight()) {
				activeButtonProxy.accept(Unit.UNIT);
				action = Action.FOCUS;
			}
		}

		if (!action.handleInput)
			action = super.onMouseButton(ctx, button, pressed);
		return action;
	}
}
