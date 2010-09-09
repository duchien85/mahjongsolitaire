package aga.mahjong;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends Activity {
	private static final int MENU_NEW_GAME = 1;
	private static final int MENU_RESTART = 2;
	private static final int MENU_OPTIONS = 3;
	private static final int MENU_QUIT = 4;
	private static final int MENU_UNDO = 5;
	private static final int MENU_HINT = 6;
	
	public static MainActivity instance;
	private BoardView boardView;
	
	public BoardView getBoardView() {
		return boardView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutProvider.init(this);
		Config.getInstance().init(this);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        boardView = (BoardView)findViewById(R.id.boardView);
		boardView.getController().startNewGame();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_NEW_GAME, 0, "New");
		menu.add(0, MENU_RESTART, 0, "Restart");
		menu.add(0, MENU_OPTIONS, 0, "Options");
		menu.add(0, MENU_UNDO, 0, "Undo");
		menu.add(0, MENU_HINT, 0, "Hint");
		menu.add(0, MENU_QUIT, 0, "Exit");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_NEW_GAME:
			boardView.getController().startNewGame();
			break;
		case MENU_RESTART:
			boardView.getController().restart();
			break;
		case MENU_UNDO:
			boardView.getController().undo();
			break;
		case MENU_HINT:
			boardView.getController().showHint();
			break;
		case MENU_OPTIONS:
			startActivityForResult(new Intent("aga.mahjong.OPTIONS"), 1);
			break;
		case MENU_QUIT:
			finish();
			break;
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode > 0)
			boardView.getController().startNewGame();
	}
}