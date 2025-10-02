package ca.jhayden.whim.ataxx.ai;

import java.util.SortedSet;

import ca.jhayden.whim.ataxx.engine.GameEngine;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;

public class ComputeAiMove {
	private final AtaxxState state;
	private int depth = 2;
	private boolean choiceRandom = false;

	public ComputeAiMove(AtaxxState state) {
		this(state, 2, true);
	}

	public ComputeAiMove(AtaxxState state, int depth, boolean chooseRandomly) {
		super();
		this.state = state;
		this.depth = depth;
		this.choiceRandom = chooseRandomly;
	}

	void growGameTree(final GameTreeBuilder root, final int depth) {
		if (depth == 0) {
			return;
		}

		final SortedSet<GameMove> moves = GameEngine.computeAllMoves(root.getState());
		for (GameMove gm : moves) {
			GameTreeBuilder b = root.with(gm);
			growGameTree(b, depth - 1);
		}
	}

	public GameMove call() {
		final GameTreeBuilder builder = new GameTreeBuilder(this.state);
		growGameTree(builder, this.depth);

		GameTree tree = GameTree.of(builder);
		return tree.findBestMove(choiceRandom);
	}

}
