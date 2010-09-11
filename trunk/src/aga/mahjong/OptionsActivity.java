package aga.mahjong;

import android.app.Activity;
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
				Intent intent = new Intent(Names.ACTION_CHANGE_LAYOUT);
				startActivityForResult(intent, 1);
			}
		});

		displayLayout(Config.getInstance().getLayout());
		
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

		view.requestFocus();
	}

	private void displayLayout(String name) {
		getLayoutName().setText(name);
		((LayoutView)findViewById(R.id.layout_view)).
			setLayout(LayoutProvider.getLayout(name));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			String name = data.getStringExtra(Names.EXTRA_RESULT);
			displayLayout(name);
		}
	}
	
	private TextView getLayoutName() {
		return (TextView)findViewById(R.id.layout_value);
	}
	
	private void accept() {
		RadioButton rb = (RadioButton)findViewById(R.id.random);
		Config cfg = Config.getInstance();
		cfg.setIsRandom(rb.isChecked());
		cfg.setLayout(getLayoutName().getText().toString());
		cfg.save();
		setResult(1);
		finish();
	}
}
