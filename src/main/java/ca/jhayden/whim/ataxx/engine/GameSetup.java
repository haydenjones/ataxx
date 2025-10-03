package ca.jhayden.whim.ataxx.engine;

public interface GameSetup {
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
		DEPTH_2(2);

		private final int value;

		AiType(int v) {
			value = v;
		}

		public int value() {
			return value;
		}
	}

	public abstract NumberOfPlayers getNumberOfPlayers();

	public abstract AiType getAiType();

}
