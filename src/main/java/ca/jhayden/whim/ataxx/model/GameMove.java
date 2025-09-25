package ca.jhayden.whim.ataxx.model;

public record GameMove(Pos start, MoveType move) implements Comparable<GameMove> {
	public static GameMove PASS = new GameMove(new Pos(0, 0), MoveType.UULL);

	@Override
	public int compareTo(GameMove that) {
		int c = this.start.compareTo(that.start);
		if (c != 0) {
			return c;
		}
		return this.move.compareTo(that.move);
	}
}