package ca.jhayden.whim.ataxx.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AtaxxBoardTest {
    @Test
    public void of_validBoard() {
        String boardStr = """
                1.....2
                .......
                .......
                ...#...
                .......
                .......
                2.....1""";
        AtaxxBoard board = AtaxxBoard.of(boardStr);
        assertNotNull(board);
        assertEquals(7, board.rows().size());
    }

    @Test
    public void of_singleRow() {
        AtaxxBoard board = AtaxxBoard.of("1.2");
        assertEquals(1, board.rows().size());
    }

    @Test
    public void of_emptyBoard() {
        AtaxxBoard board = AtaxxBoard.of("");
        assertEquals(0, board.rows().size());
    }
}