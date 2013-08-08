/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.haydenjones.ataxx.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author hjones
 */
public class FindBestMove {
    private final GameState initial;
    private final byte[] playerOrdering;
    private final GameAI ai;
    private final int depth;
    
    private final Random random = new Random();
    
    public FindBestMove(GameState state, byte[] ordering, GameAI gai)  {
        this(state, ordering, gai, 2);
    }
    
    FindBestMove(GameState state, byte[] ordering, GameAI gai, int newDepth)  {
        super();
        initial = state;
        playerOrdering = ordering;
        ai = gai;
        depth = newDepth;
    }
    
    public GameMove exec()  {
        List<GameMove> moves = ai.getPossibleMoves(initial);
        
        final byte myPlayerID = initial.getCurrentPlayerID();
        
        List<GameMove> bestMoves = new ArrayList<GameMove>();
        int bestScore = -99;
        
        // Let's just select the best move from my choices...
        for (GameMove move : moves)  {
            GameInfo gi = ai.move(initial, playerOrdering, move);
            int newScore = score(gi.getState().getBoard(), myPlayerID);
            if (newScore > bestScore)  {
                bestScore = newScore;
                bestMoves.clear();
            }
            
            if (newScore == bestScore)  {
                bestMoves.add(move);
            }
        }
        
        final int size = bestMoves.size();
        if (size == 0)  {
            return GameMove.NULL;
        }
        
        int choice = random.nextInt(size);
        return bestMoves.get(choice);
    }
    
    int score(byte[][] board, byte myPlayerID)  {
        int score = 0;
        for (int i1=0; i1<board.length; i1++)  {
            for (int i2=0; i2<board[i1].length; i2++)  {
                if (board[i1][i2] == myPlayerID)  {
                    score++;
                }
                else if (board[i1][i2] > GameState.EMPTY_SQUARE)  {
                    score--;
                }
            }
        }
        return score;
    }
}
