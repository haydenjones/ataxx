package ca.jhayden.whim.ataxx.engine;

import org.junit.jupiter.api.Test;

import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.MoveType;
import ca.jhayden.whim.ataxx.model.Pos;

public class GameMoveTest {
	@Test
	public void works() {
		AtaxxState state = GameEngine.newGame();
		GameMove move = new GameMove(new Pos(0, 0), MoveType.DR);
		state = GameEngine.applyMove(state, move);
	}
}
