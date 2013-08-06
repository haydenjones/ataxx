/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.haydenjones.ataxx.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 * @author hjones
 */
public class GameStateTest {
    void compareGameState(GameState state, byte expectingPlayerID, byte[][] board)  {
        assertEquals("PlayerID", expectingPlayerID, state.getCurrentPlayerID());
        
        assertEquals("# of rows", board.length, state.getBoard().length);
        
        for (int i1=0; i1<board.length; i1++)  {
            assertEquals("row #" + i1, board[i1].length, state.getBoard()[i1].length);
            
            for (int i2=0; i2<board[i1].length; i2++)  {
                assertEquals(i1 + ", " + i2, board[i1][i2], state.getBoard()[i1][i2]);
            }
        }
    }
    
    @Test
    public void testConstructor1() {
        byte[][] expectingBoard = new byte[][] {
            new byte[] { 1, 0, 0, 0, 0, 0, 2 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 2, 0, 0, 0, 0, 0, 1 }
        };
        
        final byte expectingPlayerID = 1;
        GameState gs = new GameState(expectingBoard, expectingPlayerID);
        compareGameState(gs, expectingPlayerID, expectingBoard);
    }
    
    @Test
    public void testConstructor2() {
        byte[][] expectingBoard = new byte[][] {
            new byte[] { 1, 0, 0, 0, 0, 0, 2 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 2, 0, 0, 0, 0, 0, 1 }
        };
        
        final byte expectingPlayerID = 2;
        GameState gs = new GameState(expectingBoard, expectingPlayerID);
        compareGameState(gs, expectingPlayerID, expectingBoard);
    }
}