package aga.mahjong;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import aga.mahjong.core.Layout;
import android.app.Activity;
import android.content.res.AssetManager;

public class LayoutProvider {
	private static final String dir = "layouts";

	private static ArrayList<Layout> layouts = new ArrayList<Layout>();

	public static Collection<Layout> getLayouts() {
		return layouts;
	}

	public static void init(Activity a) {
		try {
			layouts.clear();
			AssetManager am = a.getAssets();
			for (String str : am.list(dir)) {
				InputStream in = am.open(dir + "/" + str);
				Layout layout = Layout.load(in);
				layout.setName(str.replace(".bin", ""));
				layouts.add(layout);
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static Layout getLayout(String name) {
		name = name.toLowerCase();
		for (Layout l : getLayouts()) {
			if (l.getName().equals(name))
				return l;
		}
		throw new RuntimeException("Unknow layout: " + name);
	}
}