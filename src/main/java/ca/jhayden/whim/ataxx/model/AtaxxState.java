package ca.jhayden.whim.ataxx.model;

import java.util.List;

public record AtaxxState(List<Player> players, AtaxxBoard board, int currentPlayerIndex) {
    
}
