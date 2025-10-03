package ca.jhayden.whim.ataxx.swing;

import ca.jhayden.whim.ataxx.engine.GameSetup;
import ca.jhayden.whim.ataxx.model.GameMove;

public interface GameHub {

	public abstract void startNewGame(GameSetup setup);

	public abstract void move(GameMove possibleMove, Object from);

}
