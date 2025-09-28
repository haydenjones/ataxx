package ca.jhayden.whim.ataxx.model;

import java.util.Collections;
import java.util.List;

public record Player(Tile tile, boolean isHuman) {
	public static final List<Player> EMPTY_LIST = Collections.emptyList();
}
