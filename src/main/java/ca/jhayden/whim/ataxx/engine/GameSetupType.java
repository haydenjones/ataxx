package ca.jhayden.whim.ataxx.engine;

public enum GameSetupType implements GameSetup {

	TWO_PLAYER_SIMPLE(NumberOfPlayers.TWO, AiType.DEPTH_1), //
	TWO_PLAYER_SMART(NumberOfPlayers.TWO, AiType.DEPTH_2), //
	FOUR_PLAYER_SIMPLE(NumberOfPlayers.FOUR, AiType.DEPTH_1), //
	FOUR_PLAYER_SMART(NumberOfPlayers.FOUR, AiType.DEPTH_2);

	private final boolean playerOrderRandom;
	private final NumberOfPlayers numberOfPlayers;
	private final AiType aiType;

	GameSetupType(NumberOfPlayers nop, AiType ai) {
		numberOfPlayers = nop;
		aiType = ai;
		playerOrderRandom = true;
	}

	@Override
	public boolean isPlayerOrderRandom()  {
		return playerOrderRandom;
	}

	@Override
	public NumberOfPlayers getNumberOfPlayers() {
		return numberOfPlayers;
	}

	@Override
	public AiType getAiType() {
		return aiType;
	}
}
