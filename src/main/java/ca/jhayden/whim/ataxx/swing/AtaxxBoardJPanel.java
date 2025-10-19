package ca.jhayden.whim.ataxx.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JPanel;

import ca.jhayden.whim.ataxx.model.AnimateInfo;
import ca.jhayden.whim.ataxx.model.AtaxxChangeInfo;
import ca.jhayden.whim.ataxx.model.AtaxxRow;
import ca.jhayden.whim.ataxx.model.AtaxxState;
import ca.jhayden.whim.ataxx.model.GameMove;
import ca.jhayden.whim.ataxx.model.Pos;
import ca.jhayden.whim.ataxx.model.Tile;
import ca.jhayden.whim.ataxx.ui.GameHub;

public class AtaxxBoardJPanel extends JPanel implements AtaxxGui, MouseMotionListener, MouseListener {
	private static final int SQUARE_LENGTH = 60;
	private static final int SQUARE_GAP = 4;

	private static final long serialVersionUID = -6758845382471013594L;
	private final GameHub gameHub;
	private final Tile playerTile;

	private AtaxxState state = AtaxxState.NULL;

	private Pos startPos = null;
	private Pos cursorPos = null;

	public AtaxxBoardJPanel(GameHub hub, Tile myPlayerTile) {
		super();
		gameHub = hub;
		playerTile = myPlayerTile;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(SQUARE_LENGTH * 7, SQUARE_LENGTH * 7));
	}

	void fillRect(Graphics2D g2d, Pos pos, Color c) {
		g2d.setColor(c);
		g2d.fillRect(SQUARE_LENGTH * pos.col() + SQUARE_GAP, SQUARE_LENGTH * pos.row() + SQUARE_GAP,
				SQUARE_LENGTH - SQUARE_GAP - SQUARE_GAP, SQUARE_LENGTH - SQUARE_GAP - SQUARE_GAP);
	}

	void drawRect(Graphics2D g2d, Pos pos, Color c) {
		g2d.setColor(c);
		g2d.drawRect(SQUARE_LENGTH * pos.col(), SQUARE_LENGTH * pos.row(), SQUARE_LENGTH, SQUARE_LENGTH);
		g2d.drawRect(SQUARE_LENGTH * pos.col() + 1, SQUARE_LENGTH * pos.row() + 1, SQUARE_LENGTH - 2,
				SQUARE_LENGTH - 2);
	}

	void fillOval(Graphics2D g2d, Pos pos, Color c) {
		g2d.setColor(c);
		g2d.fillOval( //
				SQUARE_LENGTH * pos.col() + SQUARE_GAP + SQUARE_GAP, //
				SQUARE_LENGTH * pos.row() + SQUARE_GAP + SQUARE_GAP, //
				SQUARE_LENGTH - SQUARE_GAP - SQUARE_GAP - SQUARE_GAP - SQUARE_GAP, //
				SQUARE_LENGTH - SQUARE_GAP - SQUARE_GAP - SQUARE_GAP - SQUARE_GAP);
	}

	@Override
	public void paint(Graphics g) {
		if (g instanceof Graphics2D g2d) {
			int rowPos = 0;
			for (AtaxxRow row : state.board().rows()) {

				int colPos = 0;
				for (Tile tile : row) {
					g2d.setColor(Color.GRAY);
					g2d.fillRect(SQUARE_LENGTH * colPos, SQUARE_LENGTH * rowPos, SQUARE_LENGTH, SQUARE_LENGTH);

					Pos pos = new Pos(rowPos, colPos);
					if (pos.equals(this.startPos)) {
						this.drawRect(g2d, pos, Color.GREEN);
					}
					else if (pos.equals(this.cursorPos)) {
						this.drawRect(g2d, pos, Color.CYAN);
					}

					if (tile == Tile.WALL) {
						fillRect(g2d, pos, Color.BLACK);
					}
					else {
						fillRect(g2d, pos, Color.WHITE);

						if (tile != null) {
							if (tile.isPiece()) {
								this.fillOval(g2d, pos, AtaxxJFrame.COLOR_MAP.get(tile));
							}
						}
					}

					colPos++;
				}

				rowPos++;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		final Tile cursorTile = this.state.board().at(this.cursorPos);

		if (this.startPos == null) {
			if (cursorTile == this.playerTile) {
				this.startPos = this.cursorPos;
			}
		}
		else if (this.startPos.equals(this.cursorPos)) {
			this.startPos = null;
		}
		else if (cursorTile == Tile.EMPTY) {
			GameMove possibleMove = GameMove.of(this.playerTile, this.startPos, this.cursorPos);
			if (possibleMove != null) {
				this.startPos = null;
				this.gameHub.move(possibleMove, this);
			}
		}

		this.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.cursorPos = new Pos(e.getY() / AtaxxBoardJPanel.SQUARE_LENGTH, e.getX() / AtaxxBoardJPanel.SQUARE_LENGTH);
		this.repaint();
	}

	@Override
	public void update(AtaxxChangeInfo changeInfo, List<AnimateInfo> animations) {
		for (AnimateInfo ai : animations) {
			System.out.println(ai);
		}
		this.state = changeInfo.endState();
		this.repaint();
	}
}
