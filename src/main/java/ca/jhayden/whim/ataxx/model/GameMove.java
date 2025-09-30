package ca.jhayden.whim.ataxx.model;

public record GameMove(Tile tile, Pos start, MoveType move) implements Comparable<GameMove> {
	public static GameMove PASS = new GameMove(null, null, null);

	public static GameMove of(Tile tile, Pos startPos, Pos endPos) {
		for (MoveType mt : MoveType.values()) {
			Pos pos = startPos.translate(mt);
			if (pos.equals(endPos)) {
				return new GameMove(tile, startPos, mt);
			}
		}
		return null;
	}

	@Override
	public int compareTo(GameMove that) {
		int c = this.start.compareTo(that.start);
		if (c != 0) {
			return c;
		}
		return this.move.compareTo(that.move);
	}
}