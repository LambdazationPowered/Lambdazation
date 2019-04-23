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

		JButton button1 = new JButton("Action1");
		frame.add(button1);

		JButton button2 = new JButton("Action2");
		frame.add(button2);

		JTextField output = new JTextField(60);
		frame.add(output);

		Source<Unit> button1ClickedSource = Source.newSource(callback -> {
			ActionListener listener = event -> callback.accept(Unit.UNIT);
			button1.addActionListener(listener);
			return () -> button1.removeActionListener(listener);
		});
		Source<Unit> button2ClickedSource = Source.newSource(callback -> {
			ActionListener listener = event -> callback.accept(Unit.UNIT);
			button2.addActionListener(listener);
			return () -> button2.removeActionListener(listener);
		});

		// @formatter:off
		Flow<Unit> flow = Flow
			.input(button1ClickedSource).compose(
			button1Clicked -> Flow
			.input(button2ClickedSource).compose(
			button2Clicked -> Flow
			.output(button1Clicked
				.replace(() -> output.setText("Action1"))).then(Flow
			.output(button2Clicked
				.replace(() -> output.setText("Action2"))))));
		// @formatter:on

		Reactive reactive = Reactive.build(flow);
		reactive.resume();
		reactive.suspend();

		frame.setVisible(true);
	}
}
