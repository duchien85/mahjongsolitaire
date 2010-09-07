package aga.mahjong;

import android.app.Activity;
import android.os.Bundle;

public class Main extends Activity {
	public static Main instance;
	
	public static Main getInstance() {
		return instance;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.main);
    }
}