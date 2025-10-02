package ca.jhayden.whim.ataxx.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ca.jhayden.whim.ataxx.model.AtaxxBoard;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Tile;

public class ComputeAiMove2_Test {
	final List<Player> players = Arrays.asList(new Player(Tile.PIECE_1, true), new Player(Tile.PIECE_2, false));

	@Test
	public void worksDepth2() {
		String rawBoard = """
				..1..22
				.1.....""";

		Player next = players.get(1);

		AtaxxState state = new AtaxxState(players, AtaxxBoard.of(rawBoard), next);
		ComputeAiMove ai = new ComputeAiMove(state, 2, false);

		GameMove chosenMove = ai.call();
		System.out.println(chosenMove);
		assertEquals("GameMove[tile=PIECE_2, start=Pos[row=0, col=5], move=DR]", chosenMove.toString());
	}
}
