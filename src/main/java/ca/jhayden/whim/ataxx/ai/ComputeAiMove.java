package ca.jhayden.whim.ataxx.ai;

import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;

public class ComputeAiMove {
	private final AtaxxState state;

	public ComputeAiMove(AtaxxState state) {
		super();
		this.state = state;
	}

	public GameMove call() {
		GameTree tree = new GameTree(state, 2);
		return tree.findBestMove(state.currentPlayer().tile());
	}

}
