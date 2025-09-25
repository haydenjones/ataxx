package ca.jhayden.whim.ataxx.model;

public enum MoveType {
    UULL(-2, -2), UUL(-2, -1), UU(-2, 0), UUR(-2, 1), UURR(-2, 2), //
    ULL(-1, -2), UL(-1, -1), U(-1, 0), UR(-1, 1), URR(-1, 2), //
    LL(0, -2), L(0, -1), R(0, 1), RR(0, 2), //
    DLL(1, -2), DL(1, -1), D(1, 0), DR(1, 1), DRR(1, 2), //
    DDLL(2, -2), DDL(2, -1), DD(2, 0), DDR(2, 1), DDRR(2, 2);

    private final int rowAdj;
    private final int colAdj;
    private final boolean jump;

    MoveType(int ra, int ca)  {
        rowAdj = ra;
        colAdj = ca;
        jump = (ra == -2) || (ra == 2) || (ca == -2) || (ca == 2);
    }

    public boolean isJump() {
        return jump;
    }

    public int rowAdj() {
        return rowAdj;
    }

    public int colAdj() {
        return colAdj;
    }
}
