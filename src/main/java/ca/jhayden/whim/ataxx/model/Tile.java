package ca.jhayden.whim.ataxx.model;

public enum Tile {
	EMPTY('.') {
		@Override
		public int getMyScore(Scores score) {
			return 0;
		}
	}, //
	WALL('#') {
		@Override
		public int getMyScore(Scores score) {
			return 0;
		}
	}, //
	PIECE_1('1') {
		@Override
		public int getMyScore(Scores score) {
			return score.piece1();
		}
	}, //
	PIECE_2('2') {
		@Override
		public int getMyScore(Scores score) {
			return score.piece2();
		}
	}, //
	PIECE_3('3') {
		@Override
		public int getMyScore(Scores score) {
			return score.piece3();
		}
	}, //
	PIECE_4('4') {
		@Override
		public int getMyScore(Scores score) {
			return score.piece4();
		}
	};

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

	public abstract int getMyScore(Scores score);
}
