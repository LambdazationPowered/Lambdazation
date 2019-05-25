package org.lambdazation.client.gui.widget.model;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelTextField extends ModelBase {
	public static final Attribute<ModelTextField, String> ATTRIBUTE_TEXT = attribute(ModelTextField::getText, ModelTextField::setText);

	private String text;

	public ModelTextField(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
