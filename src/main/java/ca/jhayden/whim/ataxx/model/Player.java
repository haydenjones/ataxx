package ca.jhayden.whim.ataxx.model;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public record Player(Tile tile, boolean isHuman, Color color, String name) {
	public static final List<Player> EMPTY_LIST = Collections.emptyList();
}
