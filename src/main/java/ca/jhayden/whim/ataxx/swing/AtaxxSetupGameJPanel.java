package ca.jhayden.whim.ataxx.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.jhayden.whim.ataxx.engine.GameSetupType;
import ca.jhayden.whim.ataxx.ui.GameHub;

public class AtaxxSetupGameJPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 3270638458907465976L;

	private final GameHub gameHub;

	public AtaxxSetupGameJPanel(GameHub hub, String gameOverMessage) {
		super(new GridBagLayout());
		this.gameHub = hub;

		GridBagConstraints gbc = new GridBagConstraints();
		if (!gameOverMessage.isEmpty()) {
			gbc.gridwidth = GameSetupType.values().length;
			this.add(new JLabel(gameOverMessage));
		}

		gbc.gridx = 0;
		gbc.gridy = 1;
		for (GameSetupType gst : GameSetupType.values()) {
			JButton jb = new GameSetupJButton(gst);
			this.add(jb, gbc);
			jb.addActionListener(this);

			gbc.gridy = gbc.gridy + 1;
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof GameSetupJButton gsb) {
			gameHub.startNewGame(gsb.getGameSetupType());
		}
	}
}

class GameSetupJButton extends JButton {
	private static final long serialVersionUID = 1L;

	private final GameSetupType gameSetupType;

	public GameSetupJButton(GameSetupType gst) {
		super(gst.name());
		gameSetupType = gst;
	}

	GameSetupType getGameSetupType() {
		return gameSetupType;
	}
}