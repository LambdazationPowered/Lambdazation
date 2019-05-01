package org.lambdazation.client.gui.widget.attribute;

import org.lambdazation.client.gui.widget.WidgetComponent;
import org.lambdazation.common.util.data.Unit;
import org.lambdazation.common.util.reactive.Event;
import org.lambdazation.common.util.reactive.Flow;

public interface AttributeWritable<W extends WidgetComponent, A> extends AttributeReadable<W, A> {
	void set(W widget, A value);

	default Flow<Unit> update(W widget, Event<A> event) {
		return Flow.output(event.fmap(value -> () -> set(widget, value)));
	}
}
