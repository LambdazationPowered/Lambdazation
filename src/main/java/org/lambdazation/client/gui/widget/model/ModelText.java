package org.lambdazation.client.gui.widget.model;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelText extends ModelBase {
	public static final Attribute<ModelText, String> ATTRIBUTE_TEXT = attribute(ModelText::getText, ModelText::setText);

	private String text;

	public ModelText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
