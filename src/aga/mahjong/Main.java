package aga.mahjong;

import aga.mahjong.core.Board;
import aga.mahjong.core.RandomArrange;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

public class Main extends Activity {
	public static Main instance;

	public static Main getInstance() {
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;

		// Force landscape and no title for extra room
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);
		BoardView view = (BoardView) findViewById(R.id.boardView);
		
		Board b = new Board(LayoutProvider.getLayout("turtle"), new RandomArrange());
		view.setBoard(b);
		
		// mSolitaireView.SetTextView((TextView) findViewById(R.id.text));
	}
}