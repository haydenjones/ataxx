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
    public static byte[][] copyBoard(byte[][] board)  {
        byte[][] newBoard = new byte[board.length][];
        for (int i1=0; i1<newBoard.length; i1++)  {
            newBoard[i1] = new byte[board[i1].length];
            System.arraycopy(board[i1], 0, newBoard[i1], 0, board[i1].length);
        }
        return newBoard;
    }
    
    public static final byte EMPTY_SQUARE = 0;
    public static final byte INVALID_SQUARE = -9;
    
    private final byte currentPlayerID;
    private final byte[][] board;
    
    public GameState(byte[][] newBoard, byte playerID)  {
        super();
        currentPlayerID = playerID;
        board = copyBoard(newBoard);
    }

    public byte[][] getBoard()  {
        return board;
    }
    
    public byte getCurrentPlayerID() {
        return currentPlayerID;
    }
}
