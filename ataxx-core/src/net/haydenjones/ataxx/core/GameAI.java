/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.haydenjones.ataxx.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author hjones
 */
public class GameAI {

    public GameMove determineMove(GameState state, byte[] playerOrdering) {
        FindBestMove fbm = new FindBestMove(state, playerOrdering, this);
        return fbm.exec();
    }
    
    public enum MoveType {
        NULL, GROW, JUMP;
    }
    
    public enum Grow {
        NW(-1, -1), N(-1, 0), NE(-1, 1),
         W(0, -1),             E(0, 1),
        SW(1, -1),  S(1, 0),  SE(1, 1);
        
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
         NWW(-1, -2),                                     NEE(-1, 2),
          WW(0, -2),                                       EE(0, 2),
         SWW(1, -2),                                      SEE(1, 2),
        SSWW(2, -2),  SSW(2, -1), SS(2, 0),  SSE(2, 1),  SSEE(2, 2);
        
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
    
    public GameAI()  {
        super();
    }
    
    public List<GameMove> getPossibleMoves(GameState state)  {
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

    public MoveType getMoveType(GameState state, GameMove move)  {
        byte source = getSquare(move.getFrom(), state.getBoard());
        if (source != state.getCurrentPlayerID())  {
            return MoveType.NULL;
        }
        
        byte target = getSquare(move.getTo(), state.getBoard());
        if (target != GameState.EMPTY_SQUARE)  {
            return MoveType.NULL;
        }
        
        // Is this a grow?
        for (Grow g : Grow.values())  {
            if (g.adjust(move.getFrom()).equals(move.getTo()))  {
                return MoveType.GROW;
            }
        }
        
        // Is this a jump?
        for (Jump g : Jump.values())  {
            if (g.adjust(move.getFrom()).equals(move.getTo()))  {
                return MoveType.JUMP;
            }
        }
        
        return MoveType.NULL;
    }
    
    public boolean isValidMove(GameState state, GameMove move)  {
        return (getMoveType(state, move) != MoveType.NULL);
    }
    
    final byte getSquare(GamePos to, byte[][] board)  {
        // Check Row
        if (to.getRow() < 0)  return GameState.INVALID_SQUARE;
        if (to.getRow() >= board.length)  return GameState.INVALID_SQUARE;
        
        // Check Col
        if (to.getCol() < 0)  return GameState.INVALID_SQUARE;
        if (to.getCol() >= board[to.getRow()].length)  return GameState.INVALID_SQUARE;
        
        return board[to.getRow()][to.getCol()];
    }
    
    final boolean isSquareEmpty(GamePos to, GameState state) {
        byte content = getSquare(to, state.getBoard());
        return (GameState.EMPTY_SQUARE == content);
    }
    
    static int indexOf(byte[] bytes, byte toFind)  {
        for (int i1=0; i1<bytes.length; i1++)  {
            if (bytes[i1] == toFind)  {
                return i1;
            }
        }
        return -1;
    }
    
    final byte findNextPlayerID(byte[][] board, byte currentPlayerID, byte[] playerOrdering)  {
        AtomicInteger index = new AtomicInteger(indexOf(playerOrdering, currentPlayerID));

        List<Byte> ordering = new ArrayList<Byte>();
        for (int i1=0; i1<playerOrdering.length; i1++)  {
            ordering.add(playerOrdering[(index.intValue() + i1 + 1) % playerOrdering.length]);
        }
        
        int earliest = playerOrdering.length;
        
        for (int i1=0; i1<board.length; i1++)  {
            for (int i2=0; i2<board[i1].length; i2++)  {
                if (GameState.EMPTY_SQUARE != board[i1][i2])  {
                    continue;
                }
                
                // Any grow moves here?
                GamePos toPos = GamePos.ofRowCol(i1, i2);
                for (Grow x : Grow.values())  {
                    GamePos fromPos = x.adjust(toPos);
                    byte b = getSquare(fromPos, board);
                    if (b > GameState.EMPTY_SQUARE)  {
                        earliest = Math.min(earliest, ordering.indexOf(b));
                    }
                }
                
                // Ant jump moves here?
                for (Jump x : Jump.values())  {
                    GamePos fromPos = x.adjust(toPos);
                    byte b = getSquare(fromPos, board);
                    if (b > GameState.EMPTY_SQUARE)  {
                        earliest = Math.min(earliest, ordering.indexOf(b));
                    }
                }
                
                if (earliest == 0)  {
                    return ordering.get(0);
                }
            }
        }
        
        if (earliest >= ordering.size())  {
            return -1;
        }
        return ordering.get(earliest);
    }
    
    public GameInfo move(GameState state, byte[] playerOrdering, GameMove move)  {
        final byte currentPlayerID = state.getCurrentPlayerID();
        
        // Check that the move is valid
        
        // execute the move
        byte[][] board = GameState.copyBoard(state.getBoard());
        
        // MoveType???
        MoveType type = getMoveType(state, move);
        if (type == MoveType.JUMP)  {
            board[move.getFrom().getRow()][move.getFrom().getCol()] = GameState.EMPTY_SQUARE;
        }
        board[move.getTo().getRow()][move.getTo().getCol()] = currentPlayerID;
        
        // Flip pieces
        for (Grow g : Grow.values())  {
            GamePos gp = g.adjust(move.getTo());
            byte content = getSquare(gp, board);
            if (content > 0)  {
                board[gp.getRow()][gp.getCol()] = currentPlayerID;
            }
        }
        
        // Move forward the playerIndex
        final byte nextPlayerID = findNextPlayerID(board, currentPlayerID, playerOrdering);
        
        return new GameInfo(new GameState(board, nextPlayerID), playerOrdering);
    }
}
