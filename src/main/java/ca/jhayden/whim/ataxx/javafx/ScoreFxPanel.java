package ca.jhayden.whim.ataxx.javafx;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;

public class ScoreFxPanel extends HBox {
    
    private final Label[] scoreLabels = new Label[4];
    private final String[] colorNames = {"RED", "BLUE", "MAGENTA", "ORANGE"};
    private final Color[] colors = {Color.RED, Color.BLUE, Color.MAGENTA, Color.ORANGE};
    
    public ScoreFxPanel() {
        setSpacing(20);
        for (int i = 0; i < 4; i++) {
            scoreLabels[i] = new Label();
            scoreLabels[i].setFont(Font.font("Arial", FontWeight.BOLD, 14));
        }
    }
    
    public void startNewGame(int numberOfPlayers) {
        getChildren().clear();
        for (int i = 0; i < numberOfPlayers; i++) {
            getChildren().add(scoreLabels[i]);
        }
    }
    
    public void update(AtaxxState state, boolean gameOver) {
        Scores scores = state.computeScores();
        Tile currentPlayerTile = state.currentPlayer().tile();
        
        Tile[] tiles = {Tile.PIECE_1, Tile.PIECE_2, Tile.PIECE_3, Tile.PIECE_4};
        
        for (int i = 0; i < scoreLabels.length; i++) {
            if (scoreLabels[i].getParent() != null) {
                int score = scores.count(tiles[i]);
                String text = colorNames[i] + ": " + score;
                scoreLabels[i].setText(text);
                
                if (tiles[i] == currentPlayerTile && !gameOver) {
                    scoreLabels[i].setTextFill(colors[i]);
                    scoreLabels[i].setStyle("-fx-background-color: lightgray; -fx-padding: 5;");
                } else {
                    scoreLabels[i].setTextFill(Color.BLACK);
                    scoreLabels[i].setStyle("-fx-padding: 5;");
                }
            }
        }
    }
}