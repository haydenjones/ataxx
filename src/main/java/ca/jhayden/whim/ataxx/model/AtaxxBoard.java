package ca.jhayden.whim.ataxx.model;

import java.util.Collections;
import java.util.List;

public record AtaxxBoard(List<AtaxxRow> rows) {
    public static AtaxxBoard of(String board)  {
        final var rows = board.lines().map(AtaxxRow::of).toList();
        return new AtaxxBoard(Collections.unmodifiableList(rows));
    }

    public Tile at(Pos start) {
        int index = start.row();
        if (index < 0)  {
            return Tile.WALL;
        }
        else if (index >= rows.size())  {
            return Tile.WALL;
        }

        return rows.get(index).at(start.col());
    }    
}
