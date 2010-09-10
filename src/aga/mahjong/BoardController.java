package aga.mahjong;

import java.util.Stack;

import aga.mahjong.core.Board;
import aga.mahjong.core.Cell;
import aga.mahjong.core.IArrangeStrategy;
import aga.mahjong.core.Layout;
import aga.mahjong.core.Pair;
import aga.mahjong.core.Position;
import aga.mahjong.core.RandomArrange;
import aga.mahjong.core.Tile;

public class BoardController {
	private BoardView view;
	private boolean hintMode;
	private Stack<Cell> undoStack;

	public BoardController(BoardView view) {
		this.view = view;
		undoStack = new Stack<Cell>();
	}

	private Board getBoard() {
		return view.getBoard();
	}

	private void setBoard(Board value) {
		view.setBoard(value);
	}

	public void clickTile(Position position) {
		if (hintMode) {
			getBoard().getSelection().clear();
			hintMode = false;
		}

		if (getBoard().getSelection().contains(position) || !getBoard().IsFree(position))
			return;

		if (getBoard().getSelection().size() > 0) {
			Tile t1 = getBoard().getItem(getBoard().getSelection().get(0));
			Tile t2 = getBoard().getItem(position);
			if (Tile.isMatch(t1, t2)) {
				removePair(position, getBoard().getSelection().get(0));
			} else {
				getBoard().getSelection().clear();
				getBoard().getSelection().add(position);
			}
		} else {
			getBoard().getSelection().add(position);
		}
		
		view.update();
		checkGame();
	}

	private void removePair(Position p1, Position p2) {
		undoStack.push(new Cell(p1, getBoard().getItem(p1)));
		undoStack.push(new Cell(p2, getBoard().getItem(p2)));
		getBoard().setItem(p1, null);
		getBoard().setItem(p2, null);
		getBoard().getSelection().clear();
	}

	private void checkGame() {
		if (getBoard().getTilesCount() == 0)
			view.showDialog(null, "You won!");
		else if (getBoard().getPayersCount() == 0)
			view.showDialog(null, "Game over.");
	}

	public void undo() {
		if (undoStack.size() > 0) {
			getBoard().getSelection().clear();
			restoreCell();
			restoreCell();
			view.update();
		}
	}

	private void restoreCell() {
		Cell c = undoStack.pop();
		getBoard().setItem(c.getPosition(), c.getTile());
	}

	public void startNewGame() {
		Layout layout = LayoutProvider.getLayout(Config.getInstance().getLayout());
		IArrangeStrategy ar = new RandomArrange();
		/*if (Config.getInstance().getIsRandom()) {
			ar = new RandomArrange();
		} else {
			ar = new RandomArrange();
		}*/
		setBoard(new Board(layout, ar));
	}

	public void restart() {
		getBoard().Restart();
		view.update();
	}

	public void showHint() {
		hintMode = true;
		getBoard().getSelection().clear();
		for (Pair pair : getBoard().GetPairs()) {
			getBoard().getSelection().add(pair.getPosition1());
			getBoard().getSelection().add(pair.getPosition2());
		}
		view.update();
	}
}