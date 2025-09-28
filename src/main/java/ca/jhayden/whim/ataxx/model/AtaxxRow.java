package ca.jhayden.whim.ataxx.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class AtaxxRow implements Iterable<Tile> {
	public static final List<AtaxxRow> EMPTY_LIST = Collections.emptyList();

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
	private final Scores score;

	private AtaxxRow(Tile[] rowTiles) {
		super();
		tiles = Objects.requireNonNull(rowTiles, "AtaxxRow(rowTiles)");
		score = Scores.of(rowTiles);
	}

	public Scores getScore() {
		return score;
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

	@Override
	public Iterator<Tile> iterator() {
		return new MyIterator(tiles);
	}
}

class MyIterator implements Iterator<Tile> {
	private final Tile[] tiles;
	private int index = 0;

	MyIterator(Tile[] tiles) {
		super();
		this.tiles = tiles;
	}

	@Override
	public boolean hasNext() {
		return index < tiles.length;
	}

	@Override
	public Tile next() {
		if (index >= tiles.length) {
			throw new NoSuchElementException(index + " >= " + tiles.length);
		}
		return tiles[index++];
	}

}