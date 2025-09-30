package ca.jhayden.whim.ataxx.swing;

import ca.jhayden.whim.ataxx.model.GameMove;

public interface GameHub {

	public abstract void startNewGame(int numberOfPlayers);

	public abstract void move(GameMove possibleMove, Object from);

}
