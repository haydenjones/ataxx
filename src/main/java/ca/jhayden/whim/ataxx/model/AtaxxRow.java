package ca.jhayden.whim.ataxx.model;

import java.util.Objects;

public class AtaxxRow {
    public static AtaxxRow of(String row)  {
        Objects.requireNonNull(row, "AtaxxRow.of(row)");
        row = row.trim();
        Tile[] tiles = new Tile[row.length()];
        for (int i1=0; i1<tiles.length; i1++)  {
            tiles[i1] = Tile.fromChar(row.charAt(i1));
        }
        return new AtaxxRow(tiles);
    }

    private final Tile[] tiles;

    private AtaxxRow(Tile[] rowTiles)  {
        super();
        tiles = Objects.requireNonNull(rowTiles, "AtaxxRow(rowTiles)");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Tile t : tiles)  {
            sb.append(t.charValue());
        }
        return sb.toString();
    }
}
