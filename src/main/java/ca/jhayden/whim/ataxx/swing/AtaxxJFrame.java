package ca.jhayden.whim.ataxx.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.EnumMap;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ca.jhayden.whim.ataxx.ai.ComputeAiMove;
import ca.jhayden.whim.ataxx.engine.GameEngine;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.Tile;

public class AtaxxJFrame extends JFrame implements GameHub {
	private static final long serialVersionUID = 5990472863772348300L;

	static final EnumMap<Tile, Color> COLOR_MAP = new EnumMap<>(Tile.class);
	static {
		COLOR_MAP.put(Tile.EMPTY, Color.WHITE);
		COLOR_MAP.put(Tile.WALL, Color.BLACK);
		COLOR_MAP.put(Tile.PIECE_1, Color.RED);
		COLOR_MAP.put(Tile.PIECE_2, Color.BLUE);
		COLOR_MAP.put(Tile.PIECE_3, Color.MAGENTA);
		COLOR_MAP.put(Tile.PIECE_4, Color.ORANGE);
	}

	private final ScoreJPanel scorePanel = new ScoreJPanel();
	private final AtaxxSetupGame setupPanel = new AtaxxSetupGame(this);
	private final AtaxxJPanel gamePanel = new AtaxxJPanel(this, Tile.PIECE_1);

	private AtaxxState state = null;

	public AtaxxJFrame() {
		super();

		this.setLayout(new BorderLayout());

		this.getContentPane().add(scorePanel, BorderLayout.NORTH);
		this.getContentPane().add(setupPanel, BorderLayout.CENTER);
	}

	@Override
	public void startNewGame(int numberOfPlayers) {
		this.getContentPane().remove(setupPanel);
		this.getContentPane().add(gamePanel, BorderLayout.CENTER);
		this.scorePanel.startNewGame(numberOfPlayers);
		state = GameEngine.newGame(numberOfPlayers);
		this.updateGameState(state);
	}

	void updateGameState(AtaxxState newState) {
		this.state = newState;

		boolean gameOver = GameEngine.isGameOver(newState);
		if (gameOver) {
			System.out.println("G A M E _ O V E R");
		}

		gamePanel.update(newState, gameOver);
		scorePanel.update(newState, gameOver);
		this.pack();
		this.repaint();
	}

	@Override
	public void move(final GameMove move, Object from) {
		boolean allow = GameEngine.isValidMove(this.state, move);
		if (allow) {
			AtaxxState newState = GameEngine.applyMove(this.state, move);
			this.updateGameState(newState);

			if (!GameEngine.isGameOver(newState)) {
				if (!newState.currentPlayer().isHuman()) {
					PerformAiMoveTask task = new PerformAiMoveTask(this, newState);
					Thread t = new Thread(task);
					t.start();
				}
			}
		}
	}
}

class PerformAiMoveTask implements Runnable {
	private final GameHub gameHub;
	private final AtaxxState state;

	public PerformAiMoveTask(GameHub gh, AtaxxState newState) {
		super();
		this.gameHub = gh;
		this.state = newState;
	}

	@Override
	public void run() {
		ComputeAiMove moveFinder = new ComputeAiMove(state);
		final GameMove move = moveFinder.call();

		try {
			if (move == GameMove.PASS) {
				TimeUnit.MILLISECONDS.sleep(500);
			}
			else {
				TimeUnit.SECONDS.sleep(2);
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> gameHub.move(move, this));
	}
}
