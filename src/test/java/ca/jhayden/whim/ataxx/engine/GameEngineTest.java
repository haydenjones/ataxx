package ca.jhayden.whim.ataxx.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.Tile;

public class GameEngineTest {
	@Test
	public void newGame_initialState() {
		AtaxxState state = GameEngine.newGame(GameSetupType.TWO_PLAYER_SIMPLE).endState();

		assertNotNull(state);
		assertEquals(state.players().getFirst(), state.currentPlayer());
		assertEquals(2, state.players().size());
		assertNotNull(state.board());
		assertEquals(7, state.board().rows().size());
	}

	@Test
	public void newGame_playerConfiguration() {
		AtaxxState state = GameEngine.newGame(GameSetupType.TWO_PLAYER_SIMPLE).endState();

		assertEquals(Tile.PIECE_1, state.players().get(0).tile());
		assertTrue(state.players().get(0).isHuman());

		assertEquals(Tile.PIECE_2, state.players().get(1).tile());
		assertFalse(state.players().get(1).isHuman());
	}
}