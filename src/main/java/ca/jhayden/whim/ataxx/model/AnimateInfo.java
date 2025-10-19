package ca.jhayden.whim.ataxx.model;

import java.util.Collections;
import java.util.List;

public record AnimateInfo(AtaxxBoard base, AnimateInfoType type, List<FromToPos> positions) {
	public static final List<AnimateInfo> EMPTY_LIST = Collections.emptyList();
}
