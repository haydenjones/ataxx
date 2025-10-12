package ca.jhayden.whim.ataxx.model;

public record Scores(int piece1, int piece2, int piece3, int piece4) {

	public static Scores of(Tile[] rowTiles) {
		int p1 = 0;
		int p2 = 0;
		int p3 = 0;
		int p4 = 0;

		for (Tile t : rowTiles) {
			if (t == Tile.PIECE_1) {
				p1++;
			}
			else if (t == Tile.PIECE_2) {
				p2++;
			}
			else if (t == Tile.PIECE_3) {
				p3++;
			}
			else if (t == Tile.PIECE_4) {
				p4++;
			}
		}

		return new Scores(p1, p2, p3, p4);
	}

	public int count(Tile tile) {
		if (Tile.PIECE_1 == tile) {
			return piece1();
		}
		if (Tile.PIECE_2 == tile) {
			return piece2();
		}
		if (Tile.PIECE_3 == tile) {
			return piece3();
		}
		if (Tile.PIECE_4 == tile) {
			return piece4();
		}
		return 0;
	}

	public Scores with(Scores that) {
		return new Scores(this.piece1 + that.piece1, this.piece2 + that.piece2, this.piece3 + that.piece3,
				this.piece4 + that.piece4);
	}
}
