package ca.jhayden.whim.ataxx.swing;

import java.awt.FlowLayout;
import java.util.EnumMap;

import javax.swing.JPanel;

import ca.jhayden.whim.ataxx.model.AtaxxChangeInfo;
import ca.jhayden.whim.ataxx.model.ChangeType;
import ca.jhayden.whim.ataxx.model.Player;
import ca.jhayden.whim.ataxx.model.Tile;

public class ScoreJPanel extends JPanel implements AtaxxGui {
	private static final long serialVersionUID = 8710836157609000120L;

	private EnumMap<Tile, ScoreSingleJPanel> map = new EnumMap<>(Tile.class);

	public ScoreJPanel() {
		super(new FlowLayout());
	}

	@Override
	public void update(AtaxxChangeInfo changeInfo) {
		if (changeInfo.type() == ChangeType.START_NEW_GAME) {
			for (ScoreSingleJPanel p : map.values()) {
				this.remove(p);
			}

			map.clear();

			for (Player p : changeInfo.endState().players()) {
				ScoreSingleJPanel panel = new ScoreSingleJPanel(p);
				this.add(panel);
				map.put(p.tile(), panel);
			}
		}

		for (Player p : changeInfo.endState().players()) {
			ScoreSingleJPanel panel = map.get(p.tile());
			panel.update(changeInfo);
		}
	}
}
