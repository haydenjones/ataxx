package ca.jhayden.whim.ataxx.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record AtaxxBoard(List<AtaxxRow> rows) {
	public static final AtaxxBoard EMPTY_BOARD = new AtaxxBoard(AtaxxRow.EMPTY_LIST);

	public static AtaxxBoard of(String board) {
		final var rows = board.lines().map(AtaxxRow::of).toList();
		return new AtaxxBoard(rows);
	}

	public AtaxxBoard(List<AtaxxRow> rows) {
		this.rows = Collections.unmodifiableList(new ArrayList<>(rows));
	}

	public Tile at(Pos start) {
		int index = start.row();
		if (index < 0) {
			return Tile.WALL;
		}
		else if (index >= rows.size()) {
			return Tile.WALL;
		}

		return rows.get(index).at(start.col());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (AtaxxRow ar : rows) {
			sb.append(System.lineSeparator());
			sb.append(ar.toString());
		}
		return sb.toString();
	}
}
