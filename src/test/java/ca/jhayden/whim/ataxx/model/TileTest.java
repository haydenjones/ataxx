package ca.jhayden.whim.ataxx.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TileTest {
    @Test
    public void fromChar_validChars() {
        assertEquals(Tile.EMPTY, Tile.fromChar('.'));
        assertEquals(Tile.WALL, Tile.fromChar('#'));
        assertEquals(Tile.PIECE_1, Tile.fromChar('1'));
        assertEquals(Tile.PIECE_2, Tile.fromChar('2'));
    }

    @Test
    public void fromChar_invalidChar() {
        assertThrows(IllegalArgumentException.class, () -> Tile.fromChar('x'));
    }

    @Test
    public void charValue() {
        assertEquals('.', Tile.EMPTY.charValue());
        assertEquals('#', Tile.WALL.charValue());
        assertEquals('1', Tile.PIECE_1.charValue());
        assertEquals('2', Tile.PIECE_2.charValue());
    }
}