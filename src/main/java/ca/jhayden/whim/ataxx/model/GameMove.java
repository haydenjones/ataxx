package ca.jhayden.whim.ataxx.model;

public record GameMove(Pos start, MoveType move) implements Comparable<GameMove> {
	public static GameMove PASS = new GameMove(new Pos(0, 0), MoveType.UULL);

	public static GameMove of(Pos startPos, Pos endPos) {
		for (MoveType mt : MoveType.values()) {
			Pos pos = startPos.translate(mt);
			if (pos.equals(endPos)) {
				return new GameMove(startPos, mt);
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