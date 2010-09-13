package aga.mahjong;

import android.app.Activity;
import android.content.pm.ActivityInfo;

public abstract class BaseActivity extends Activity {
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
