package ca.jhayden.whim.ataxx.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;

public class ScoreJPanel extends JPanel {
	private static final long serialVersionUID = 8710836157609000120L;

	private final JLabel label1 = new JLabel("");
	private final JLabel label2 = new JLabel("");
	private final JLabel label3 = new JLabel("");
	private final JLabel label4 = new JLabel("");

	public ScoreJPanel() {
		super(new GridBagLayout());

		label1.setForeground(AtaxxJFrame.COLOR_MAP.get(Tile.PIECE_1));
		label2.setForeground(AtaxxJFrame.COLOR_MAP.get(Tile.PIECE_2));
		label3.setForeground(AtaxxJFrame.COLOR_MAP.get(Tile.PIECE_3));
		label4.setForeground(AtaxxJFrame.COLOR_MAP.get(Tile.PIECE_4));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.ipadx = 10;
		gbc.ipady = 5;
		gbc.weightx = 1;
		gbc.weighty = 1;

		gbc.gridx = 0;
		this.add(label1, gbc);
		gbc.gridx = 1;
		this.add(label2, gbc);
		gbc.gridx = 2;
		this.add(label3, gbc);
		gbc.gridx = 3;
		this.add(label4, gbc);
	}

	void setLabel(JLabel label, int score, boolean currentPlayer) {
		label.setText((currentPlayer ? "* " : "") + score);
	}

	public void update(AtaxxState state, boolean gameOver) {
		Scores score = state.computeScores();

		setLabel(label1, score.piece1(), Tile.PIECE_1 == state.currentPlayer().tile());
		setLabel(label2, score.piece2(), Tile.PIECE_2 == state.currentPlayer().tile());
		setLabel(label3, score.piece3(), Tile.PIECE_3 == state.currentPlayer().tile());
		setLabel(label4, score.piece4(), Tile.PIECE_4 == state.currentPlayer().tile());
	}

}
