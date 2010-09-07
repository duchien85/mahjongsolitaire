package aga.mahjong;

import PocketMahjong.Core.*;

public class LayoutProvider
{
	private static java.util.HashMap<String, Layout> _layouts;
	public static Iterable<Layout> getLayouts()
	{
		if (_layouts == null)
		{
			Load();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no Java equivalent to LINQ queries:
//C# TO JAVA CONVERTER TODO TASK: Lambda expressions and anonymous methods are not converted by C# to Java Converter:
		return _layouts.values().OrderBy(p => p.getName()).toArray();
	}

	private static void Load()
	{
		System.Diagnostics.Stopwatch w = new System.Diagnostics.Stopwatch();
		w.Start();

		String prefix = "PocketMahjong.Layouts.";
		_layouts = new java.util.HashMap<String, Layout>();
		Assembly asm = Assembly.GetExecutingAssembly();
		for (String name : asm.GetManifestResourceNames())
		{
			if (name.startsWith(prefix))
			{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
				var data = asm.GetManifestResourceStream(name);
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
				var lay = Layout.Load(data);
				lay.setName(name.replace(prefix, "").Replace(".bin",""));
				_layouts.put(lay.getName().toLowerCase(), lay);
			}
		}

		w.Stop();
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
		var t = w.ElapsedMilliseconds;
	}

	public static Layout GetLayout(String name)
	{
		if (_layouts == null)
		{
			Load();
		}

		name = name.toLowerCase();
		if (_layouts.containsKey(name))
		{
			return _layouts.get(name);
		}
		else
		{
			throw new ArgumentOutOfRangeException("name");
		}
	}
}