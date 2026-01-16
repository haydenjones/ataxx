package ca.jhayden.whim.ataxx.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;
import ca.jhayden.whim.ataxx.ui.GameHub;

public class GameOverPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 8194052182429999079L;

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

	private final AtaxxState state;
	private final GameHub hub;
	private final AtaxxSetupGameJPanel setup;
	private final String message;

	private final JButton bQuit = new JButton("Quit");

	public GameOverPanel(AtaxxState state, GameHub hub) {
		super(new BorderLayout());
		this.state = state;
		this.hub = hub;
		this.message = computeGameOverMessage(state);

		this.setup = new AtaxxSetupGameJPanel(hub, this);

		this.add(new JLabel(message), BorderLayout.NORTH);
		this.add(setup, BorderLayout.CENTER);
		this.add(bQuit, BorderLayout.SOUTH);

		bQuit.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bQuit) {
			System.exit(0);
		}
		else if (e.getSource() instanceof GameSetupJButton gsb) {
			hub.startNewGame(gsb.getGameSetupType());
			this.setVisible(false);
		}
	}
}
