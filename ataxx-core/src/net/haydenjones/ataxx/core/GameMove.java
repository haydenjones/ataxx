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
}
