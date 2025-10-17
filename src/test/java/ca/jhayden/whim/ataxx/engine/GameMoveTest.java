package ca.jhayden.whim.ataxx.engine;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.MoveType;
import ca.jhayden.whim.ataxx.model.Pos;
import ca.jhayden.whim.ataxx.model.Tile;

public class GameMoveTest {
	@Test
	public void works() {
		AtaxxState state = GameEngine.newGame(GameSetupType.TWO_PLAYER_SIMPLE).endState();
		GameMove move = new GameMove(Tile.PIECE_1, new Pos(0, 0), MoveType.DR);
		state = GameEngine.applyMove(state, move, null).endState();
		assertNotNull(state);
	}
}
