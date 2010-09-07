package aga.mahjong;

import PocketMahjong.Core.*;

public class LayoutView extends UserControl
{
	public LayoutView()
	{
		InitializeComponent();
	}

	private Board _board;
	private Layout _layout;
	public Layout getLayout()
	{
		return _layout;
	}
	public void setLayout(Layout value)
	{
		_layout = value;
		if (value != null)
		{
			_board = new Board(_layout, new RandomArrange());
		}
		else
		{
			_board = null;
		}
		Invalidate();
	}

	private Brush TileBrush = new SolidBrush(Color.White);
	private Pen TilePen = new Pen(Color.Blue, 1);
	@Override
	protected void OnPaint(PaintEventArgs e)
	{
		e.Graphics.Clear(Color.White);
		if (_board != null)
		{
			int CellWidth = 8;
			int CellHeight = 16;
			int sx = (int)((Width - Math.ceil(_board.ColumnCount / 2.0) * CellWidth) / 2);
			int sy = (int)((Height - Math.ceil(_board.RowCount / 2.0) * CellHeight) / 2);

			for (int lay = 0; lay < _board.LayerCount; lay++)
			{
				for (int col = _board.ColumnCount; col >= 0; col--)
				{
					for (int row = 0; row < _board.RowCount; row++)
					{
						Position pos = new Position(lay, row, col);
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
						var tile = _board[lay][row][col];
						if (tile != null)
						{
							int x = sx + pos.Column * CellWidth / 2 + pos.Layer * 2;
							int y = sy + pos.Row * CellHeight / 2 - pos.Layer * 2;
							Rectangle rect = new Rectangle(x, y, CellWidth, CellHeight);
							e.Graphics.FillRectangle(TileBrush, rect);
							e.Graphics.DrawRectangle(TilePen, rect);
						}
					}
				}
			}
		}
	}

	@Override
	protected void OnPaintBackground(PaintEventArgs e)
	{
	}
}