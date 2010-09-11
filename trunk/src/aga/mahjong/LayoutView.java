package aga.mahjong;

import aga.mahjong.core.Board;
import aga.mahjong.core.Layout;
import aga.mahjong.core.RandomArrange;
import aga.mahjong.core.Tile;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class LayoutView extends View {
	private Board board;
	private Layout layout;
	private Paint fillPaint;
	private Paint drawPaint;
	private int screenWidth, screenHeight;

	public LayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		fillPaint = new Paint();
		fillPaint.setColor(Color.WHITE);
		fillPaint.setStyle(Paint.Style.FILL);

		drawPaint = new Paint();
		drawPaint.setColor(Color.GRAY);
		drawPaint.setStyle(Paint.Style.STROKE);
	}

	public void setLayout(Layout value) {
		layout = value;
		if (value != null) {
			board = new Board(layout);
			new RandomArrange().arrange(board);
		} else {
			board = null;
		}
		this.invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		screenWidth = w;
		screenHeight = h;
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.GREEN);
		if (board != null) {
			int CellWidth = 8;
			int CellHeight = 16;
			int sx = (int)((screenWidth - Math.ceil(board.getColumnCount() / 2.0) * CellWidth) / 2);
			int sy = (int)((screenHeight - Math.ceil(board.getRowCount() / 2.0) * CellHeight) / 2);

			for (int lay = 0; lay < board.getLayerCount(); lay++) {
				for (int col = board.getColumnCount(); col >= 0; col--) {
					for (int row = 0; row < board.getRowCount(); row++) {
						Tile tile = board.getItem(lay, row, col);
						if (tile != null) {
							int x = sx + col * CellWidth / 2 + lay * 2;
							int y = sy + row * CellHeight / 2 - lay * 2;
							Rect rect = new Rect(x, y, x + CellWidth, y + CellHeight);
							canvas.drawRect(rect, fillPaint);
							canvas.drawRect(rect, drawPaint);
						}
					}
				}
			}
		}
	}

}
