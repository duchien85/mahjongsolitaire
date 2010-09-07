package aga.mahjong;

import java.io.File;

public class Config {
	private static Config _instance;

	public static Config getInstance() {
		if (_instance == null) {
			_instance = new Config();
			Load();
		}
		return _instance;
	}

	private boolean _isRandom = true;

	public boolean getIsRandom() {
		return _isRandom;
	}

	public void setIsRandom(boolean value) {
		_isRandom = value;
	}

	private String _layout = "Turtle";

	public String getLayout() {
		return _layout;
	}

	public void setLayout(String value) {
		_layout = value;
	}

	private static String getFileName() {
		return ".config";
	}

	public static void Save() {
	}

	public static void Load() {
	}
}