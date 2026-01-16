package ca.jhayden.whim.ataxx.engine;

import java.io.Serializable;

public interface GameSetup extends Serializable {
	public enum NumberOfPlayers {
		TWO(2), //
		FOUR(4);

		private final int value;

		NumberOfPlayers(int v) {
			value = v;
		}

		public int value() {
			return value;
		}
	}

	public enum AiType {
		DEPTH_1(1), //
		DEPTH_2(4);

		private final int value;

		AiType(int v) {
			value = v;
		}

		public int value() {
			return value;
		}
	}

	public abstract boolean isPlayerOrderRandom();
	
	public abstract NumberOfPlayers getNumberOfPlayers();

	public abstract AiType getAiType();

}
