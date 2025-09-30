package ca.jhayden.whim.ataxx.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class AtaxxSetupGame extends JPanel implements ActionListener {
	private static final long serialVersionUID = 3270638458907465976L;

	private final JButton b2 = new JButton("You against the AI");
	private final JButton b4 = new JButton("You against 3 AI");

	private final GameHub gameHub;

	public AtaxxSetupGame(GameHub hub) {
		super();
		this.gameHub = hub;
		this.add(b2);
		this.add(b4);

		// Add Listeners
		b2.addActionListener(this);
		b4.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int numberOfPlayers = (event.getSource() == b2) ? 2 : 4;
		gameHub.startNewGame(numberOfPlayers);
	}
}
