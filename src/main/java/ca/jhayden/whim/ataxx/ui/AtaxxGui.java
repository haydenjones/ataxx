package ca.jhayden.whim.ataxx.ui;

import java.util.List;

import ca.jhayden.whim.ataxx.model.AnimateInfo;

// I want to change this so so it works with animations instead.
public interface AtaxxGui {
	public abstract void update(List<AnimateInfo> animations);
}
