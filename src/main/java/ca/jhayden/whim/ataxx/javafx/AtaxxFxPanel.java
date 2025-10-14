package ca.jhayden.whim.ataxx.javafx;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import ca.jhayden.whim.ataxx.model.AtaxxRow;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.Pos;
import ca.jhayden.whim.ataxx.model.Tile;
import ca.jhayden.whim.ataxx.ui.GameHub;

public class AtaxxFxPanel {
    private static final int SQUARE_SIZE = 60;
    private final GridPane grid = new GridPane();
    private final GameHub gameHub;
    private final Tile playerTile;
    
    private AtaxxState state = AtaxxState.NULL;
    private Pos startPos = null;

    public AtaxxFxPanel(GameHub hub, Tile myPlayerTile) {
        this.gameHub = hub;
        this.playerTile = myPlayerTile;
        setupGrid();
    }

    private void setupGrid() {
        grid.setHgap(2);
        grid.setVgap(2);
        
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                square.setFill(Color.WHITE);
                square.setStroke(Color.GRAY);
                
                final int r = row, c = col;
                square.setOnMouseClicked(e -> handleClick(new Pos(r, c)));
                
                grid.add(square, col, row);
            }
        }
    }

    private void handleClick(Pos pos) {
        if (!state.currentPlayer().isHuman() || state.currentPlayer().tile() != playerTile) {
            return;
        }

        Tile cursorTile = state.board().at(pos);

        if (startPos == null) {
            if (cursorTile == playerTile) {
                startPos = pos;
                updateDisplay();
            }
        } else if (startPos.equals(pos)) {
            startPos = null;
            updateDisplay();
        } else if (cursorTile == Tile.EMPTY) {
            GameMove move = GameMove.of(playerTile, startPos, pos);
            if (move != null) {
                startPos = null;
                gameHub.move(move, this);
            }
        }
    }

    public void update(AtaxxState state, boolean gameOver) {
        this.state = state;
        updateDisplay();
    }

    private void updateDisplay() {
        if (state == AtaxxState.NULL) return;

        int rowPos = 0;
        for (AtaxxRow row : state.board().rows()) {
            int colPos = 0;
            for (Tile tile : row) {
                Rectangle square = (Rectangle) grid.getChildren().get(rowPos * 7 + colPos);
                Pos pos = new Pos(rowPos, colPos);

                if (tile == Tile.WALL) {
                    square.setFill(Color.BLACK);
                } else {
                    square.setFill(Color.WHITE);
                }

                if (pos.equals(startPos)) {
                    square.setStroke(Color.GREEN);
                    square.setStrokeWidth(3);
                } else {
                    square.setStroke(Color.GRAY);
                    square.setStrokeWidth(1);
                }

                final int finalRow = rowPos;
                final int finalCol = colPos;
                grid.getChildren().removeIf(node -> 
                    GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == finalRow && 
                    GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == finalCol && 
                    node instanceof Circle);

                if (tile.isPiece()) {
                    Circle piece = new Circle(SQUARE_SIZE / 3);
                    piece.setFill(getTileColor(tile));
                    piece.setMouseTransparent(true);
                    grid.add(piece, colPos, rowPos);
                }

                colPos++;
            }
            rowPos++;
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

    public Parent getRoot() {
        return grid;
    }
}