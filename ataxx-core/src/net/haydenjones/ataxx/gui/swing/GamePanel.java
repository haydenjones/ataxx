/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.haydenjones.ataxx.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.haydenjones.ataxx.core.GameAI;
import net.haydenjones.ataxx.core.GameInfo;
import net.haydenjones.ataxx.core.GameMove;
import net.haydenjones.ataxx.core.GamePos;
import net.haydenjones.ataxx.core.GameState;


/**
 *
 * @author hjones
 */
public class GamePanel extends JPanel {
    public static void main(String[] args) {
        System.out.println("Begin");
        
        byte[][] board = new byte[][] {
            new byte[] { 1, 0, 0, 0, 0, 0, 2 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 0, 0, 0, 0, 0, 0, 0 },
            new byte[] { 2, 0, 0, 0, 0, 0, 1 },
        };
                
        GameState gs = new GameState(board, (byte) 1);
        GameInfo gi = new GameInfo(gs, new byte[] { 1, 2 });
        
        GamePanel panel = new GamePanel(gi);
        JOptionPane.showConfirmDialog(null, panel);
        
        System.out.println("End");
    }
    
    final static int squareSize = 20;
    
    private final GameInfo gameInfo;
    private final GameAI ai = new GameAI();
    private final AtomicReference<GameState> stateRef = new AtomicReference<GameState>();
    private final byte myPlayerID;
    
    private final Dimension size;

    private final AtomicReference<GamePos> fromPos = new AtomicReference<GamePos>();
    private final AtomicReference<GamePos> hoverPos = new AtomicReference<GamePos>();
    private final AtomicBoolean myTurn = new AtomicBoolean(false);
    private final Timer timer = new Timer(true);

    byte[] getPlayerOrdering()  {
        return gameInfo.getPlayerOrdering();
    }
    
    public boolean isPlayerTurn()  {
        return myTurn.get();
    }
    
    public GamePanel(GameInfo info)  {
        super();
        gameInfo = info;
        stateRef.set(info.getState());
        size = new Dimension(squareSize * info.getState().getBoard()[0].length, squareSize * info.getState().getBoard().length);
    
        myPlayerID = info.getState().getCurrentPlayerID();
        
        this.setMaximumSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);

        final MyMouseListener mml = new MyMouseListener(this);
        addMouseListener(mml);
        addMouseMotionListener(mml);
        
        fromPos.set(GamePos.NULL);
        hoverPos.set(GamePos.NULL);
        
