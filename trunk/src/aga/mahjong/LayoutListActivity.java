package aga.mahjong;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LayoutListActivity extends ListActivity {
	private Object[] layouts;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		layouts = LayoutProvider.getLayouts().toArray();
		setListAdapter(new ArrayAdapter<Object>(this, R.layout.list_item, layouts));
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		String name = Config.getInstance().getLayout();
		for(int i = 0; i < layouts.length; i++) {
			if (layouts[i].toString().equals(name)) {
				setSelection(i);
				break;
			}
		}
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String name = layouts[position].toString();
		Intent intent = getIntent();
		intent.putExtra(Names.EXTRA_RESULT, name);
		setResult(0, intent);
		finish();
	}
    
	@Override
	protected void onResume() {
		super.onResume();
		String str = Config.getInstance().getOrientation();
		if (Names.ORIENTATION_LANDSCAPE.equals(str))
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		else if (Names.ORIENTATION_PORTRAIT.equals(str))
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		else
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}
}
