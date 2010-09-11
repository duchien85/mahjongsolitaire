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
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View {

	private final static class PositionInfo {
		private final Position position;
		private final Rect bounds;

		public Position getPosition() {
			return position;
		}

		public Rect getBounds() {
			return bounds;
		}

		public PositionInfo(Position pos, Rect rect) {
			position = pos;
			bounds = rect;
		}
	}

	private static final int CellWidth = 32;
	private static final int CellHeight = 45;
	private static final int TileWidth = 36;
	private static final int TileHeight = 49;
	private static final int StatusBarHeight = 10;

	private ArrayList<PositionInfo> bounds = new ArrayList<PositionInfo>();
	private HashMap<Tile, BitmapDrawable> faces;
	private BitmapDrawable _tile1_top, _tile1_body, _tile1_bottom;
	private BitmapDrawable _tile2_top, _tile2_body, _tile2_bottom;
	private Paint textPaint;
	private int screenWidth, screenHeight;
	private BoardController controller;
	private Board board;

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		controller = new BoardController(this);
		setFocusable(true);
		setFocusableInTouchMode(true);
		loadImages();
		
		textPaint = new Paint();
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(16);
		textPaint.setAntiAlias(true);
		textPaint.setFakeBoldText(true);
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board value) {
		board = value;
		update();
	}

	public BoardController getController() {
		return controller;
	}

	private void loadImages() {
		Resources r = getContext().getResources();

		faces = new java.util.HashMap<Tile, BitmapDrawable>();
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
				faces.put(t, face);
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

	private void drawTiles(Canvas canvas) {
		bounds.clear();
		if (board == null) {
			return;
		}
		
		int sx = (int) ((screenWidth - Math.ceil(board.getColumnCount() / 2.0)	* CellWidth) / 2);
		int sy = (int) ((screenHeight - StatusBarHeight - Math.ceil(board.getRowCount() / 2.0)	* CellHeight) / 2);

		for (Cell cell : getDrawList()) {
			Position pos = cell.getPosition();
			BitmapDrawable face = faces.get(cell.getTile());
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
			bounds.add(new PositionInfo(cell.getPosition(), 
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
		d.draw(canvas);
	}
	
	private void drawStatus(Canvas canvas) {
		String str = String.format("Tiles: %1$s   Pairs: %2$s", board.getTilesCount(), board.getPayersCount());
		canvas.drawText(str, 0, screenHeight - StatusBarHeight, textPaint);
	}	 

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		screenWidth = w;
		screenHeight = h;
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.GREEN);
		if (screenHeight <= 0 || screenWidth <= 0)
			return;
		
		drawTiles(canvas);
		drawStatus(canvas);
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
		for (int i = bounds.size() - 1; i >= 0; i--) {
			if (bounds.get(i).getBounds().contains(x, y)) {
				controller.clickTile(bounds.get(i).getPosition());
				break;
			}
		}
	}

	public void update() {
		invalidate();
	}

	public void showDialog(String title, String message) {
		new AlertDialog.Builder(getContext())
	      .setTitle(title)
	      .setMessage(message)
	      .setPositiveButton("OK", null)
	      .show();
	}
}