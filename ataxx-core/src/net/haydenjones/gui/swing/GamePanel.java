/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.haydenjones.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
    
    private final Dimension size;

    private final AtomicReference<GamePos> fromPos = new AtomicReference<GamePos>();
    private final AtomicReference<GamePos> hoverPos = new AtomicReference<GamePos>();
    
    public GamePanel(GameInfo info)  {
        super();
        gameInfo = info;
        stateRef.set(info.getState());
        size = new Dimension(squareSize * info.getState().getBoard()[0].length, squareSize * info.getState().getBoard().length);
        
        this.setMaximumSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);

        final MyMouseListener mml = new MyMouseListener(this);
        addMouseListener(mml);
        addMouseMotionListener(mml);
        
        fromPos.set(GamePos.NULL);
        hoverPos.set(GamePos.NULL);
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
            g2d.setColor(Color.PINK);
            g2d.drawRect(hover.getCol() * squareSize, hover.getRow() * squareSize, squareSize, squareSize);
        }
    }
    
    public void clickAt(GamePos gamePos)  {
        GamePos from = fromPos.get();
        GameState gs = stateRef.get();
        
        byte cp = gs.getCurrentPlayerID();
        
        if (from == GamePos.NULL)  {
            if (gs.getBoard()[gamePos.getRow()][gamePos.getCol()] == cp)  {
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
                }
            }
            else if (gamePos.equals(from))  {
                fromPos.set(GamePos.NULL);
            }
            else if (gs.getBoard()[gamePos.getRow()][gamePos.getCol()] == cp)  {
                fromPos.set(gamePos);
            }
            else {
                return;
            }
        }
        
        repaint();
    }

    void movedTo(GamePos gp) {
        final GamePos previous = hoverPos.get();
        if (previous.equals(gp))  {
            return;
        }
        hoverPos.set(gp);
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