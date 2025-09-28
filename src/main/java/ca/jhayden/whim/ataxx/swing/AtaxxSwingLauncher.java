package ca.jhayden.whim.ataxx.swing;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class AtaxxSwingLauncher implements Runnable {
	public static void main(String[] args) {
		final var launcher = new AtaxxSwingLauncher();
		SwingUtilities.invokeLater(launcher);
	}

	@Override
	public void run() {
		AtaxxJFrame ajf = new AtaxxJFrame();
		ajf.setTitle("ATAXX");
		ajf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ajf.pack();
		ajf.setVisible(true);
	}
}
