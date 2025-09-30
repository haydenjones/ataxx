package ca.jhayden.whim.ataxx.swing;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;

public class ScoreSingleJPanel extends JPanel {

	private static final long serialVersionUID = 2619467135809809125L;

	private final JLabel label = new JLabel("");
	private final Tile tile;

	public ScoreSingleJPanel(Tile myTile) {
		super();

		this.tile = myTile;

		label.setForeground(AtaxxJFrame.COLOR_MAP.get(myTile));
		this.add(label);
	}

	public void update(Scores score, Tile activePlayerTile) {
		String start = ((activePlayerTile == tile) ? "* " : "") + this.tile.getMyScore(score);
		label.setText(start);
	}

}
