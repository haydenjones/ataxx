package ca.jhayden.whim.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import ca.jhayden.whim.ataxx.engine.GameEngine;
import ca.jhayden.whim.ataxx.engine.GameSetupType;
import ca.jhayden.whim.ataxx.model.AtaxxState;

public class NewGame_Test {
	@Test
	public void skeletonCoding() {
		AtaxxState state = GameEngine.newGame(GameSetupType.TWO_PLAYER_SIMPLE);
		assertNotNull(state);
		assertEquals(2, state.players().size(), "# of Players");
	}

	@Test
	public void newGame_boardStructure() {
		AtaxxState state = GameEngine.newGame(GameSetupType.TWO_PLAYER_SIMPLE);
		assertNotNull(state.board());
		assertEquals(7, state.board().rows().size());
	}
}