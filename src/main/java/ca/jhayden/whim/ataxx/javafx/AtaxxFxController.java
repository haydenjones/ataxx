package ca.jhayden.whim.ataxx.javafx;

import java.util.concurrent.TimeUnit;

import ca.jhayden.whim.ataxx.ai.ComputeAiMove;
import ca.jhayden.whim.ataxx.engine.GameEngine;
import ca.jhayden.whim.ataxx.engine.GameSetup;
import ca.jhayden.whim.ataxx.engine.GameSetupType;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;
import ca.jhayden.whim.ataxx.ui.GameHub;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AtaxxFxController implements GameHub {

	@FXML
	private VBox setupPanel;
	@FXML
	private VBox gamePanel;
	@FXML
	private HBox scorePanel;
	@FXML
	private AtaxxFxPanel boardPanel;

	private GameSetup gameSetup;
	private AtaxxState state;
	private ScoreFxPanel scoreFxPanel;

	@FXML
	private void initialize() {
		createSetupButtons();
		scoreFxPanel = new ScoreFxPanel();

		scorePanel.getChildren().add(scoreFxPanel);

		boardPanel = new AtaxxFxPanel(this, Tile.PIECE_1);

		gamePanel.getChildren().add(boardPanel);
		gamePanel.setVisible(false);
	}

	private void createSetupButtons() {
		for (GameSetupType gst : GameSetupType.values()) {
			Button button = new Button(gst.name());
			button.setOnAction(e -> startNewGame(gst));
			setupPanel.getChildren().add(button);
		}
	}

	@Override
	public void startNewGame(GameSetup setup) {
		this.gameSetup = setup;
		this.state = GameEngine.newGame(setup).endState();

		scoreFxPanel.startNewGame(setup.getNumberOfPlayers().value());
		setupPanel.setVisible(false);
		gamePanel.setVisible(true);

		updateGameState(state);
	}

	private void updateGameState(AtaxxState newState) {
		this.state = newState;

		final boolean gameOver = GameEngine.isGameOver(newState);

		boardPanel.update(newState, gameOver);
		scoreFxPanel.update(newState, gameOver);

		if (gameOver) {
			System.out.println("GAME OVER");
			System.out.println(computeGameOverMessage(newState));
		}

		if (newState.currentPlayer().isHuman() && GameEngine.computeAllMoves(newState).isEmpty()) {
			move(GameMove.PASS, null);
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
			AtaxxState newState = GameEngine.applyMove(this.state, move).endState();
			updateGameState(newState);

			if (!GameEngine.isGameOver(newState) && !newState.currentPlayer().isHuman()) {
				Thread aiThread = new Thread(() -> {
					ComputeAiMove moveFinder = new ComputeAiMove(newState, gameSetup.getAiType().value(), true);
					GameMove aiMove = moveFinder.call();

					try {
						TimeUnit.SECONDS.sleep(1);
					}
					catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}

					Platform.runLater(() -> move(aiMove, this));
				});
				aiThread.setDaemon(true);
				aiThread.start();
			}
		}
	}
}