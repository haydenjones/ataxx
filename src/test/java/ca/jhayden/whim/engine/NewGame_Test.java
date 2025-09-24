package ca.jhayden.whim.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import ca.jhayden.whim.ataxx.engine.GameEngine;
import ca.jhayden.whim.ataxx.model.AtaxxState;

public class NewGame_Test {
    @Test
    public void skeletonCoding() {
        AtaxxState state = GameEngine.newGame();
        assertNotNull(state);

        assertEquals(0, state.currentPlayerIndex(), "current player index");

        assertEquals(2, state.players().size(), "# of Players");
    }
}