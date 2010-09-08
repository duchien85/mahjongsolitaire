package aga.mahjong;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class OptionsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		
		View view = findViewById(R.id.options_view);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);

		boolean random = Config.getInstance().isRandom();
		if (random)
			((RadioButton)findViewById(R.id.random)).setChecked(true);
		else
			((RadioButton)findViewById(R.id.solvable)).setChecked(true);

		final Button change = (Button)findViewById(R.id.change_layout);
		change.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("LIST");
				intent.setClassName("aga.mahjong", ListActivity.class.getSimpleName());
				startActivity(intent);
			}
		});

		final TextView layout = (TextView)findViewById(R.id.layout_value);
		layout.setText(Config.getInstance().getLayout());
		
		final Button ok = (Button)findViewById(R.id.button_accept);
		ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				accept();
			}
		});

		final Button cancel = (Button)findViewById(R.id.button_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		/*view.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					finish();
					return true;
				}
				return false;
			}
		});*/

		view.requestFocus();
	}

	private void accept() {
		RadioButton rb = (RadioButton)findViewById(R.id.random);
		Config.getInstance().setIsRandom(rb.isChecked());
		
		//ListView list = (ListView) main.findViewById(R.id.list);
		//Config.getInstance().setLayout(list.getSelectedItem().toString());
		
		finish();
	}
}
