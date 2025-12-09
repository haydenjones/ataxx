package ca.jhayden.whim.ataxx.model;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class AnimateInfo {
	public static final List<AnimateInfo> EMPTY_LIST = Collections.emptyList();

	private static final AtomicLong COUNTER = new AtomicLong(0L);

	private final Long id;
	private final AtaxxState state;
	private final AnimateInfoType type;
	private final Tile tile;
	private final List<FromToPos> positions;

	public AnimateInfo(AtaxxState ab, AnimateInfoType y, Tile t, List<FromToPos> pos) {
		id = COUNTER.incrementAndGet();
		state = ab;
		type = y;
		tile = t;
		positions = pos;
	}

	public Long id() {
		return id;
	}

	public AtaxxState state() {
		return state;
	}

	public AnimateInfoType type() {
		return type;
	}

	public Tile tile() {
		return tile;
	}

	public List<FromToPos> positions() {
		return positions;
	}

	@Override
	public String toString() {
		return String.format("#%d [%s]", id, type);
	}
}
