package ca.jhayden.whim.ataxx.engine;

import org.junit.jupiter.api.Test;

import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.MoveType;
import ca.jhayden.whim.ataxx.model.Pos;

public class GameMoveTest {
	public static void main(String[] args) {
		AtaxxState state = GameEngine.newGame();
		System.out.println(state.board());

		GameMove move = new GameMove(new Pos(0, 0), MoveType.DR);
		System.out.println(move);

		state = GameEngine.applyMove(state, move);
		System.out.println(state);
	}

	@Test
	public void works() {
		AtaxxState state = GameEngine.newGame();
		System.out.println(state.board());

		GameMove move = new GameMove(new Pos(0, 0), MoveType.DL);
		System.out.println(move);

		state = GameEngine.applyMove(state, move);
		System.out.println(state);
	}
}
