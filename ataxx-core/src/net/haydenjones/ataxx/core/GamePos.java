/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.haydenjones.ataxx.core;

/**
 *
 * @author hjones
 */
public class GamePos {
    public static final GamePos NULL = new GamePos(-1, -1);

    static GamePos ofRowCol(int row, int col) {
        return new GamePos(row, col);
    }
    
    private final int row;
    private final int col;
    
    private GamePos(int r, int c)  {
        super();
        row = r;
        col = c;
    }
    
    public int getRow()  {
        return row;
    }
    
    public int getCol()  {
        return col;
    }
}
