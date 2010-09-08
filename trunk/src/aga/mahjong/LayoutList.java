package aga.mahjong;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class LayoutList extends ListActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		Object[] layouts = LayoutProvider.getLayouts().toArray();
		setListAdapter(new ArrayAdapter<Object>(this, R.layout.list_item, layouts));
		
		/*String name = Config.getInstance().getLayout();
		for(int i = 0; i < names.length; i++) {
			if (names[i].equals(name)) {
				list.setSelection(i);
				break;
			}
		}*/
    }
    
}
