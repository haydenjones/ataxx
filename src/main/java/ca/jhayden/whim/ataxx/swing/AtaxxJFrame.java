package ca.jhayden.whim.ataxx.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ca.jhayden.whim.ataxx.ai.ComputeAiMove;
import ca.jhayden.whim.ataxx.engine.GameEngine;
import ca.jhayden.whim.ataxx.engine.GameSetup;
import ca.jhayden.whim.ataxx.model.AnimateInfo;
import ca.jhayden.whim.ataxx.model.AtaxxChangeInfo;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.ChangeType;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;
import ca.jhayden.whim.ataxx.ui.GameHub;

public class AtaxxJFrame extends JFrame implements GameHub {
	private static final long serialVersionUID = 5990472863772348300L;

	static final EnumMap<Tile, Color> COLOR_MAP = new EnumMap<>(Tile.class);
	static final EnumMap<Tile, String> COLOR_NAME = new EnumMap<>(Tile.class);
	static {
		COLOR_MAP.put(Tile.EMPTY, Color.WHITE);
		COLOR_MAP.put(Tile.WALL, Color.BLACK);

		COLOR_MAP.put(Tile.PIECE_1, Color.WHITE);
		COLOR_NAME.put(Tile.PIECE_1, "");

		COLOR_MAP.put(Tile.PIECE_2, Color.WHITE);
		COLOR_NAME.put(Tile.PIECE_2, "");

		COLOR_MAP.put(Tile.PIECE_3, Color.WHITE);
		COLOR_NAME.put(Tile.PIECE_3, "");

		COLOR_MAP.put(Tile.PIECE_4, Color.WHITE);
		COLOR_NAME.put(Tile.PIECE_4, "");
	}

	private final ScoreJPanel scorePanel = new ScoreJPanel();
	private final AtaxxSetupGameJPanel setupPanel = new AtaxxSetupGameJPanel(this);
	private final AtaxxBoardJPanel gamePanel = new AtaxxBoardJPanel(this, Tile.PIECE_1);

	private GameSetup gameSetup = null;
	private AtaxxState state = null;

	public AtaxxJFrame() {
		super();

		this.setLayout(new BorderLayout());

		this.getContentPane().add(scorePanel, BorderLayout.NORTH);
		this.getContentPane().add(setupPanel, BorderLayout.CENTER);
	}

	@Override
	public void startNewGame(GameSetup setup) {
		this.gameSetup = setup;
		final AtaxxChangeInfo changeInfo = GameEngine.newGame(setup);

		for (Player p : changeInfo.endState().players()) {
			COLOR_MAP.put(p.tile(), p.color());
			COLOR_NAME.put(p.tile(), p.name());
		}

		this.state = changeInfo.endState();
		this.scorePanel.update(changeInfo);

		this.getContentPane().remove(setupPanel);
		this.getContentPane().add(gamePanel, BorderLayout.CENTER);

		this.updateGameState(changeInfo);
	}

	static String computeGameOverMessage(AtaxxState state) {
		return computeGameOverMessage(state.computeScores(), state.players());
	}

	static String computeGameOverMessage(final Scores score, final List<Player> players) {
		int bestScore = 0;
		final List<String> bestPlayers = new ArrayList<>();
		System.out.println(score);

		for (Player p : players) {
			final Tile playerTile = p.tile();
			final int playerScore = score.count(p.tile());

			System.out.println(playerScore + "/" + bestScore + ") " + p);

			if (playerScore > bestScore) {
				bestPlayers.clear();
				bestScore = playerScore;
			}

			if (bestScore == playerScore) {
				bestPlayers.add(COLOR_NAME.get(playerTile));
			}

			System.out.println(bestPlayers);
			System.out.println("");
		}

		return "Winners are " + bestPlayers;
	}

	void updateGameState(AtaxxChangeInfo changeInfo) {
		final AtaxxState newState = changeInfo.endState();
		this.state = newState;

		if (changeInfo.type() == ChangeType.START_NEW_GAME) {
			this.pack();
		}
		else if (changeInfo.type() == ChangeType.GAME_OVER) {
			System.out.println("G A M E _ O V E R");
			String message = AtaxxJFrame.computeGameOverMessage(newState);
			System.out.println(message);
		}

		gamePanel.update(changeInfo);
		scorePanel.update(changeInfo);

		this.repaint();

		// If the player is human and they don't have a move, then PASS.
		if (newState.currentPlayer().isHuman()) {
			SortedSet<GameMove> playerMoves = GameEngine.computeAllMoves(newState);
			if (playerMoves.isEmpty()) {
				this.move(GameMove.PASS, null);
			}
		}
	}

	@Override
	public void move(final GameMove move, Object from) {
		boolean allow = GameEngine.isValidMove(this.state, move);
		if (allow) {
			final var animations = new ArrayList<AnimateInfo>();
			AtaxxChangeInfo changeInfo = GameEngine.applyMove(this.state, move, animations);
			this.updateGameState(changeInfo);

			if (changeInfo.type() == ChangeType.GAME_MOVE) {
				final AtaxxState newState = changeInfo.endState();
				if (!newState.currentPlayer().isHuman()) {
					PerformAiMoveTask task = new PerformAiMoveTask(this, newState, this.gameSetup.getAiType().value());
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
	private final int depth;

	public PerformAiMoveTask(GameHub gh, AtaxxState newState, int depth) {
		super();
		this.gameHub = gh;
		this.state = newState;
		this.depth = depth;
	}

	@Override
	public void run() {
		ComputeAiMove moveFinder = new ComputeAiMove(state, depth, true);
		final GameMove move = moveFinder.call();

		boolean humanAlive = false;
		Scores score = state.computeScores();
		for (Player p : state.players()) {
			if (p.isHuman()) {
				if (score.count(p.tile()) > 0) {
					humanAlive = true;
				}
			}
		}

		try {
			if (!humanAlive) {
				TimeUnit.MILLISECONDS.sleep(250);
			}
			else if (move == GameMove.PASS) {
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
