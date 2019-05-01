package org.lambdazation.common.util.reactive;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.lambdazation.common.util.data.Unit;

public final class TestSwing {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("Test Window");
		frame.setSize(800, 600);
		frame.setLayout(new FlowLayout());

		JButton button1 = new JButton("Button1");
		frame.add(button1);

		JButton button2 = new JButton("Button2");
		frame.add(button2);

		JTextField output = new JTextField(60);
		frame.add(output);

		Source<Unit> button1ClickSource = Source.newSource(callback -> {
			ActionListener listener = event -> callback.accept(Unit.UNIT);
			button1.addActionListener(listener);
			return () -> button1.removeActionListener(listener);
		});
		Source<Unit> button2ClickSource = Source.newSource(callback -> {
			ActionListener listener = event -> callback.accept(Unit.UNIT);
			button2.addActionListener(listener);
			return () -> button2.removeActionListener(listener);
		});

		// @formatter:off
		Flow<Unit> flow =
			Flow.input(button1ClickSource).compose(
			button1Click ->
			Combinator.increment(0, button1Click.replace(count -> count + 1)).compose(
			button1ClickCountChange ->
			Flow.input(button2ClickSource).compose(
			button2Click ->
			Flow.output(button1ClickCountChange
				.fmap(count -> () -> output.setText("Button1 clicked " + count + " times"))).then(
			Flow.output(button2Click
				.replace(() -> output.setText("Button2 clicked")))))));
		// @formatter:on

		Reactive.react(flow);

		frame.setVisible(true);
	}
}
