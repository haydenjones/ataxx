package ca.jhayden.whim.ataxx.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import ca.jhayden.whim.ataxx.model.AtaxxBoard;
import ca.jhayden.whim.ataxx.model.AtaxxRow;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.MoveType;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Pos;
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
        return new AtaxxState(players, board);
    }

    public static boolean isValidMove(AtaxxState state, GameMove move)  {
        if (move == GameMove.PASS)  {
            return true;
        };

        Tile tile = state.board().at(move.start());
        if (tile != state.currentPlayer().tile())  {
            return false;
        }

        Pos end = move.start().translate(move.move());
        Tile endTile = state.board().at(end);
        return (endTile == Tile.EMPTY);
    }

    public static AtaxxState applyMove(AtaxxState state, GameMove move) {
        // Advance the player
        int index = state.players().indexOf(state.currentPlayer());
        int nextPlayerIndex = (index + 1) % state.players().size();
        final Player nextPlayer = state.players().get(nextPlayerIndex);

        // Check that the move is valid
        if (!isValidMove(state, move)) {
            throw new IllegalArgumentException(move + " NOT allowed.");
        };

        // Update the Board
        if (move != GameMove.PASS)  {
            final TreeMap<Pos, Tile> changedTiles = new TreeMap<>();
            if (move.move().isJump())  {
                changedTiles.put(move.start(), Tile.EMPTY);
            }
            final Tile playerTile = state.currentPlayer().tile();
            final Pos endPos = move.start().translate(move.move());
            changedTiles.put(endPos, playerTile);

            for (MoveType mt : MoveType.values())  {
                if (!mt.isJump())  {
                    Pos pos = endPos.translate(mt);
                    Tile tile = state.board().at(pos);
                    if ((tile != playerTile) && (tile.isPiece()))  {
                        changedTiles.put(pos, playerTile);
                    }
                }
            }

            // Now we Update the board
            List<AtaxxRow> newRows = new ArrayList<>();
            for (AtaxxRow row : state.board().rows())  {
                row = row.with(changedTiles, newRows.size());
                newRows.add(row);
            }
        }

        return new AtaxxState(state.players(), state.board(), nextPlayer);
    }
}
