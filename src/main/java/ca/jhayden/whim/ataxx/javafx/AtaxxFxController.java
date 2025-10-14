package ca.jhayden.whim.ataxx.javafx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

import ca.jhayden.whim.ataxx.engine.GameEngine;
import ca.jhayden.whim.ataxx.engine.GameSetup;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;
import ca.jhayden.whim.ataxx.ui.GameHub;

public class AtaxxFxController extends BorderPane implements GameHub, Initializable {
    private ScoreFxPanel scorePanel;
    private AtaxxSetupFx setupPanel;
    private AtaxxFxPanel gamePanel;
    
    private GameSetup gameSetup = null;
    private AtaxxState state = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader scoreLoader = new FXMLLoader(getClass().getResource("/ca/jhayden/whim/ataxx/javafx/score.fxml"));
            scorePanel = new ScoreFxPanel();
            scoreLoader.setController(scorePanel);
            setTop(scoreLoader.load());
            
            FXMLLoader setupLoader = new FXMLLoader(getClass().getResource("/ca/jhayden/whim/ataxx/javafx/setup.fxml"));
            setupPanel = new AtaxxSetupFx(this);
            setupLoader.setController(setupPanel);
            setCenter(setupLoader.load());
            
            gamePanel = new AtaxxFxPanel(this, Tile.PIECE_1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startNewGame(GameSetup setup) {
        this.gameSetup = setup;
        this.scorePanel.startNewGame(setup.getNumberOfPlayers().value());
        this.state = GameEngine.newGame(setup);

        setCenter(gamePanel.getRoot());
        updateGameState(state);
    }

    private void updateGameState(AtaxxState newState) {
        this.state = newState;

        boolean gameOver = GameEngine.isGameOver(newState);
        if (gameOver) {
            String message = computeGameOverMessage(newState);
            System.out.println("G A M E _ O V E R");
            System.out.println(message);
        }

        gamePanel.update(newState, gameOver);
        scorePanel.update(newState, gameOver);

        if (newState.currentPlayer().isHuman()) {
            var playerMoves = GameEngine.computeAllMoves(newState);
            if (playerMoves.isEmpty()) {
                move(GameMove.PASS, null);
            }
        }
    }

    private String computeGameOverMessage(AtaxxState state) {
        Scores score = state.computeScores();
        int bestScore = 0;
        String winner = "";

        for (Player p : state.players()) {
            int playerScore = score.count(p.tile());
            if (playerScore > bestScore) {
                bestScore = playerScore;
                winner = p.tile().name();
            }
        }

        return "Winner: " + winner;
    }

    @Override
    public void move(GameMove move, Object from) {
        if (GameEngine.isValidMove(this.state, move)) {
            AtaxxState newState = GameEngine.applyMove(this.state, move);
            updateGameState(newState);

            if (!GameEngine.isGameOver(newState) && !newState.currentPlayer().isHuman()) {
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        Platform.runLater(() -> move(GameMove.PASS, this));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }
        }
    }
}