package ca.jhayden.whim.ataxx.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import ca.jhayden.whim.ataxx.engine.GameSetupType;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;

public class GameOverDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 182167897708030017L;

	static String computeGameOverMessage(AtaxxState state) {
		return computeGameOverMessage(state.computeScores(), state.players());
	}

	static String computeGameOverMessage(final Scores score, final List<Player> players) {
		int bestScore = 0;
		final List<String> bestPlayers = new ArrayList<>();
		System.out.println(score);

		for (Player p : players) {
			final Tile playerTile = p.tile();
			final int playerScore = score.count(p.tile());

			if (playerScore > bestScore) {
				bestPlayers.clear();
				bestScore = playerScore;
			}

			if (bestScore == playerScore) {
				bestPlayers.add(AtaxxJFrame.COLOR_NAME.get(playerTile));
			}

			System.out.println(bestPlayers);
			System.out.println("");
		}

		return "Winners are " + bestPlayers;
	}

	private final AtaxxJFrame gameHub;
	private final AtaxxSetupGameJPanel setupPanel;
	private final JButton bQuit = new JButton("Exit");
	private final String message;

	public GameOverDialog(AtaxxJFrame owner, AtaxxState state) {
		super(owner, true);
		message = computeGameOverMessage(state);
		gameHub = owner;
		setupPanel = new AtaxxSetupGameJPanel(owner, this);

		this.setLayout(new BorderLayout());
		this.add(new JLabel(message), BorderLayout.NORTH);
		this.add(setupPanel, BorderLayout.CENTER);
		this.add(bQuit, BorderLayout.SOUTH);

		bQuit.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e);
		if (e.getSource() instanceof GameSetupJButton gsb) {
			GameSetupType gst = gsb.getGameSetupType();
			gameHub.startNewGame(gst);
			this.dispose();
		}
		else {
			System.exit(0);
		}
	}
}
