package ca.jhayden.whim.ataxx.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import ca.jhayden.whim.ataxx.model.AnimateInfo;
import ca.jhayden.whim.ataxx.model.AnimateInfoType;
import ca.jhayden.whim.ataxx.model.AtaxxBoard;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.FromToPos;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.MoveType;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Pos;
import ca.jhayden.whim.ataxx.model.Tile;

public class ApplyMoveTask {
	private final AtaxxState beginState;
	private final GameMove move;

	public ApplyMoveTask(AtaxxState beginState, GameMove move) {
		super();
		this.beginState = Objects.requireNonNull(beginState);
		this.move = Objects.requireNonNull(move);

	}

	public List<AnimateInfo> computeWithAnimations() {
		final List<AnimateInfo> animations = new ArrayList<>();
		doMove(animations);
		return animations;
	}

	public AtaxxState computeJustState() {
		return doMove(null);
	}

	AtaxxState doMove(List<AnimateInfo> animations) {
		// Check that the move is valid
		if (!GameEngine.isValidMove(beginState, move)) {
			throw new IllegalArgumentException(move + " NOT allowed.");
		}

		AtaxxBoard board = beginState.board();

		// Update the Board
		if (move != GameMove.PASS) {
			final Pos endPos = move.start().translate(move.move());
			AtaxxBoard afterTheMove = moveThePrimaryPiece(endPos, animations);
			board = flipTheNeighbours(afterTheMove, endPos, animations);
		}

		// Advance the player
		int index = beginState.players().indexOf(beginState.currentPlayer());
		int nextPlayerIndex = (index + 1) % beginState.players().size();
		final Player nextPlayer = beginState.players().get(nextPlayerIndex);

		final AtaxxState endState = new AtaxxState(beginState.players(), board, nextPlayer);
		if (animations != null) {
			AnimateInfo ai = new AnimateInfo(endState, AnimateInfoType.TURN_IS_DONE, nextPlayer.tile(),
					FromToPos.EMPTY_LIST);
			animations.add(ai);
		}
		return endState;
	}

	void addAnimation(List<AnimateInfo> animations, AnimateInfoType type, AtaxxBoard base, Tile tile,
			FromToPos positions) {
		if (animations == null) {
			return;
		}

		AtaxxState baseState = new AtaxxState(beginState.players(), base, beginState.currentPlayer());
		AnimateInfo ai = new AnimateInfo(baseState, type, tile, List.of(positions));
		animations.add(ai);
	}

	void addFlipAnimation(List<AnimateInfo> animations, AtaxxBoard base, Tile tile, List<FromToPos> positions) {
		if ((animations == null) || (positions.isEmpty())) {
			return;
		}

		AtaxxState baseState = new AtaxxState(beginState.players(), base, beginState.currentPlayer());
		AnimateInfo ai = new AnimateInfo(baseState, AnimateInfoType.FLIP_SURROUNDING_PIECES, tile, positions);
		animations.add(ai);
	}

	AtaxxBoard moveThePrimaryPiece(Pos endPos, List<AnimateInfo> animations) {
		Map<Pos, Tile> changedTiles = new TreeMap<>();

		if (move.move().isJump()) {
			changedTiles.put(move.start(), Tile.EMPTY);
			AtaxxBoard base = this.beginState.board().with(changedTiles);
			addAnimation(animations, AnimateInfoType.MAIN_PIECE_JUMP, base, beginState.currentPlayer().tile(),
					new FromToPos(move.start(), endPos));
		}
		else {
			addAnimation(animations, AnimateInfoType.MAIN_PIECE_GROW, beginState.board(), move.tile(),
					new FromToPos(move.start(), endPos));
		}

		changedTiles.put(endPos, move.tile());

		return beginState.board().with(changedTiles);
	}

	AtaxxBoard flipTheNeighbours(AtaxxBoard board, Pos endPos, List<AnimateInfo> animations) {
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

		addFlipAnimation(animations, board, this.move.tile(), positions);
		return board.with(changedTiles);
	}
}
