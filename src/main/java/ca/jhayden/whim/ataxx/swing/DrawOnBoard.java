package ca.jhayden.whim.ataxx.swing;

import java.awt.Color;
import java.awt.Graphics2D;

import ca.jhayden.whim.ataxx.model.AnimateInfo;
import ca.jhayden.whim.ataxx.model.AnimateInfoType;
import ca.jhayden.whim.ataxx.model.AtaxxBoard;
import ca.jhayden.whim.ataxx.model.AtaxxRow;
import ca.jhayden.whim.ataxx.model.FromToPos;
import ca.jhayden.whim.ataxx.model.Pos;
import ca.jhayden.whim.ataxx.model.Tile;

public class DrawOnBoard {
	static record Rect(int x, int y, int w, int h) {
	}

	Rect computeTileFull(Pos pos) {
		return new Rect(AtaxxBoardJPanel.SQUARE_LENGTH * pos.col(), AtaxxBoardJPanel.SQUARE_LENGTH * pos.row(),
				AtaxxBoardJPanel.SQUARE_LENGTH, AtaxxBoardJPanel.SQUARE_LENGTH);
	}

	Rect computeTileInner(Pos pos) {
		return new Rect( //
				AtaxxBoardJPanel.SQUARE_LENGTH * pos.col() + gap1, //
				AtaxxBoardJPanel.SQUARE_LENGTH * pos.row() + gap1, //
				AtaxxBoardJPanel.SQUARE_LENGTH - gap2, //
				AtaxxBoardJPanel.SQUARE_LENGTH - gap2 //
		);
	}

	Rect computeOval(Pos pos) {
		return new Rect( //
				AtaxxBoardJPanel.SQUARE_LENGTH * pos.col() + gap2, //
				AtaxxBoardJPanel.SQUARE_LENGTH * pos.row() + gap2, //
				AtaxxBoardJPanel.SQUARE_LENGTH - gap4, //
				AtaxxBoardJPanel.SQUARE_LENGTH - gap4 //
		);
	}

	Rect computeOval(FromToPos f2p, float delta) {
		Rect from = computeOval(f2p.from());
		Rect to = computeOval(f2p.to());

		float fx = ((to.x() - from.x()) * delta) + from.x();
		float fy = ((to.y() - from.y()) * delta) + from.y();

		return new Rect( //
				(int) fx, //
				(int) fy, //
				from.w(), //
				from.h() //
		);
	}

	Rect computeOvalSmall(FromToPos f2p, float delta) {
		Rect from = computeOvalSmall(f2p.from());
		Rect to = computeOvalSmall(f2p.to());

		float fx = ((to.x() - from.x()) * delta) + from.x();
		float fy = ((to.y() - from.y()) * delta) + from.y();

		return new Rect( //
				(int) fx, //
				(int) fy, //
				from.w(), //
				from.h() //
		);
	}

	Rect computeOvalSmall(Pos pos) {
		return new Rect( //
				AtaxxBoardJPanel.SQUARE_LENGTH * pos.col() + gap4, //
				AtaxxBoardJPanel.SQUARE_LENGTH * pos.row() + gap4, //
				AtaxxBoardJPanel.SQUARE_LENGTH - gap8, //
				AtaxxBoardJPanel.SQUARE_LENGTH - gap8 //
		);
	}

	private final AtaxxBoardJPanel panel;

	private final int gap1 = AtaxxBoardJPanel.SQUARE_GAP;
	private final int gap2 = 2 * AtaxxBoardJPanel.SQUARE_GAP;
	private final int gap4 = 4 * AtaxxBoardJPanel.SQUARE_GAP;
	private final int gap8 = 8 * AtaxxBoardJPanel.SQUARE_GAP;

	public DrawOnBoard(AtaxxBoardJPanel p) {
		super();
		panel = p;
	}

	void paint(Graphics2D g2d, AtaxxBoard board, Pos startPos, Pos cursorPos) {
		int rowPos = 0;
		for (AtaxxRow row : board.rows()) {

			int colPos = 0;
			for (Tile tile : row) {

				Color c = Color.GRAY;
				Pos pos = new Pos(rowPos, colPos);
				if (pos.equals(startPos)) {
					c = Color.GREEN;
				}
				else if (pos.equals(cursorPos)) {
					c = Color.CYAN;
				}

				Rect r = this.computeTileFull(pos);
				fillRect(g2d, r, c);

				r = this.computeTileInner(pos);
				if (tile != Tile.WALL) {
					fillRect(g2d, r, Color.WHITE);
				}
				else {
					fillRect(g2d, r, Color.BLACK);
				}

				if (tile.isPiece()) {
					c = AtaxxJFrame.COLOR_MAP.get(tile);
					r = this.computeOval(pos);
					fillOval(g2d, r, c);
				}

				colPos++;
			}

			rowPos++;
		}
	}

	void paint(Graphics2D g2d, float delta, AnimateInfo ca) {
		if ((ca.type() == AnimateInfoType.MAIN_PIECE_GROW) || (ca.type() == AnimateInfoType.MAIN_PIECE_JUMP)) {
			final Color color = AtaxxJFrame.COLOR_MAP.get(ca.tile());
			FromToPos f2p = ca.positions().getFirst();
			Rect r = computeOval(f2p, delta);
			fillOval(g2d, r, color);
		}
		else if (ca.type() == AnimateInfoType.FLIP_SURROUNDING_PIECES) {
			final Color color = AtaxxJFrame.COLOR_MAP.get(ca.tile());
			for (FromToPos f2p : ca.positions()) {
				Rect r = computeOvalSmall(f2p, delta);
				this.fillOval(g2d, r, color);
			}
		}
	}

	void fillRect(Graphics2D g2d, Rect r, Color c) {
		g2d.setColor(c);
		g2d.fillRect(r.x(), r.y(), r.w(), r.h());
	}

	void fillOval(Graphics2D g2d, Rect r, Color c) {
		g2d.setColor(c);
		g2d.fillOval(r.x(), r.y(), r.w(), r.h());
	}
}
