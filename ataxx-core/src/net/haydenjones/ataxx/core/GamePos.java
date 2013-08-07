/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.haydenjones.ataxx.core;

/**
 *
 * @author hjones
 */
public class GamePos implements Comparable<GamePos> {
    public static final GamePos NULL = new GamePos(-1, -1);

    public static GamePos ofRowCol(int row, int col) {
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

    @Override
    public String toString()  {
        if (this == GamePos.NULL)  {
            return "GamePos (NULL)";
        }
        return String.format("GamePos (row: %s, col:%s)", row,col);
    }
    
    @Override
    public int hashCode()  {
        return (19 * row) + (17 * col) + 13;
    }
    
    @Override
    public int compareTo(GamePos gp)  {
        int c = gp.row - row;
        if (c != 0)  {
            return c;
        }
        
        return gp.col - col;
    }
    
    @Override
    public boolean equals(Object o)  {
        if (!(o instanceof GamePos))  {
            return false;
        }

        GamePos gp = (GamePos) o;
        return (compareTo(gp) == 0);
    }
}
