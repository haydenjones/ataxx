/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.haydenjones.ataxx.core;

/**
 *
 * @author hjones
 */
public class GameState {
    private final byte currentPlayerID;
    private final byte[][] board;
    
    public GameState(byte[][] newBoard, byte playerID)  {
        super();
        currentPlayerID = playerID;
        board = new byte[newBoard.length][];
        for (int i1=0; i1<newBoard.length; i1++)  {
            board[i1] = new byte[newBoard[i1].length];
            System.arraycopy(newBoard[i1], 0, board[i1], 0, newBoard[i1].length);
        }
    }

    public byte[][] getBoard()  {
        return board;
    }
    
    public byte getCurrentPlayerID() {
        return currentPlayerID;
    }
}
