package ca.jhayden.whim.ataxx.swing;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.jhayden.whim.ataxx.model.AtaxxChangeInfo;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;
import ca.jhayden.whim.ataxx.ui.DoAfterAnimation;

public class ScoreSingleJPanel extends JPanel implements DoAfterAnimation {

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
	public void doAfterAnimations(AtaxxChangeInfo changeInfo) {
		final Tile activePlayerTile = changeInfo.endState().currentPlayer().tile();
		final Scores score = changeInfo.endState().computeScores();
		final String star = (activePlayerTile == player.tile()) ? " * " : "";
		String start = star + player.name() + ": " + player.tile().getMyScore(score) + star;
		label.setText(start.trim());
	}
}
