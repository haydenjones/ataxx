package ca.jhayden.whim.ataxx.model;

import java.util.Collections;
import java.util.List;

public record FromToPos(Pos from, Pos to) {
	public static List<FromToPos> EMPTY_LIST = Collections.emptyList();
}
