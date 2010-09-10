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
	private boolean isRandom;
	private String layout;

	public static Config getInstance() {
		return instance;
	}
	
	private Config() {
	}
	
	public void init(Activity a) {
		activity = a;
		SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
		isRandom = prefs.getBoolean(IS_RANDOM, true);
		layout = prefs.getString(LAYOUT, "well");
	}
	
	public boolean isRandom() {
		return isRandom;
	}

	public void setIsRandom(boolean value) {
		isRandom = value;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String value) {
		layout = value;
	}
	
	public void save() {
		SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean(IS_RANDOM, isRandom);
		editor.putString(LAYOUT, layout);
		editor.commit();
	}
}