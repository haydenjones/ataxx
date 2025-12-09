package ca.jhayden.whim.ataxx.swing;

import java.awt.FlowLayout;
import java.util.EnumMap;

import javax.swing.JPanel;

import ca.jhayden.whim.ataxx.model.AnimateInfo;
import ca.jhayden.whim.ataxx.model.AnimateInfoType;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Tile;
import ca.jhayden.whim.ataxx.ui.DoAnimation;

public class ScoreJPanel extends JPanel implements DoAnimation {
	private static final long serialVersionUID = 8710836157609000120L;

	private EnumMap<Tile, ScoreSingleJPanel> map = new EnumMap<>(Tile.class);

	public ScoreJPanel() {
		super(new FlowLayout());
	}

	@Override
	public void doAnimation(AnimateInfo changeInfo) {
		if (changeInfo.type() == AnimateInfoType.START_NEW_GAME) {
			for (ScoreSingleJPanel p : map.values()) {
				this.remove(p);
			}

			map.clear();

			for (Player p : changeInfo.state().players()) {
				ScoreSingleJPanel panel = new ScoreSingleJPanel(p);
				this.add(panel);
				map.put(p.tile(), panel);
			}
		}

		for (Player p : changeInfo.state().players()) {
			ScoreSingleJPanel panel = map.get(p.tile());
			panel.doAnimation(changeInfo);
		}
	}
}
