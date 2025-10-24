package ca.jhayden.whim.ataxx.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ca.jhayden.whim.ataxx.model.AnimateInfo;
import ca.jhayden.whim.ataxx.model.AnimateInfoType;
import ca.jhayden.whim.ataxx.model.AtaxxBoard;
import ca.jhayden.whim.ataxx.model.AtaxxChangeInfo;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.ChangeType;
import ca.jhayden.whim.ataxx.model.FromToPos;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.MoveType;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Pos;
import ca.jhayden.whim.ataxx.model.Tile;

public record ApplyMoveTask(AtaxxState state, GameMove move, List<AnimateInfo> animationsDone) {
	public AtaxxChangeInfo call() {
		// Check that the move is valid
		if (!GameEngine.isValidMove(state, move)) {
			throw new IllegalArgumentException(move + " NOT allowed.");
		}

		// Advance the player
		int index = state.players().indexOf(state.currentPlayer());
		int nextPlayerIndex = (index + 1) % state.players().size();
		final Player nextPlayer = state.players().get(nextPlayerIndex);

		AtaxxBoard board = state.board();

		// Update the Board
		if (move != GameMove.PASS) {
			final Pos endPos = move.start().translate(move.move());
			AtaxxBoard afterTheMove = moveThePrimaryPiece(endPos);
			board = flipTheNeighbours(afterTheMove, endPos);
		}

		var newState = new AtaxxState(state.players(), board, nextPlayer);
		var changeType = GameEngine.isGameOver(newState) ? ChangeType.GAME_OVER : ChangeType.GAME_MOVE;
		return new AtaxxChangeInfo(changeType, newState);
	}

	void addAnimation(AnimateInfoType type, AtaxxBoard base, Tile tile, FromToPos positions) {
		if (animationsDone == null) {
			return;
		}
		addAnimation(type, base, tile, List.of(positions));
	}

	void addAnimation(AnimateInfoType type, AtaxxBoard base, Tile tile, List<FromToPos> positions) {
		if (animationsDone == null) {
			return;
		}
		else if (positions.isEmpty()) {
			return;
		}
		AnimateInfo ai = new AnimateInfo(base, type, tile, positions);
		animationsDone.add(ai);
	}

	AtaxxBoard moveThePrimaryPiece(Pos endPos) {
		Map<Pos, Tile> changedTiles = new TreeMap<>();

		if (move.move().isJump()) {
			changedTiles.put(move.start(), Tile.EMPTY);
			AtaxxBoard base = this.state().board().with(changedTiles);

			addAnimation(AnimateInfoType.JUMP, base, move.tile(), new FromToPos(move.start(), endPos));
		}
		else {
			addAnimation(AnimateInfoType.GROW, state.board(), move.tile(), new FromToPos(move.start(), endPos));
		}

		changedTiles.put(endPos, move.tile());

		return state.board().with(changedTiles);
	}

	AtaxxBoard flipTheNeighbours(AtaxxBoard board, Pos endPos) {
		Map<Pos, Tile> changedTiles = new TreeMap<>();
		var positions = new ArrayList<FromToPos>();

		for (MoveType mt : MoveType.values()) {
			if (!mt.isJump()) {
				Pos newPos = endPos.translate(mt);
				Tile newTile = board.at(newPos);
				if (newTile != move.tile()) {
					if (newTile.isPiece()) {
						changedTiles.put(newPos, move.tile());
						positions.add(new FromToPos(endPos, newPos));
					}
				}
			}
		}

		addAnimation(AnimateInfoType.FLIP, board, this.move.tile(), positions);
		return board.with(changedTiles);
	}
}
