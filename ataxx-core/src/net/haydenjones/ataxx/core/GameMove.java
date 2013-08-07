/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.haydenjones.ataxx.core;

/**
 *
 * @author hjones
 */
public class GameMove {
    public static final GameMove NULL = new GameMove(GamePos.NULL, GamePos.NULL);

    private final GamePos from;
    private final GamePos to;
    
    public GameMove(GamePos from, GamePos to)  {
        super();
        this.from = from;
        this.to = to;
    }
    
    public GamePos getFrom()  {
        return from;
    }
    
    public GamePos getTo()  {
        return to;
    }

    @Override
    public int hashCode()  {
        return 13 * from.hashCode() + to.hashCode();
    }
    
    @Override
    public boolean equals(Object o)  {
        if (!(o instanceof GameMove))  {
            return false;
        }
        
        GameMove gm = (GameMove) o;
        int c = from.compareTo(gm.from);
        if (c != 0)  {
            return false;
        }
        
        return (to.compareTo(gm.to) == 0);
    }
}
