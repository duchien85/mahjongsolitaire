package aga.mahjong;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import aga.mahjong.core.Board;
import aga.mahjong.core.Cell;
import aga.mahjong.core.Position;
import aga.mahjong.core.Tile;
import aga.mahjong.core.TileSet;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View {

	private final static class PositionInfo {
		private Position _position;
		private Rect _bounds;

		public Position getPosition() {
			return _position;
		}

		public void setPosition(Position value) {
			_position = value;
		}

		public Rect getBounds() {
			return _bounds;
		}

		public void setBounds(Rect value) {
			_bounds = value;
		}

		public PositionInfo(Position pos, Rect rect) {
			_position = pos;
			_bounds = rect;
		}

		public PositionInfo clone() {
			return new PositionInfo(_position, _bounds);
		}
	}

	private static final int CellWidth = 32;
	private static final int CellHeight = 45;
	private static final int TileWidth = 36;
	private static final int TileHeight = 49;
	private static final int FaceWidth = 24;
	private static final int FaceHeight = 38;
	private static final int StatusBarHeight = 40;

	private java.util.ArrayList<PositionInfo> _bounds = new java.util.ArrayList<PositionInfo>();

	private Bitmap mBoardBitmap;
	private Canvas mBoardCanvas;
	private final Paint mSuitPaint = new Paint();

	private HashMap<Tile, Bitmap> _tiles;
	private HashMap<Tile, Bitmap> _tilesSelected;
	private Drawable _tile1_top, _tile1_bottom;
	private Drawable _tile2_top, _tile2_bottom;

	private int mScreenWidth;
	private int mScreenHeight;

	private BoardController controller;
	private Board board;

	public BoardView(Context context) {
		super(context);
		controller = new BoardController(this);

		setFocusable(true);
		setFocusableInTouchMode(true);
		
		SetScreenSize(480, 295);
		LoadImages();
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board value) {
		board = value;
	}

	public BoardController getController() {
		return controller;
	}

	public int GetWidth() {
		return mScreenWidth;
	}

	public int GetHeight() {
		return mScreenHeight;
	}

	public Canvas GetBoardCanvas() {
		return mBoardCanvas;
	}

	public void SetScreenSize(int width, int height) {
		mScreenWidth = width;
		mScreenHeight = height;
		mBoardBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		mBoardCanvas = new Canvas(mBoardBitmap);
	}

	private void LoadImages() {
		Resources r = getContext().getResources();

		_tiles = new java.util.HashMap<Tile, Bitmap>();
		_tilesSelected = new java.util.HashMap<Tile, Bitmap>();
		_tile1_top = r.getDrawable(R.drawable.tile1_1);
		_tile1_bottom = r.getDrawable(R.drawable.tile1_3);
		_tile2_top = r.getDrawable(R.drawable.tile2_1);
		_tile2_bottom = r.getDrawable(R.drawable.tile2_3);
		
		for (Tile t : TileSet.getAllTiles()) {
			try {
				String name = String.format("%1$s%2$s", t.getKind(), t.getNumber());
				Field field = R.drawable.class.getField(name.toLowerCase());
				Drawable face = r.getDrawable(field.getInt(t));
				AddTile(t, _tiles, r.getDrawable(R.drawable.tile1_2), face);
				AddTile(t, _tilesSelected, r.getDrawable(R.drawable.tile2_2), face);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}

		/*drawable = r.getDrawable(R.drawable.redking);
		redKing = Bitmap.createBitmap(faceWidth, faceHeight,
				Bitmap.Config.ARGB_4444);
		canvas = new Canvas(redKing);
		drawable.setBounds(0, 0, faceWidth, faceHeight);
		drawable.draw(canvas);*/
	}

	private void AddTile(Tile t, java.util.HashMap<Tile, Bitmap> collection, Drawable tile, Drawable face ) {
		if (!collection.containsKey(t)) {
			Bitmap bmp = Bitmap.createBitmap(TileWidth, TileHeight, Bitmap.Config.ARGB_4444);
			Canvas gr = new Canvas(bmp);
			tile.draw(gr);
			
			face.setBounds((bmp.getWidth() - FaceWidth) / 2 + 2, 
					(bmp.getHeight() - FaceHeight) / 2, FaceWidth, FaceHeight);
			face.draw(gr);
			
			collection.put(t, bmp);
		}
	}

	public Iterable<Cell> getDrawList() {
		ArrayList<Cell> res = new ArrayList<Cell>();

		for (int lay = 0; lay < board.getLayerCount(); lay++) {
			for (int col = board.getColumnCount(); col >= 0; col--) {
				for (int row = 0; row < board.getRowCount(); row++) {
					Position pos = new Position(lay, row, col);
					Tile tile = board.getItem(lay, row, col);
					if (tile != null) {
						res.add(new Cell(pos, tile));
					}
				}
			}
		}

		return res;
	}

	private void DrawTiles() {
		int sx = (int) ((mScreenWidth - Math.ceil(board.getColumnCount() / 2.0) * CellWidth) / 2);
		int sy = (int) ((mScreenHeight - StatusBarHeight - Math.ceil(board.getRowCount() / 2.0) * CellHeight) / 2);
		for (Cell cell : getDrawList()) {
			Position pos = cell.getPosition();
			Bitmap face;
			Drawable top, bottom;
			if (board.getSelection().contains(pos)) {
				face = _tilesSelected.get(cell.getTile());
				top = _tile2_top;
				bottom = _tile2_bottom;
			} else {
				face = _tiles.get(cell.getTile());
				top = _tile1_top;
				bottom = _tile1_bottom;
			}
			int x = sx + pos.getColumn() * CellWidth / 2 + pos.getLayer() * 4;
			int y = sy + pos.getRow() * CellHeight / 2 - pos.getLayer() * 4;

			drawDrawable(top, x, y, 36, 3);
			mBoardCanvas.drawBitmap(face, x, y + top.getBounds().height(), mSuitPaint);
			drawDrawable(bottom, x, y + 3 + FaceHeight, 36, 3);

			_bounds.add(new PositionInfo(cell.getPosition(), new Rect(x, y,	TileWidth, TileHeight)));
		}
	}
	
	private void drawDrawable(Drawable d, int x, int y, int w, int h) {
		d.setBounds(x, y, w, h);
		d.draw(mBoardCanvas);
	}

	/*
	 * private void DrawStatus() { var str =
	 * String.format("Tiles: %1$s   Pairs: %2$s", _board.TilesCount,
	 * _board.PayersCount); Rectangle rect = new Rectangle(0, Height -
	 * StatusBarHeight, Width, StatusBarHeight);
	 * _gr.FillRectangle(LightGreenBrush, rect); _gr.DrawString(str, Font,
	 * BlackBrush, 0, rect.Y); }a
	 */
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		SetScreenSize(w, h);
		// mRules.Resize(w, h);
		// mSelectCard.SetHeight(h);
	}

	@Override
	public void onDraw(Canvas canvas) {
		/*if (_boardImage == null) {
			_boardImage = new Bitmap(ClientRectangle.getWidth(),
					ClientRectangle.getHeight());
			_gr = Graphics.FromImage(_boardImage);
		}*/
		drawBoard();
	    canvas.drawBitmap(mBoardBitmap, 0, 0, mSuitPaint);
	}

	private void drawBoard() {
		_bounds.clear();
		mBoardCanvas.drawColor(Color.GREEN);
		if (board == null) {
			return;
		}

		DrawTiles();
		//DrawStatus();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	/*
	 * @Override protected void OnMouseDown(MouseEventArgs e) { Point pt = new
	 * Point(e.X, e.Y); for (int i = _bounds.size() - 1; i >= 0; i--) { if
	 * (_bounds.get(i).getBounds().Contains(pt)) {
	 * _controller.ClickTile(_bounds.get(i).getPosition()); break; } } }
	 * 
	 * @Override protected void OnPaint(PaintEventArgs e) { Draw(); if
	 * (_boardImage != null) { e.Graphics.DrawImage(_boardImage, 0, 0); } var
	 * res = _timer.ElapsedMilliseconds; e.Graphics.DrawString(res.toString(),
	 * Font, BlackBrush, 0, 0); }
	 */
}