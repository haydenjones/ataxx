package ca.jhayden.whim.ataxx.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.Scores;

public class ScoreFxPanel {
    @FXML private Label player1Score;
    @FXML private Label player2Score;
    @FXML private Label player3Score;
    @FXML private Label player4Score;
    @FXML private Label currentPlayerLabel;

    public void startNewGame(int numberOfPlayers) {
        player1Score.setText("Red: 2");
        player2Score.setText("Blue: 2");
        
        if (numberOfPlayers == 4) {
            player3Score.setText("Magenta: 2");
            player3Score.setVisible(true);
            player4Score.setText("Orange: 2");
            player4Score.setVisible(true);
        } else {
            player3Score.setVisible(false);
            player4Score.setVisible(false);
        }
        
        currentPlayerLabel.setText("Current: Red");
    }

    public void update(AtaxxState state, boolean gameOver) {
        Scores scores = state.computeScores();
        player1Score.setText("Red: " + scores.piece1());
        player2Score.setText("Blue: " + scores.piece2());
        
        if (state.players().size() == 4) {
            player3Score.setText("Magenta: " + scores.piece3());
            player4Score.setText("Orange: " + scores.piece4());
        }
        
        if (!gameOver) {
            String currentPlayer = switch (state.currentPlayer().tile()) {
                case PIECE_1 -> "Red";
                case PIECE_2 -> "Blue";
                case PIECE_3 -> "Magenta";
                case PIECE_4 -> "Orange";
                default -> "Unknown";
            };
            currentPlayerLabel.setText("Current: " + currentPlayer);
        } else {
            currentPlayerLabel.setText("Game Over!");
        }
    }


}