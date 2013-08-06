/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.haydenjones.ataxx.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author hjones
 */
public class GameAI {

    final boolean isSquareEmpty(GamePos to, GameState state) {
        // Check Row
        if (to.getRow() < 0)  return false;
        if (to.getRow() >= state.getBoard().length)  return false;
        
        // Check Col
        if (to.getCol() < 0)  return false;
        if (to.getCol() >= state.getBoard()[to.getRow()].length) return false;
        
        return (GameState.EMPTY_SQUARE == state.getBoard()[to.getRow()][to.getCol()]);
    }
    
    public enum Grow {
        NW(-1, -1), N(-1, 0), NE(-1, 1),
        W(-1, 0), E(1, 0),
        SW(1, -1), S(1, 0), SE(1, 1);
        
        private final int rowAdj;
        private final int colAdj;
        
        public int getRowAdj()  {
            return rowAdj;
        }
        public int getColAdj()  {
            return colAdj;
        }
        
        public GamePos adjust(GamePos from)  {
            return GamePos.ofRowCol(from.getRow() + getRowAdj(), from.getCol() + getColAdj());
        }
        
        Grow(int ra, int ca)  {
            rowAdj = ra;
            colAdj = ca;
        }
    }
    
    public enum Jump {
        NNWW(-2, -2), NNW(-2, 1), NN(-2, 0), NNE(-2, 1), NNEE(-2, 2),
        NWW(-1, -2), NEE(-1, 2),
        WW(0, -2), EE(0, 2),
        SWW(1, -2), SEE(1, 2),
        SSWW(2, -2), SSW(2, 1), SS(2, 0), SSE(2, 1), SSEE(2, 2);
        
        private final int rowAdj;
        private final int colAdj;
        
        public int getRowAdj()  {
            return rowAdj;
        }
        public int getColAdj()  {
            return colAdj;
        }
        
        public GamePos adjust(GamePos from)  {
            return GamePos.ofRowCol(from.getRow() + getRowAdj(), from.getCol() + getColAdj());
        }
        
        Jump(int ra, int ca)  {
            rowAdj = ra;
            colAdj = ca;
        }
    }
    
    private GameAI()  {
        super();
    }
    
    public Collection<GameMove> getPossibleMoves(GameState state)  {
        List<GameMove> moves = new ArrayList<GameMove>();
        
        for (int i1=0; i1<state.getBoard().length; i1++)  {
            for (int i2=0; i2<state.getBoard()[i1].length; i2++)  {
                if (state.getCurrentPlayerID() != state.getBoard()[i1][i2])  {
                    continue;
                }
                
                final GamePos from = GamePos.ofRowCol(i1, i2);
                
                // Detect Grows
                for (Grow g : Grow.values())  {
                    GamePos to = g.adjust(from);
                    if (isSquareEmpty(to, state))  {
                        moves.add(new GameMove(from, to));
                        break;
                    }
                }
                
                // Detect Jumps
                for (Jump j : Jump.values())  {
                    GamePos to = j.adjust(from);
                    if (isSquareEmpty(to, state))  {
                        moves.add(new GameMove(from, to));
                        break;
                    }
                }
            }
        }
        
        return moves;
    }
}
