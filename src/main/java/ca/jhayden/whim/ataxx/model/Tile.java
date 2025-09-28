package ca.jhayden.whim.ataxx.model;

public enum Tile {
	EMPTY('.'), //
	WALL('#'), //
	PIECE_1('1'), //
	PIECE_2('2'), //
	PIECE_3('3'), //
	PIECE_4('4');

	public static Tile fromChar(final char ch) {
		for (Tile tile : Tile.values()) {
			if (ch == tile.charValue) {
				return tile;
			}
		}
		throw new IllegalArgumentException(String.format("Unrecognized Tile character: '%s'", ch));
	}

	private final char charValue;

	Tile(char charValue) {
		this.charValue = charValue;
	}

	public final char charValue() {
		return this.charValue;
	}

	public boolean isPiece() {
		return (this != Tile.WALL) && (this != Tile.EMPTY);
	}
}
