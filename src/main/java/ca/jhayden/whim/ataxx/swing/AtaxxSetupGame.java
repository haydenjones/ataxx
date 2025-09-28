package ca.jhayden.whim.ataxx.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class AtaxxSetupGame extends JPanel implements ActionListener {
	private static final long serialVersionUID = 3270638458907465976L;

	private final JButton button = new JButton("Start Standard Game");
	private final GameHub gameHub;

	public AtaxxSetupGame(GameHub hub) {
		super();
		this.gameHub = hub;
		this.add(button);

		// Add Listeners
		button.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		gameHub.startNewGame();
	}
}
