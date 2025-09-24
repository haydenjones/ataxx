package ca.jhayden.whim.ataxx.engine;

import java.util.Collections;
import java.util.List;

import ca.jhayden.whim.ataxx.model.AtaxxBoard;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Tile;

public class GameEngine {
    public static AtaxxState newGame() {
        List<Player> players = Collections.unmodifiableList(List.of(new Player(Tile.PIECE_1, true), new Player(Tile.PIECE_2, false)));
        AtaxxBoard board = AtaxxBoard.of("""
                1.....2
                .......
                .......
                ...#...
                .......
                .......
                2.....1""");
        return new AtaxxState(players, board, 0);
    }    
}
