package ca.jhayden.whim.ataxx.swing;

import ca.jhayden.whim.ataxx.model.AtaxxChangeInfo;

// I want to change this so so it works with animations instead.
@Deprecated
public interface AtaxxGui {
	public abstract void update(AtaxxChangeInfo changeInfo);
}
