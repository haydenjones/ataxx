package ca.jhayden.whim.ataxx.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;

import ca.jhayden.whim.ataxx.engine.GameEngine;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;

public class GameTree {
	private static final int WORST_POSSIBLE_SCORE = -99;

	private static final List<GameTree> EMPTY_LIST = Collections.emptyList();

	private final GameMove move;
	private final AtaxxState state;
	private final List<GameTree> children;

	public GameTree(AtaxxState state, int depth) {
		this.move = null;
		this.state = state;
		this.children = new ArrayList<>();

		if (depth > 0) {
			SortedSet<GameMove> moves = GameEngine.computeAllMoves(state);
			for (GameMove gm : moves) {
				children.add(new GameTree(gm, GameEngine.applyMove(state, gm), depth - 1));
			}
		}
	}

	public GameTree(GameMove myMove, AtaxxState state, int depth) {
		super();
		this.move = myMove;
		this.state = state;
		this.children = (depth < 1) ? GameTree.EMPTY_LIST : new ArrayList<>();

		if (depth > 0) {
			SortedSet<GameMove> moves = GameEngine.computeAllMoves(state);
			for (GameMove gm : moves) {
				children.add(new GameTree(gm, GameEngine.applyMove(state, gm), depth - 1));
			}
		}
	}

	// In general the algorithm is like...
	// If I have no kids then
	// Look at each of my kids and select the move that the current player would
	// make.
	int findBestScore() {
		if (this.children.isEmpty()) {
			final Tile playerTile = this.state.currentPlayer().tile();
			Scores s = this.state.computeScores();
			int score = 0;
			score = score + ((playerTile == Tile.PIECE_1) ? s.piece1() : -s.piece1());
			score = score + ((playerTile == Tile.PIECE_2) ? s.piece2() : -s.piece2());
			score = score + ((playerTile == Tile.PIECE_3) ? s.piece3() : -s.piece3());
			score = score + ((playerTile == Tile.PIECE_4) ? s.piece4() : -s.piece4());
			return score;
		}

		int bestScore = WORST_POSSIBLE_SCORE;
		for (GameTree child : this.children) {
			bestScore = Math.max(bestScore, child.findBestScore());
		}

		return bestScore;
	}

	public GameMove findBestMove(Tile tile) {
		final int size = children.size();
		if (size == 0) {
			return GameMove.PASS;
		}

		List<GameMove> bestMoves = new ArrayList<>();
		int bestScore = WORST_POSSIBLE_SCORE;

		for (GameTree tree : children) {
			int best = tree.findBestScore();
			if (best > bestScore) {
				bestScore = best;
				bestMoves.clear();
			}

			if (best == bestScore) {
				bestMoves.add(tree.move);
			}
		}

		Random r = new Random();
		int choose = r.nextInt(bestMoves.size());
		return bestMoves.get(choose);
	}
}
