package aga.mahjong;

import aga.mahjong.core.*;

public class BoardController
{
	private BoardView _view;
	private boolean _hintMode;
	private java.util.Stack<Cell> _undoStack;

	private Board getBoard()
	{
		return _view.getBoard();
	}
	private void setBoard(Board value)
	{
		_view.setBoard(value);
	}

	public BoardController(BoardView view)
	{
		_view = view;
		_undoStack = new java.util.Stack<Cell>();
	}

	/*public void ClickTile(Position position)
	{
		if (_hintMode)
		{
			getBoard().Selection.Clear();
			_hintMode = false;
		}

		if (getBoard().Selection.Contains(position) || !getBoard().IsFree(position))
		{
			return;
		}

		if (getBoard().Selection.size() > 0)
		{
			if (getBoard()[getBoard().Selection[0]].Match(getBoard()[position]))
			{
				RemovePair(position, getBoard().Selection[0]);
			}
			else
			{
				getBoard().Selection.Clear();
				getBoard().Selection.Add(position);
			}
		}
		else
		{
			getBoard().Selection.Add(position);
		}
		_view.UpdateView();
		CheckGame();
	}

	private void RemovePair(Position p1, Position p2)
	{
		_undoStack.push(new Cell(p1, getBoard()[p1]));
		_undoStack.push(new Cell(p2, getBoard()[p2]));
		getBoard()[p1] = null;
		getBoard()[p2] = null;
		getBoard().Selection.Clear();
	}

	private void CheckGame()
	{
		if (getBoard().TilesCount == 0)
		{
			JOptionPane.showConfirmDialog(null, "You won!", "Pocket Mahjong", JOptionPane.DEFAULT_OPTION);
		}
		else if (getBoard().PayersCount == 0)
		{
			JOptionPane.showConfirmDialog(null, "Game over.", "Pocket Mahjong", JOptionPane.DEFAULT_OPTION);
		}
	}

	public void Undo()
	{
		if (_undoStack.size() > 0)
		{
			getBoard().Selection.Clear();
			RestoreCell();
			RestoreCell();
			_view.UpdateView();
		}
	}

	private void RestoreCell()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
		var c = _undoStack.pop();
		getBoard()[c.Position] = c.Tile;
	}

	public void StartNewGame()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
		var lay = LayoutProvider.getLayout(Config.Instance.Layout);
		IArrangeStrategy ar;
		if (Config.Instance.IsRandom)
		{
			ar = new RandomArrange();
		}
		else
		{
			ar = new SolvableArrange();
		}
		setBoard(new Board(lay, ar));
		_view.UpdateView();
	}

	public void Restart()
	{
		getBoard().Restart();
		_view.UpdateView();
	}

	public void ShowHint()
	{
		_hintMode = true;
		getBoard().Selection.Clear();
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
		for (var pair : getBoard().GetPairs())
		{
			getBoard().Selection.Add(pair.Position1);
			getBoard().Selection.Add(pair.Position2);
		}
		_view.UpdateView();
	}*/
}