package ca.jhayden.whim.ataxx.swing;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.jhayden.whim.ataxx.model.AnimateInfo;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;
import ca.jhayden.whim.ataxx.ui.DoAnimation;

public class ScoreSingleJPanel extends JPanel implements DoAnimation {

	private static final long serialVersionUID = 2619467135809809125L;

	private final JLabel label = new JLabel("");
	private final Player player;

	public ScoreSingleJPanel(final Player player) {
		super();
		this.player = player;
		label.setForeground(player.color());
		this.add(label);
	}

	@Override
	public void doAnimation(AnimateInfo info) {
		AtaxxState endState = info.state();
		final Tile activePlayerTile = endState.currentPlayer().tile();
		final Scores score = endState.computeScores();
		final String star = (activePlayerTile == player.tile()) ? " * " : "";
		String start = star + player.name() + ": " + player.tile().getMyScore(score) + star;
		label.setText(start.trim());
	}
}
