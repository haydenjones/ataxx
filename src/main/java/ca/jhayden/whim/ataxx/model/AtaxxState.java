package ca.jhayden.whim.ataxx.model;

import java.util.List;

public record AtaxxState(List<Player> players, AtaxxBoard board, Player currentPlayer) {
    public AtaxxState(List<Player> players, AtaxxBoard board)  {
        this(players, board, players.getFirst());
    }    
}
