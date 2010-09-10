package aga.mahjong;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity {
	private static final String STATE_FILE_NAME = "state.data";
	
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
		File file = getFileStreamPath(STATE_FILE_NAME);
		if (file.exists()) {
			try {
				FileInputStream in = new FileInputStream(file);
				try {
					boardView.getController().loadState(in);
				} finally {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				boardView.getController().startNewGame();
			}
		}
		else
			boardView.getController().startNewGame();
	}

	@Override
	protected void onStop() {
		super.onStop();
		try {
			FileOutputStream out = openFileOutput(STATE_FILE_NAME, MODE_PRIVATE);
			try {
				boardView.getController().saveState(out);
			} finally {
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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