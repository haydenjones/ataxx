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
public class FindBestMoveNode {
    private final GameState initial;
    private final byte[] playerOrdering;
    private final GameAI ai;
    private final int depth;
    private final List<FindBestMoveNode> nodes = new ArrayList<FindBestMoveNode>();
    private final byte masterPlayerID;
    private final GameMove move;
    
    public FindBestMoveNode(GameState state, byte[] ordering, GameAI gai, int newDepth)  {
        this(state, ordering, gai, newDepth, state.getCurrentPlayerID(), GameMove.NULL);
    }
    
    FindBestMoveNode(GameState state, byte[] ordering, GameAI gai, int newDepth, byte playerID, GameMove moveJustTaken)  {
        super();
        initial = state;
        playerOrdering = ordering;
        ai = gai;
        depth = newDepth;
        masterPlayerID = playerID;
        move = moveJustTaken;
        createNodes();
    }
    
    final void createNodes()  {
        if (depth == 0)  {
            // I should score the node here...
            return;
        }
        
        List<GameMove> moves = ai.getPossibleMoves(initial);
        for (GameMove move : moves)  {
            GameInfo gi = ai.move(initial, playerOrdering, move);
            nodes.add(new FindBestMoveNode(gi.getState(), playerOrdering, ai, depth - 1, masterPlayerID, move));
        }
    }
    
    public GameMove exec()  {
        int bestScore = -1000;
        List<GameMove> bestMoves = new ArrayList<GameMove>();
        
        for (FindBestMoveNode child : nodes)  {
            int bs = child.exec2();
            if (bestScore == -1000)  {
                bestScore = bs;
            }
            
            if (bs > bestScore)  {
                bestMoves.clear();
                bestScore = bs;
            }
            
            if (bs == bestScore)  {
                bestMoves.add(child.move);
            }
        }
        
        if (bestMoves.isEmpty())  {
            return GameMove.NULL;
        }
        
        Random r = new Random();
        int chosen = r.nextInt(bestMoves.size());
        return bestMoves.get(chosen);
    }
    
    public int exec2()  {
        if (depth == 0)  {
            return score(initial.getBoard(), masterPlayerID);
        }
        
        // My score is going to be the best or worst (depending on who's turn it is)
        final boolean myTurn = (masterPlayerID == initial.getCurrentPlayerID());
        
        int bestScore = -1000;
        for (FindBestMoveNode child : nodes)  {
            int bestChildScore = child.exec2();
            
            if (bestScore == -1000)  {
                bestScore = bestChildScore;
            }
            
            if (myTurn)  {
                bestScore = Math.max(bestScore, bestChildScore);
            }
            else {
                bestScore = Math.min(bestScore, bestChildScore);
            }
        }
        
        return bestScore;
    }
    
    final int score(byte[][] board, byte myPlayerID)  {
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
