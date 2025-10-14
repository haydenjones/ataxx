package ca.jhayden.whim.ataxx.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import ca.jhayden.whim.ataxx.engine.GameSetupType;
import ca.jhayden.whim.ataxx.ui.GameHub;

public class AtaxxSetupFx {
    @FXML private Button twoPlayerButton;
    @FXML private Button fourPlayerButton;
    
    private final GameHub gameHub;

    public AtaxxSetupFx(GameHub hub) {
        this.gameHub = hub;
    }

    @FXML
    private void startTwoPlayerGame() {
        gameHub.startNewGame(GameSetupType.TWO_PLAYER_SIMPLE);
    }

    @FXML
    private void startFourPlayerGame() {
        gameHub.startNewGame(GameSetupType.FOUR_PLAYER_SIMPLE);
    }
}