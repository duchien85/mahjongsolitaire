package aga.mahjong;

import aga.mahjong.core.*;

public class BoardView
{
//C# TO JAVA CONVERTER WARNING: Java does not allow user-defined value types. The behavior of this class will differ from the original:
//ORIGINAL LINE: private struct PositionInfo
	private final static class PositionInfo
	{
		private Position _position;
		public Position getPosition()
		{
			return _position;
		}
		public void setPosition(Position value)
		{
			_position = value;
		}

		private Rectangle _bounds;
		public Rectangle getBounds()
		{
			return _bounds;
		}
		public void setBounds(Rectangle value)
		{
			_bounds = value;
		}

		public PositionInfo(Position pos, Rectangle rect)
		{
			_position = pos;
			_bounds = rect;
		}

		public PositionInfo clone()
		{
			PositionInfo varCopy = new PositionInfo();

			varCopy._position = this._position;
			varCopy._bounds = this._bounds;

			return varCopy;
		}
	}

	private static final int CellWidth = 32;
	private static final int CellHeight = 45;
	private static final int TileWidth = 36;
	private static final int TileHeight = 49;
	private static final int StatusBarHeight = 40;

	private final Brush LightGreenBrush = new SolidBrush(Color.LightGreen);
	private final Brush BlackBrush = new SolidBrush(Color.Black);

	private Stopwatch _timer = new Stopwatch();
	private Bitmap _boardImage;
	private Graphics _gr;
	private java.util.HashMap<Tile, Bitmap> _tiles;
	private java.util.HashMap<Tile, Bitmap> _tilesSelected;
	private Bitmap _tile1_top, _tile1_bottom;
	private Bitmap _tile2_top, _tile2_bottom;
	private ImageAttributes _tileAttr;
	private java.util.ArrayList<PositionInfo> _bounds = new java.util.ArrayList<PositionInfo>();

	private Board _board;
	public Board getBoard()
	{
		return _board;
	}
	public void setBoard(Board value)
	{
		_board = value;
		Draw();
		Invalidate();
	}

	private BoardController _controller;
	public BoardController getController()
	{
		return _controller;
	}

	public BoardView()
	{
		InitializeComponent();
		LoadImages();
		_controller = new BoardController(this);
	}

	@Override
	protected void OnResize(EventArgs e)
	{
		_boardImage = null;
		Draw();
		super.OnResize(e);
	}

	private void LoadImages()
	{
		_tiles = new java.util.HashMap<Tile, Bitmap>();
		_tilesSelected = new java.util.HashMap<Tile, Bitmap>();
		_tile1_top = Properties.Resources.Tile1_1;
		_tile1_bottom = Properties.Resources.Tile1_3;
		_tile2_top = Properties.Resources.Tile2_1;
		_tile2_bottom = Properties.Resources.Tile2_3;
		for (Tile t : TileSet.GetAllTiles())
		{
			AddTile(t, _tiles, Properties.Resources.Tile1_2);
			AddTile(t, _tilesSelected, Properties.Resources.Tile2_2);
		}

		_tileAttr = new ImageAttributes();
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
		var tc = _tile1_top.GetPixel(0, 0);
		_tileAttr.SetColorKey(tc, tc);
	}

	private void AddTile(Tile t, java.util.HashMap<Tile, Bitmap> collection, Bitmap tile)
	{
		if (!collection.containsKey(t))
		{
			Assembly asm = Assembly.GetExecutingAssembly();
			String ns = "PocketMahjong.Resources.";
			Bitmap face = new Bitmap(asm.GetManifestResourceStream(ns + String.format("%1$s%2$s.png", t.Kind, t.Number)));
//C# TO JAVA CONVERTER NOTE: The following 'using' block is replaced by its Java equivalent:
//			using (Graphics gr = Graphics.FromImage(tile))
			Graphics gr = Graphics.FromImage(tile);
			try
			{
				gr.DrawImage(face, (tile.getWidth() - face.getWidth()) / 2 + 2, (tile.getHeight() - face.getHeight()) / 2);
			}
			finally
			{
				gr.dispose();
			}
			collection.put(t, tile);
		}
	}

