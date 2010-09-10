package aga.mahjong;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import aga.mahjong.core.Board;
import aga.mahjong.core.Cell;
import aga.mahjong.core.Position;
import aga.mahjong.core.Tile;
import aga.mahjong.core.TileSet;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View {

	private final static class PositionInfo {
		private Position _position;
		private Rect _bounds;

		public Position getPosition() {
			return _position;
		}

		public Rect getBounds() {
			return _bounds;
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
	private static final int StatusBarHeight = 40;

	private java.util.ArrayList<PositionInfo> _bounds = new java.util.ArrayList<PositionInfo>();

	//private Bitmap mBoardBitmap;
	//private Canvas mBoardCanvas;
	//private final Paint mSuitPaint = new Paint();

	private HashMap<Tile, BitmapDrawable> _faces;
	private BitmapDrawable _tile1_top, _tile1_body, _tile1_bottom;
	private BitmapDrawable _tile2_top, _tile2_body, _tile2_bottom;

	private int mScreenWidth;
	private int mScreenHeight;

	private BoardController controller;
	private Board board;

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		controller = new BoardController(this);
		SetScreenSize(320, 480);
		setFocusable(true);
		setFocusableInTouchMode(true);
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

	public void SetScreenSize(int width, int height) {
		mScreenWidth = width;
		mScreenHeight = height;
		//mBoardBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		//mBoardCanvas = new Canvas(mBoardBitmap);
	}

	private void LoadImages() {
		Resources r = getContext().getResources();

		_faces = new java.util.HashMap<Tile, BitmapDrawable>();
		_tile1_top = (BitmapDrawable) r.getDrawable(R.drawable.tile1_1);
		_tile1_body = (BitmapDrawable) r.getDrawable(R.drawable.tile1_2);
		_tile1_bottom = (BitmapDrawable) r.getDrawable(R.drawable.tile1_3);
		_tile2_top = (BitmapDrawable) r.getDrawable(R.drawable.tile2_1);
		_tile2_body = (BitmapDrawable) r.getDrawable(R.drawable.tile2_2);
		_tile2_bottom = (BitmapDrawable) r.getDrawable(R.drawable.tile2_3);

		for (Tile t : TileSet.getAllTiles()) {
			try {
				String name = String.format("%1$s%2$s", t.getKind(), t.getNumber());
				Field field = R.drawable.class.getField(name.toLowerCase());
				BitmapDrawable face = (BitmapDrawable) r.getDrawable(field.getInt(t));
				_faces.put(t, face);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
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

	private void DrawTiles(Canvas canvas) {
		int sx = (int) ((mScreenWidth - Math.ceil(board.getColumnCount() / 2.0)	* CellWidth) / 2);
		int sy = (int) ((mScreenHeight - StatusBarHeight - Math.ceil(board.getRowCount() / 2.0)	* CellHeight) / 2);

		for (Cell cell : getDrawList()) {
			Position pos = cell.getPosition();
			BitmapDrawable face = _faces.get(cell.getTile());
			BitmapDrawable top, body, bottom;

			if (board.getSelection().contains(pos)) {
				top = _tile2_top;
				body = _tile2_body;
				bottom = _tile2_bottom;
			} else {
				top = _tile1_top;
				body = _tile1_body;
				bottom = _tile1_bottom;
			}

			int bodyW = body.getBitmap().getWidth();
			int x = sx + pos.getColumn() * CellWidth / 2 + pos.getLayer() * 4;
			int y = sy + pos.getRow() * CellHeight / 2 - pos.getLayer() * 4;
			_bounds.add(new PositionInfo(cell.getPosition(), 
					new Rect(x, y,	x + TileWidth, y + TileHeight)));

			drawDrawable(canvas, top, x, y);
			y += top.getBitmap().getHeight();

			drawDrawable(canvas, body, x, y);
			drawDrawable(canvas, face, x + 2 + (bodyW - face.getBitmap().getWidth()) / 2, y);
			y += body.getBitmap().getHeight();

			drawDrawable(canvas, bottom, x, y);
		}
	}

	private void drawDrawable(Canvas canvas, BitmapDrawable d, int x, int y) {
		d.setBounds(x, y, x + d.getBitmap().getWidth(), y + d.getBitmap().getHeight());
		// d.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		d.draw(canvas);
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
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public void onDraw(Canvas canvas) {
		drawBoard(canvas);
		// canvas.drawBitmap(mBoardBitmap, 0, 0, mSuitPaint);
	}

	private void drawBoard(Canvas canvas) {
		_bounds.clear();
		canvas.drawColor(Color.GREEN);
		if (board == null) {
			return;
		}

		DrawTiles(canvas);
		// DrawStatus();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			onMouseDown((int) event.getX(), (int) event.getY());
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			controller.undo();
			return true;
		}
		return super.onKeyDown(keyCode, msg);
	}

	protected void onMouseDown(int x, int y) {
		for (int i = _bounds.size() - 1; i >= 0; i--) {
			if (_bounds.get(i).getBounds().contains(x, y)) {
				controller.clickTile(_bounds.get(i).getPosition());
				break;
			}
		}
	}

	public void update() {
		invalidate();
	}

	public void showDialog(String string) {
		new AlertDialog.Builder(getContext())
	      .setMessage(string)
	      .setPositiveButton("OK", null)
	      .show();
	}
}