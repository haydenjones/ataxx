package ca.jhayden.whim.ataxx.swing;

import java.util.List;

import ca.jhayden.whim.ataxx.model.AnimateInfo;
import ca.jhayden.whim.ataxx.model.AtaxxChangeInfo;

// I want to change this so so it works with animations instead.
public interface AtaxxGui {
	public abstract void update(AtaxxChangeInfo changeInfo, List<AnimateInfo> animations);
}