	public Iterable<Cell> getDrawList()
	{
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
//C# TO JAVA CONVERTER TODO TASK: Java does not have an equivalent to the C# 'yield' keyword:
						yield return new Cell(pos, tile);
					}
				}
			}
		}

	}

	private void Draw()
	{
		if (ClientRectangle.getWidth() <= 0 || ClientRectangle.getHeight() <= 0)
		{
			return;
		}
		if (_boardImage == null)
		{
			_boardImage = new Bitmap(ClientRectangle.getWidth(), ClientRectangle.getHeight());
			_gr = Graphics.FromImage(_boardImage);
		}

		_bounds.clear();
		_gr.Clear(Color.DarkGreen);
		if (_board == null)
		{
			return;
		}

		DrawTiles();
		DrawStatus();
	}

	private void DrawStatus()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
		var str = String.format("Tiles: %1$s   Pairs: %2$s", _board.TilesCount, _board.PayersCount);
		Rectangle rect = new Rectangle(0, Height - StatusBarHeight, Width, StatusBarHeight);
		_gr.FillRectangle(LightGreenBrush, rect);
		_gr.DrawString(str, Font, BlackBrush, 0, rect.Y);
	}

	private void DrawTiles()
	{
		int sx = (int)((_boardImage.getWidth() - Math.ceil(_board.ColumnCount / 2.0) * CellWidth) / 2);
		int sy = (int)((_boardImage.getHeight() - StatusBarHeight - Math.ceil(_board.RowCount / 2.0) * CellHeight) / 2);
		for (Cell cell : getDrawList())
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
			var pos = cell.Position;
			Image face, top, bottom;
			if (_board.Selection.Contains(pos))
			{
				face = _tilesSelected.get(cell.Tile);
				top = _tile2_top;
				bottom = _tile2_bottom;
			}
			else
			{
				face = _tiles.get(cell.Tile);
				top = _tile1_top;
				bottom = _tile1_bottom;
			}
			int x = sx + pos.Column * CellWidth / 2 + pos.Layer * 4;
			int y = sy + pos.Row * CellHeight / 2 - pos.Layer * 4;

			DrawImageTransparent(top, x, y);
			_gr.DrawImage(face, x, y + top.getHeight());
			DrawImageTransparent(bottom, x, y + top.getHeight() + face.getHeight());

			_bounds.add(new PositionInfo(cell.Position, new Rectangle(x, y, TileWidth, TileHeight)));
		}
	}

	private void DrawImageTransparent(Image image, int x, int y)
	{
		Rectangle rect = new Rectangle(x, y, image.getWidth(), image.getHeight());
		_gr.DrawImage(image, rect, 0, 0, rect.getWidth(), rect.getHeight(), GraphicsUnit.Pixel, _tileAttr);
	}

	@Override
	protected void OnMouseDown(MouseEventArgs e)
	{
		Point pt = new Point(e.X, e.Y);
		for (int i = _bounds.size() - 1; i >= 0; i--)
		{
			if (_bounds.get(i).getBounds().Contains(pt))
			{
				_controller.ClickTile(_bounds.get(i).getPosition());
				break;
			}
		}
	}

	@Override
	protected void OnPaintBackground(PaintEventArgs e)
	{
	}

	@Override
	protected void OnPaint(PaintEventArgs e)
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			//#if DEBUG
		_timer.Reset();
		_timer.Start();
			//#endif

		Draw();
		if (_boardImage != null)
		{
			e.Graphics.DrawImage(_boardImage, 0, 0);
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			//#if DEBUG
		_timer.Stop();
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
		var res = _timer.ElapsedMilliseconds;
		e.Graphics.DrawString(res.toString(), Font, BlackBrush, 0, 0);
			//#endif
	}

	public void UpdateView()
	{
		Invalidate();
	}
}