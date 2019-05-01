package org.lambdazation.client.gui.widget.attribute;

import org.lambdazation.client.gui.widget.WidgetComponent;

public interface AttributeReadable<W extends WidgetComponent, A> {
	A get(W widget);
}
