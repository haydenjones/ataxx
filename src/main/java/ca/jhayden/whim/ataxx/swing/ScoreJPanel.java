package ca.jhayden.whim.ataxx.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;

public class ScoreJPanel extends JPanel {
	private static final long serialVersionUID = 8710836157609000120L;

	private final ScoreSingleJPanel label1 = new ScoreSingleJPanel(Tile.PIECE_1);
	private final ScoreSingleJPanel label2 = new ScoreSingleJPanel(Tile.PIECE_2);
	private final ScoreSingleJPanel label3 = new ScoreSingleJPanel(Tile.PIECE_3);
	private final ScoreSingleJPanel label4 = new ScoreSingleJPanel(Tile.PIECE_4);

	public ScoreJPanel() {
		super(new GridBagLayout());
	}

	public void update(AtaxxState state, boolean gameOver) {
		Scores score = state.computeScores();
		label1.update(score, state.currentPlayer().tile());
		label2.update(score, state.currentPlayer().tile());
		label3.update(score, state.currentPlayer().tile());
		label4.update(score, state.currentPlayer().tile());
	}

	public void startNewGame(int numberOfPlayers) {
		final var gbc = new GridBagConstraints();
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

		if (numberOfPlayers > 2) {
			gbc.gridx = 2;
			this.add(label3, gbc);
			gbc.gridx = 3;
			this.add(label4, gbc);
		}
	}
}
