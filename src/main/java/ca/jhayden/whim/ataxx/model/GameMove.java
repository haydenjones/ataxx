package ca.jhayden.whim.ataxx.model;

public record GameMove(Pos start, MoveType move) {
    public static GameMove PASS = new GameMove(new Pos(0, 0), MoveType.UULL);
}