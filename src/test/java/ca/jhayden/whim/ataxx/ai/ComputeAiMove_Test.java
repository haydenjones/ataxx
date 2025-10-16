package ca.jhayden.whim.ataxx.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ca.jhayden.whim.ataxx.model.AtaxxBoard;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Tile;

public class ComputeAiMove_Test {
	final List<Player> players = Arrays.asList(new Player(Tile.PIECE_1, true, Color.RED, "Red"),
			new Player(Tile.PIECE_2, false, Color.BLUE, "Blue"));

	@Test
	public void worksDepth1() {
		String rawBoard = """
				1.....2
				.1.....
				1......
				.......
				.......
				.....1.
				222...1""";

		Player next = players.get(1);

		AtaxxState state = new AtaxxState(players, AtaxxBoard.of(rawBoard), next);
		ComputeAiMove ai = new ComputeAiMove(state, 1, false);

		GameMove chosenMove = ai.call();

		assertEquals("GameMove[tile=PIECE_2, start=Pos[row=6, col=2], move=UURR]", chosenMove.toString());
	}
}
