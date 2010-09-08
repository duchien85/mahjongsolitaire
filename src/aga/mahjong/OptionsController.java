package aga.mahjong;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

public class OptionsController {
	private final Main main;

	public OptionsController(Main main) {
		this.main = main;
	}

	public void show() {
		main.setContentView(R.layout.options);
		View view = main.findViewById(R.id.options_view);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);

		boolean random = Config.getInstance().isRandom();
		if (random)
			((RadioButton) main.findViewById(R.id.random)).setChecked(true);
		else
			((RadioButton) main.findViewById(R.id.solvable)).setChecked(true);

		ListView list = (ListView) main.findViewById(R.id.list);
		Object[] layouts = LayoutProvider.getLayouts().toArray();
		list.setAdapter(new ArrayAdapter<Object>(view.getContext(), R.id.list_item, layouts));
		
		/*String name = Config.getInstance().getLayout();
		for(int i = 0; i < names.length; i++) {
			if (names[i].equals(name)) {
				list.setSelection(i);
				break;
			}
		}*/
		
		final Button ok = (Button) main.findViewById(R.id.button_accept);
		ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				accept();
			}
		});

		final Button cancel = (Button) main.findViewById(R.id.button_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				main.setContentView(main.getBoardView());
			}
		});

		view.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					main.setContentView(main.getBoardView());
					return true;
				}
				return false;
			}
		});

		view.requestFocus();
	}

	private void accept() {
		RadioButton rb = (RadioButton) main.findViewById(R.id.random);
		Config.getInstance().setIsRandom(rb.isChecked());
		
		ListView list = (ListView) main.findViewById(R.id.list);
		Config.getInstance().setLayout(list.getSelectedItem().toString());
		
		main.setContentView(main.getBoardView());
	}
}
