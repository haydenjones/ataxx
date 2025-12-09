package ca.jhayden.whim.ataxx.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.SortedSet;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ca.jhayden.whim.ataxx.ai.ComputeAiMove;
import ca.jhayden.whim.ataxx.engine.GameEngine;
import ca.jhayden.whim.ataxx.engine.GameSetup;
import ca.jhayden.whim.ataxx.model.AnimateInfo;
import ca.jhayden.whim.ataxx.model.AnimateInfoType;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.FromToPos;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Scores;
import ca.jhayden.whim.ataxx.model.Tile;
import ca.jhayden.whim.ataxx.ui.AtaxxGui;
import ca.jhayden.whim.ataxx.ui.GameHub;

public class AtaxxJFrame extends JFrame implements GameHub, AtaxxGui {
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
	private final AtaxxSetupGameJPanel setupPanel = new AtaxxSetupGameJPanel(this, "");
	private final AtaxxBoardJPanel gamePanel = new AtaxxBoardJPanel(this, Tile.PIECE_1);

	private GameSetup gameSetup = null;
	private AtaxxState state = null;

	private Vector<AnimateInfo> animations = new Vector<>();

	public AtaxxJFrame() {
		super();

		this.setLayout(new BorderLayout());

		this.getContentPane().add(scorePanel, BorderLayout.NORTH);
		this.getContentPane().add(setupPanel, BorderLayout.CENTER);
	}

	@Override
	public void startNewGame(GameSetup setup) {
		this.gameSetup = setup;
		this.state = GameEngine.newGame(setup);

		for (Player p : this.state.players()) {
			COLOR_MAP.put(p.tile(), p.color());
			COLOR_NAME.put(p.tile(), p.name());
		}

		this.getContentPane().remove(setupPanel);
		this.getContentPane().add(gamePanel, BorderLayout.CENTER);

		AnimateInfo ai = new AnimateInfo(this.state, AnimateInfoType.START_NEW_GAME, Tile.EMPTY, FromToPos.EMPTY_LIST);
		List<AnimateInfo> list = new ArrayList<>();
		list.add(ai);

		this.update(list);
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

	@Override
	public void update(List<AnimateInfo> animations) {
		System.out.println("");
		System.out.println("update:");
		System.out.println(animations);
		this.animations.addAll(animations);
		this.animationIsDone(null);
	}

	@Override
	public void move(final GameMove move, Object from) {
		boolean allow = GameEngine.isValidMove(this.state, move);
		if (allow) {
			final var animations = GameEngine.computeAnimations(this.state, move);
			this.update(animations);
		}
	}

	/**
	 * We just completed the animation given, we should ask my kids to do the next
	 * animation.
	 * Once we are out of animations we should update with the afterAnimations
	 * state.
	 */
	@Override
	public void animationIsDone(AnimateInfo info) {
		System.out.println("");
		System.out.println("animationIsDone: " + info);
		System.out.println(animations);

		if (!animations.isEmpty()) {
			final AnimateInfo next = animations.removeFirst();

			this.state = next.state();

			this.scorePanel.doAnimation(next);
			this.gamePanel.doAnimation(next);
			if (next.type() == AnimateInfoType.START_NEW_GAME) {
				this.pack();
			}
			else if (next.type() == AnimateInfoType.TURN_IS_DONE) {
				if (GameEngine.isGameOver(state)) {
					List<AnimateInfo> list = List
							.of(new AnimateInfo(state, AnimateInfoType.GAME_IS_OVER, Tile.EMPTY, FromToPos.EMPTY_LIST));
					this.update(list);
				}
				else if (!state.currentPlayer().isHuman()) {
					PerformAiMoveTask task = new PerformAiMoveTask(this, state, this.gameSetup.getAiType().value());
					Thread t = new Thread(task);
					t.start();
				}
				else {
					SortedSet<GameMove> humanMoves = GameEngine.computeAllMoves(this.state);
					if (humanMoves.isEmpty()) {
						// Human needs to pass.
						this.move(GameMove.PASS, this);
					}
				}
			}
			else if (next.type() == AnimateInfoType.GAME_IS_OVER) {
				System.out.println("G A M E _ O V E R");
				String message = AtaxxJFrame.computeGameOverMessage(state);
				System.out.println(message);
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
