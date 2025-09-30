package ca.jhayden.whim.ataxx.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import ca.jhayden.whim.ataxx.model.AtaxxBoard;
import ca.jhayden.whim.ataxx.model.AtaxxRow;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.MoveType;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Pos;
import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;

public class GameEngine {
	public static AtaxxState newGame(int numberOfPlayers) {
		final List<Player> players;
		final AtaxxBoard board;

		if (numberOfPlayers == 2) {
			players = Collections.unmodifiableList(List.of(new Player(Tile.PIECE_1, true), //
					new Player(Tile.PIECE_2, false) //
			));
			board = AtaxxBoard.of("""
					1.....2
					.......
					.......
					.#.#..#
					.......
					.......
					2.....1""");
		}
		else if (numberOfPlayers == 4) {
			players = Collections.unmodifiableList(List.of(new Player(Tile.PIECE_1, true), //
					new Player(Tile.PIECE_2, false), //
					new Player(Tile.PIECE_3, false), //
					new Player(Tile.PIECE_4, false) //
			));
			board = AtaxxBoard.of("""
					1.....2
					.......
					.......
					...#...
					.......
					.......
					3.....4""");
		}
		else {
			throw new IllegalArgumentException("No support for " + numberOfPlayers + " player(s).");
		}

		return new AtaxxState(players, board);
	}

	public static boolean isGameOver(AtaxxState state) {
		final Scores s = state.computeScores();

		int activePlayers = 0;
		if (s.piece1() > 0) {
			activePlayers++;
		}
		if (s.piece2() > 0) {
			activePlayers++;
		}
		if (s.piece3() > 0) {
			activePlayers++;
		}
		if (s.piece4() > 0) {
			activePlayers++;
		}

		if (activePlayers == 1) {
			return true;
		}

		int rowPos = 0;
		for (AtaxxRow row : state.board().rows()) {
			int colPos = 0;
			for (Tile tile : row) {
				if (tile.isPiece()) {
					Pos startPos = new Pos(rowPos, colPos);

					for (MoveType mt : MoveType.values()) {
						Pos endPos = startPos.translate(mt);

						Tile endTile = state.board().at(endPos);
						if (endTile == Tile.EMPTY) {
							return false;
						}
					}
				}

				colPos++;
			}

			rowPos++;
		}

		return true;
	}

	public static boolean isValidMove(AtaxxState state, GameMove move) {
		if (move == GameMove.PASS) {
			return true;
		}

		Tile tile = state.board().at(move.start());
		if (tile != state.currentPlayer().tile()) {
			return false;
		}

		Pos end = move.start().translate(move.move());
		Tile endTile = state.board().at(end);
		return (endTile == Tile.EMPTY);
	}

	public static SortedSet<GameMove> computeAllMoves(AtaxxState state) {
		final var allMoves = new HashMap<String, GameMove>();

		final Tile currentPlayer = state.currentPlayer().tile();
		for (int startRow = 0; startRow < state.board().rows().size(); startRow++) {
			final var row = state.board().rows().get(startRow);
			for (int startCol = 0; startCol < row.length(); startCol++) {
				final Pos startPos = new Pos(startRow, startCol);
				Tile tile = state.board().at(startPos);
				if (tile != currentPlayer) {
					continue;
				}

				for (MoveType mt : MoveType.values()) {
					GameMove move = new GameMove(state.currentPlayer().tile(), startPos, mt);
					boolean valid = isValidMove(state, move);
					if (valid) {
						Pos endPos = startPos.translate(move.move());
						final String moveCode = String.format("%s-%s", move.move().isJump() ? startPos : "GROW",
								endPos);
						allMoves.putIfAbsent(moveCode, move);
					}
				}
			}
		}

		final var out = new TreeSet<GameMove>();
		out.addAll(allMoves.values());
		return out;
	}

	public static AtaxxState applyMove(AtaxxState state, GameMove move) {
		// Advance the player
		int index = state.players().indexOf(state.currentPlayer());
		int nextPlayerIndex = (index + 1) % state.players().size();
		final Player nextPlayer = state.players().get(nextPlayerIndex);

		// Check that the move is valid
		if (!isValidMove(state, move)) {
			throw new IllegalArgumentException(move + " NOT allowed.");
		}

		AtaxxBoard board = state.board();

		// Update the Board
		if (move != GameMove.PASS) {
			final TreeMap<Pos, Tile> changedTiles = new TreeMap<>();
			if (move.move().isJump()) {
				changedTiles.put(move.start(), Tile.EMPTY);
			}
			final Tile playerTile = state.currentPlayer().tile();
			final Pos endPos = move.start().translate(move.move());
			changedTiles.put(endPos, playerTile);

			for (MoveType mt : MoveType.values()) {
				if (!mt.isJump()) {
					Pos pos = endPos.translate(mt);
					Tile tile = state.board().at(pos);
					if ((tile != playerTile) && (tile.isPiece())) {
						changedTiles.put(pos, playerTile);
					}
				}
			}

			// Now we Update the board
			List<AtaxxRow> newRows = new ArrayList<>();
			for (AtaxxRow row : state.board().rows()) {
				row = row.with(changedTiles, newRows.size());
				newRows.add(row);
			}

			board = new AtaxxBoard(newRows);
		}

		return new AtaxxState(state.players(), board, nextPlayer);
	}
}
