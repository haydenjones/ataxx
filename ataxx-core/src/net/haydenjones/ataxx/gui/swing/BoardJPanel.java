/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.haydenjones.ataxx.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.haydenjones.ataxx.core.GameInfo;
import net.haydenjones.ataxx.core.GameState;

/**
 *
 * @author hjones
 */
public class BoardJPanel extends JPanel {
    public static void main(String[] args) {
        System.out.println("Begin");
        
        byte[][] board = new byte[][] {
            new byte[] { 1,  0,  0, 0,  0,  0, 2 },
            new byte[] { 0,  0, -1, 0, -1,  0, 0 },
            new byte[] { 0, -1,  0, 0,  0, -1, 0 },
            new byte[] { 0,  0,  0, 0,  0,  0, 0 },
            new byte[] { 0, -1,  0, 0,  0, -1, 0 },
            new byte[] { 0,  0, -1, 0, -1,  0, 0 },
            new byte[] { 2,  0,  0, 0,  0,  0, 1 },
        };
                
        GameState gs = new GameState(board, (byte) 1);
        GameInfo gi = new GameInfo(gs, new byte[] { 1, 2 });
        
        BoardJPanel panel = new BoardJPanel(gi);
        JOptionPane.showConfirmDialog(null, panel);
        
        System.out.println("End");
    }
    
    
    private final JLabel red = new JLabel(" 2 ");
    private final JLabel blue = new JLabel(" 2 ");
    private final GamePanel gamePanel;
    
    private BoardJPanel(GameInfo gi) {
        super(new BorderLayout());
        
        red.setForeground(Color.RED);
        add(red, BorderLayout.WEST);
        
        blue.setForeground(Color.BLUE);
        add(blue, BorderLayout.EAST);
        
        gamePanel = new GamePanel(gi, this);
        add(gamePanel, BorderLayout.CENTER);
    }
    
    public void updateScores(int redScore, int blueScore)  {
        red.setText(" " + redScore + " ");
        blue.setText(" " + blueScore + " ");
        repaint();
    }
}
