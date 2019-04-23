package org.lambdazation.common.util.reactive;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

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

		Source<Unit> button1ClickedSource = Source.newSource(proxy -> button1.addActionListener(event -> proxy.accept(Unit.UNIT)));
		Source<Unit> button2ClickedSource = Source.newSource(proxy -> button2.addActionListener(event -> proxy.accept(Unit.UNIT)));

		// @formatter:off
		Flow<Unit> flow = Flow
			.input(button1ClickedSource).compose(
			button1Clicked -> Flow
			.input(button2ClickedSource).compose(
			button2Clicked -> Flow
			.output(button1Clicked.fmap(unit1 -> () -> output.setText("Action1"))).compose(
			unit2 -> Flow
			.output(button2Clicked.fmap(unit3 -> () -> output.setText("Action2"))))));
		// @formatter:on

		SwingUtilities.invokeLater(() -> {
			Reactive reactive = Reactive.build(flow);
			reactive.resume();

			frame.setVisible(true);
		});

	}
}
