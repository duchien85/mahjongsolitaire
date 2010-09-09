package aga.mahjong;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Config {
	private static final String IS_RANDOM = "IsRandom";
	private static final String LAYOUT = "Layout";
	
	private static Config instance = new Config();
	private Activity activity;

	public static Config getInstance() {
		return instance;
	}
	
	private Config() {
	}
	
	public void init(Activity a) {
		activity = a;
	}
	
	public boolean isRandom() {
		return getPrefs().getBoolean(IS_RANDOM, true);
	}

	public void setIsRandom(boolean value) {
		Editor editor = getPrefs().edit();
		editor.putBoolean(IS_RANDOM, value);
		editor.commit();
	}

	public String getLayout() {
		return getPrefs().getString(LAYOUT, "turtle");
	}

	public void setLayout(String value) {
		Editor editor = getPrefs().edit();
		editor.putString(LAYOUT, value);
		editor.commit();
	}
	
	private SharedPreferences getPrefs() {
		return activity.getPreferences(Context.MODE_PRIVATE);
	}
}