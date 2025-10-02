package ca.jhayden.whim.ataxx.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;

public class GameTree {
	static int scoreFor(Scores s, Tile playerTile) {
		int score = 0;
		score = score + ((playerTile == Tile.PIECE_1) ? s.piece1() : -s.piece1());
		score = score + ((playerTile == Tile.PIECE_2) ? s.piece2() : -s.piece2());
		score = score + ((playerTile == Tile.PIECE_3) ? s.piece3() : -s.piece3());
		score = score + ((playerTile == Tile.PIECE_4) ? s.piece4() : -s.piece4());
		return score;
	}

	public static GameTree of(GameTreeBuilder builder) {
		return new GameTree(builder);
	}

	private static final int WORST_POSSIBLE_SCORE = -99;

	private static final List<GameTree> EMPTY_LIST = Collections.emptyList();

	private final GameMove move;
	private final AtaxxState state;
	private final List<GameTree> children;
	private final Tile currentPlayerTile;

	private Scores score = null;

	private GameTree(GameTreeBuilder builder) {
		super();
		this.move = builder.getMove();
		this.state = builder.getState();
		this.currentPlayerTile = builder.getState().currentPlayer().tile();
		this.children = builder.getChildren().isEmpty() ? EMPTY_LIST : new ArrayList<>();

		for (GameTreeBuilder child : builder.getChildren()) {
			this.children.add(new GameTree(child));
		}

		if (this.children.isEmpty()) {
			this.score = builder.getState().computeScores();
		}
	}

	void computeScoreTree() {
		if (this.score != null) {
			return;
		}

		// I'm going to need to compute all the scores for my kids.
		for (GameTree gt : this.children) {
			gt.computeScoreTree();
		}

		// Now that my kids all have Scores, I should look for the best one for me.
		int bestScore = WORST_POSSIBLE_SCORE;
		Scores bestChildScore = null;

		for (GameTree gt : this.children) {
			int gtScore = GameTree.scoreFor(gt.score, this.currentPlayerTile);
			if (gtScore > bestScore) {
				bestScore = gtScore;
				bestChildScore = gt.score;
			}
		}

		this.score = bestChildScore;
	}

	public GameMove findBestMove(boolean randomChoice) {
		final int size = children.size();
		if (size == 0) {
			return GameMove.PASS;
		}

		// Calculate Scores for all non-leaf nodes
		computeScoreTree();

		final List<GameMove> bestMoves = new ArrayList<>();
		final int myBestScore = GameTree.scoreFor(this.score, this.state.currentPlayer().tile());

		for (GameTree tree : children) {
			int treeScore = GameTree.scoreFor(tree.score, this.state.currentPlayer().tile());
			if (myBestScore == treeScore) {
				bestMoves.add(tree.move);
			}
		}

		Collections.sort(bestMoves);

		final int choose;
		if (randomChoice) {
			Random r = new Random();
			choose = r.nextInt(bestMoves.size());
		}
		else {
			choose = 0;
		}

		return bestMoves.get(choose);
	}

	@Override
	public String toString() {
		return String.format("""
				GameTree Player: %s, %d children
				%s""", this.currentPlayerTile, this.children.size(), this.score);
	}
}
