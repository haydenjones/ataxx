package ca.jhayden.whim.ataxx.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import ca.jhayden.whim.ataxx.engine.GameSetupType;
import ca.jhayden.whim.ataxx.ui.GameHub;

public class AtaxxSetupGameJPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 3270638458907465976L;

	private final GameHub gameHub;

	public AtaxxSetupGameJPanel(GameHub hub, ActionListener al) {
		super(new GridBagLayout());
		this.gameHub = hub;

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 1;
		for (GameSetupType gst : GameSetupType.values()) {
			JButton jb = new GameSetupJButton(gst);
			this.add(jb, gbc);
			jb.addActionListener(al);

			gbc.gridy = gbc.gridy + 1;
		}
		
		this.add(new AttributesJPanel(), gbc);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof GameSetupJButton gsb) {
			gameHub.startNewGame(gsb.getGameSetupType());
		}
	}
}

class AttributesJPanel extends JPanel {
	private final JCheckBox cbRandomPlayerOrder = new JCheckBox("Player Order: Random");
	
	public AttributesJPanel() {
		super(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 1;

		add(cbRandomPlayerOrder, gbc);
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