        timer.schedule(new AiTask(this, ai), 0, 100L);
    }
    
    @Override
    public void paint(Graphics g)  {
        Graphics2D g2d = (Graphics2D) g;
        
        GameState gs = stateRef.get();
        GamePos from = fromPos.get();
        
        // Blank out the playing area
        g2d.clearRect(0, 0, size.width, size.height);
        
        // Draw Current Board
        
        for (int row=0; row<gs.getBoard().length; row++)  {
            for (int col=0; col<gs.getBoard()[row].length; col++)  {
                g2d.setColor(Color.BLACK);
                g2d.drawRect(col * squareSize, row * squareSize, squareSize, squareSize);
                
                byte b = gs.getBoard()[row][col];

                g2d.setColor(Color.GRAY);
                g2d.drawRect(col * squareSize, row * squareSize, squareSize - 1, squareSize - 1);

                if (b > GameState.EMPTY_SQUARE)  {
                    if ((from.getRow() == row) && (from.getCol() == col))  {
                        g2d.setColor(Color.ORANGE);
                    }
                    else if (b == 1)  {
                        g2d.setColor(Color.RED);
                    }
                    else if (b == 2)  {
                        g2d.setColor(Color.BLUE);
                    }
                    g2d.fillOval(col * squareSize + 1, row * squareSize + 1, squareSize - 3, squareSize - 3);
                }
            }
        }
        
        // Draw Hover Square
        GamePos hover = hoverPos.get();
        if (hover != GamePos.NULL)  {
            if (hover.getRow() < gs.getBoard().length)  {
                if (hover.getCol() < gs.getBoard()[hover.getRow()].length)  {
                    g2d.setColor(Color.PINK);
                    g2d.drawRect(hover.getCol() * squareSize, hover.getRow() * squareSize, squareSize, squareSize);
                }
            }
        }
    }
    
    public void clickAt(GamePos gamePos)  {
        System.out.println("Click @ " + gamePos + " -- " + myTurn.get());
        if (!myTurn.get())  {
            return;
        }
        
        GamePos from = fromPos.get();
        GameState gs = stateRef.get();
        
        if (from == GamePos.NULL)  {
            if (gs.getBoard()[gamePos.getRow()][gamePos.getCol()] == myPlayerID)  {
                fromPos.set(gamePos);
            }
        }
        else {
            if (gs.getBoard()[gamePos.getRow()][gamePos.getCol()] == GameState.EMPTY_SQUARE)  {
                GameMove move = new GameMove(from, gamePos);
                boolean valid = ai.isValidMove(gs, move);
                if (valid)  {
                    GameInfo info = ai.move(gs, gameInfo.getPlayerOrdering(), move);
                    stateRef.set(info.getState());
                    fromPos.set(GamePos.NULL);
                    myTurn.set(false);
                }
            }
            else if (gamePos.equals(from))  {
                fromPos.set(GamePos.NULL);
            }
            else if (gs.getBoard()[gamePos.getRow()][gamePos.getCol()] == myPlayerID)  {
                fromPos.set(gamePos);
            }
            else {
                return;
            }
        }
        
        repaint();
    }

    void movedTo(GamePos gp) {
        if (!myTurn.get())  {
            return;
        }
        
        final GamePos previous = hoverPos.get();
        if (previous.equals(gp))  {
            return;
        }
        hoverPos.set(gp);
        repaint();
    }

    public GameState getGameState()  {
        return stateRef.get();
    }
    
    byte getMyPlayerID()  {
        return myPlayerID;
    }
    
    void ai(GameMove move)  {
        if (move == GameMove.NULL)  {
            myTurn.set(true);
            repaint();
            return;
        }
        
        GameInfo info = ai.move(stateRef.get(), gameInfo.getPlayerOrdering(), move);
        stateRef.set(info.getState());
        fromPos.set(GamePos.NULL);
        myTurn.set(false);
        repaint();
    }
}
class MyMouseListener implements MouseListener, MouseMotionListener {
    private final GamePanel panel;
    
    public MyMouseListener(GamePanel gamePanel)  {
        super();
        panel = gamePanel;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = e.getY() / GamePanel.squareSize;
        int col = e.getX() / GamePanel.squareSize;
        GamePos gp = GamePos.ofRowCol(row, col);
        panel.clickAt(gp);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int row = e.getY() / GamePanel.squareSize;
        int col = e.getX() / GamePanel.squareSize;
        GamePos gp = GamePos.ofRowCol(row, col);
        panel.movedTo(gp);
    }
    
}

class AiTask extends TimerTask {
    private final AtomicBoolean running = new AtomicBoolean(false);

    private final GamePanel gamePanel;
    private final GameAI ai;
    
    public AiTask(GamePanel panel, GameAI gai)  {
        super();
        gamePanel = panel;
        ai = gai;
    }
    
    @Override
    public void run() {
        System.out.println("");
        System.out.println("running...");
        synchronized(running)  {
            boolean r = running.get();
            if (r)  {
                System.out.println("I am running go away");
                return;
            }
            
            System.out.println("I am now running");
            running.set(true);
        }
        
        try  {
            if (gamePanel.isPlayerTurn())  {
                System.out.println("It's the player turn, go away");
                return;
            }
            
            // Check if it's the player Turn
            GameState state = gamePanel.getGameState();
            if (state.getCurrentPlayerID() == gamePanel.getMyPlayerID())  {
                System.out.println("Current player should be player, update GUI");
                gamePanel.ai(GameMove.NULL);
                return;
            }
            
            GameMove aiMove = ai.determineMove(state, gamePanel.getPlayerOrdering());
            gamePanel.ai(aiMove);
        }
        finally  {
            running.set(false);
        }
    }
    
}