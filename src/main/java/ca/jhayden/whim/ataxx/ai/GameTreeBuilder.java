package ca.jhayden.whim.ataxx.ai;

import java.util.ArrayList;
import java.util.List;

import ca.jhayden.whim.ataxx.engine.GameEngine;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;

public class GameTreeBuilder {
	private final AtaxxState state;
	private final GameMove move;
	private final List<GameTreeBuilder> children = new ArrayList<>();

	public GameTreeBuilder(AtaxxState state) {
		this(state, null);
	}

	private GameTreeBuilder(AtaxxState state, GameMove move) {
		super();
		this.state = state;
		this.move = move;
	}

	public GameTreeBuilder with(GameMove move) {
		var newState = GameEngine.applyMove(this.state, move);
		final var out = new GameTreeBuilder(newState, move);
		this.children.add(out);
		return out;
	}

	AtaxxState getState() {
		return state;
	}

	GameMove getMove() {
		return move;
	}

	List<GameTreeBuilder> getChildren() {
		return children;
	}
}
