package aga.mahjong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity {
	private BoardView boardView;
	
	public BoardView getBoardView() {
		return boardView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutProvider.init(this);
		Config.getInstance().init(this);
		
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
        setContentView(R.layout.main);
        boardView = (BoardView)findViewById(R.id.boardView);
	}

	@Override
	public void onStart() {
		super.onStart();
		//TODO: restore Game();
		boardView.getController().startNewGame();
	}

	@Override
	protected void onStop() {
		super.onStop();
		//TODO: SaveGame();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_game:
			boardView.getController().startNewGame();
			break;
		case R.id.restart:
			boardView.getController().restart();
			break;
		case R.id.undo:
			boardView.getController().undo();
			break;
		case R.id.hint:
			boardView.getController().showHint();
			break;
		case R.id.options:
			startActivityForResult(new Intent(Names.ACTION_OPTIONS), 1);
			break;
		case R.id.quit:
			finish();
			break;
		case R.id.about:
			String title = String.format("%1$s %2$s", 
					getResources().getString(R.string.app_name),
					getResources().getString(R.string.app_version));
			String message = String.format("%1$s\n%2$s", 
					getResources().getString(R.string.copyright),
					getResources().getString(R.string.url));
			boardView.showDialog(title, message);
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