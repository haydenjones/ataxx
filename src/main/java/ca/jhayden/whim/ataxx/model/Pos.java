package ca.jhayden.whim.ataxx.model;

public record Pos(int row, int col) implements Comparable<Pos> {

    public Pos translate(MoveType move) {
        return new Pos(this.row + move.rowAdj(), this.col + move.colAdj());
    }

    @Override
    public int compareTo(Pos that) {
        int c = this.row - that.row;
        if (c != 0)  {
            return c;
        }
        return this.col - that.col;
    }    
}
