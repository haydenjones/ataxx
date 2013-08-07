/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.haydenjones.ataxx.core;

/**
 *
 * @author hjones
 */
public class GameInfo {
    private final GameState state;
    private final byte[] playerOrder;
    
    public GameInfo(GameState state, byte[] playerOrder)  {
        super();
        this.state = state;
        this.playerOrder = playerOrder;
    }

    public GameState getState() {
        return state;
    }
    
    public byte[] getPlayerOrdering()  {
        return playerOrder;
    }
}
