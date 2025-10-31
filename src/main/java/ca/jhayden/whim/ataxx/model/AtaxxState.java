package ca.jhayden.whim.ataxx.model;

import java.util.List;

public record AtaxxState(List<Player> players, AtaxxBoard board, Player currentPlayer) {
	public static final AtaxxState NULL = new AtaxxState(Player.EMPTY_LIST, AtaxxBoard.EMPTY_BOARD, null);

	public AtaxxState(List<Player> players, AtaxxBoard board) {
		this(players, board, players.getFirst());
	}

	public Scores computeScores() {
		return board.computeScores();
	}
}
