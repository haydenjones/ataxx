package ca.jhayden.whim.ataxx.javafx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import ca.jhayden.whim.ataxx.model.AtaxxRow;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.Pos;
import ca.jhayden.whim.ataxx.model.Tile;
import ca.jhayden.whim.ataxx.ui.GameHub;

public class AtaxxFxPanel extends Canvas {
    private static final int SQUARE_SIZE = 60;
    private static final int GAP = 4;
    
    private final GameHub gameHub;
    private final Tile playerTile;
    private AtaxxState state = AtaxxState.NULL;
    private Pos startPos = null;
    private Pos cursorPos = null;
    
    public AtaxxFxPanel(GameHub hub, Tile myPlayerTile) {
        super(SQUARE_SIZE * 7, SQUARE_SIZE * 7);
        this.gameHub = hub;
        this.playerTile = myPlayerTile;
        
        setOnMouseClicked(this::handleMouseClick);
        setOnMouseMoved(this::handleMouseMove);
    }
    
    private void handleMouseClick(MouseEvent e) {
        if (cursorPos == null) return;
        
        Tile cursorTile = state.board().at(cursorPos);
        
        if (startPos == null) {
            if (cursorTile == playerTile) {
                startPos = cursorPos;
            }
        } else if (startPos.equals(cursorPos)) {
            startPos = null;
        } else if (cursorTile == Tile.EMPTY) {
            GameMove move = GameMove.of(playerTile, startPos, cursorPos);
            if (move != null) {
                startPos = null;
                gameHub.move(move, this);
            }
        }
        
        draw();
    }
    
    private void handleMouseMove(MouseEvent e) {
        cursorPos = new Pos((int)(e.getY() / SQUARE_SIZE), (int)(e.getX() / SQUARE_SIZE));
        draw();
    }
    
    public void update(AtaxxState state, boolean gameOver) {
        this.state = state;
        draw();
    }
    
    private void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        
        int rowPos = 0;
        for (AtaxxRow row : state.board().rows()) {
            int colPos = 0;
            for (Tile tile : row) {
                drawSquare(gc, new Pos(rowPos, colPos), tile);
                colPos++;
            }
            rowPos++;
        }
    }
    
    private void drawSquare(GraphicsContext gc, Pos pos, Tile tile) {
        double x = pos.col() * SQUARE_SIZE;
        double y = pos.row() * SQUARE_SIZE;
        
        // Background
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
        
        // Highlight selected/cursor positions
        if (pos.equals(startPos)) {
            gc.setStroke(Color.GREEN);
            gc.setLineWidth(3);
            gc.strokeRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
        } else if (pos.equals(cursorPos)) {
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(2);
            gc.strokeRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
        }
        
        // Tile content
        if (tile == Tile.WALL) {
            gc.setFill(Color.BLACK);
            gc.fillRect(x + GAP, y + GAP, SQUARE_SIZE - 2*GAP, SQUARE_SIZE - 2*GAP);
        } else {
            gc.setFill(Color.WHITE);
            gc.fillRect(x + GAP, y + GAP, SQUARE_SIZE - 2*GAP, SQUARE_SIZE - 2*GAP);
            
            if (tile != null && tile.isPiece()) {
                gc.setFill(getTileColor(tile));
                gc.fillOval(x + 2*GAP, y + 2*GAP, SQUARE_SIZE - 4*GAP, SQUARE_SIZE - 4*GAP);
            }
        }
    }
    
    private Color getTileColor(Tile tile) {
        return switch (tile) {
            case PIECE_1 -> Color.RED;
            case PIECE_2 -> Color.BLUE;
            case PIECE_3 -> Color.MAGENTA;
            case PIECE_4 -> Color.ORANGE;
            default -> Color.WHITE;
        };
    }
}