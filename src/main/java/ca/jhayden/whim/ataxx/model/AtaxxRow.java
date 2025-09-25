package ca.jhayden.whim.ataxx.model;

import java.util.Map;
import java.util.Objects;

public final class AtaxxRow {
	public static AtaxxRow of(String row) {
		Objects.requireNonNull(row, "AtaxxRow.of(row)");
		row = row.trim();
		Tile[] tiles = new Tile[row.length()];
		for (int i1 = 0; i1 < tiles.length; i1++) {
			tiles[i1] = Tile.fromChar(row.charAt(i1));
		}
		return new AtaxxRow(tiles);
	}

	private final Tile[] tiles;

	private AtaxxRow(Tile[] rowTiles) {
		super();
		tiles = Objects.requireNonNull(rowTiles, "AtaxxRow(rowTiles)");
	}

	public int length() {
		return tiles.length;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Tile t : tiles) {
			sb.append(t.charValue());
		}
		return sb.toString();
	}

	public Tile at(int col) {
		if (col < 0) {
			return Tile.WALL;
		}
		else if (col >= tiles.length) {
			return Tile.WALL;
		}
		return tiles[col];
	}

	public AtaxxRow with(Map<Pos, Tile> changedTiles, int onlyThisRow) {
		Tile[] newTiles = null;

		for (Map.Entry<Pos, Tile> entry : changedTiles.entrySet()) {
			if (entry.getKey().row() == onlyThisRow) {
				if (newTiles == null) {
					newTiles = new Tile[this.tiles.length];
					for (int i1 = 0; i1 < tiles.length; i1++) {
						newTiles[i1] = this.tiles[i1];
					}
				}

				newTiles[entry.getKey().col()] = entry.getValue();
			}
		}

		if (newTiles != null) {
			return new AtaxxRow(newTiles);
		}
		return this;
	}
}
