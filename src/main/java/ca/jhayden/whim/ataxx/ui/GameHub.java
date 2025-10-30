package ca.jhayden.whim.ataxx.ui;

import ca.jhayden.whim.ataxx.engine.GameSetup;
import ca.jhayden.whim.ataxx.model.AnimateInfo;
import ca.jhayden.whim.ataxx.model.GameMove;

public interface GameHub {

	public abstract void startNewGame(GameSetup setup);

	public abstract void move(GameMove possibleMove, Object from);

	public abstract void animationIsDone(AnimateInfo info);

}
