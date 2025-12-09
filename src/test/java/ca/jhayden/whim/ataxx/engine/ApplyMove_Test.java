package ca.jhayden.whim.ataxx.engine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ca.jhayden.whim.ataxx.model.AtaxxBoard;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.MoveType;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Pos;
import ca.jhayden.whim.ataxx.model.Tile;

public class ApplyMove_Test {
	@Test
	public void works() {
		List<Player> players = new ArrayList<>();
		final Player player = new Player(Tile.PIECE_1, true, Color.RED, "Red");
		players.add(player);
		players.add(new Player(Tile.PIECE_2, false, Color.BLUE, "Blue"));

		AtaxxBoard board = AtaxxBoard.of("""
				2222222
				1111111
				1111111
				111.111
				1111111
				1111111
				11111.2""");

		AtaxxState state = new AtaxxState(players, board, player);

		GameMove move = new GameMove(Tile.PIECE_1, new Pos(6, 4), MoveType.R);

		AtaxxState actual = GameEngine.applyMove(state, move);
	}
}
