package aga.mahjong;

import PocketMahjong.Core.*;

public class ConfigForm extends Form
{
	public ConfigForm()
	{
		InitializeComponent();

		if (Config.getInstance().getIsRandom())
		{
			_random.Checked = true;
		}
		else
		{
			_solvable.Checked = true;
		}

		Iterable<Layout> arr = LayoutProvider.getLayouts();
		_layout.DataSource = arr;
		_layout.SelectedValue = Config.getInstance().getLayout();
	}

	@Override
	protected void OnClosing(CancelEventArgs e)
	{
		Config.getInstance().setIsRandom(_random.Checked);
		Config.getInstance().setLayout((String)_layout.SelectedValue);
		Config.Save();
	}

	private void _layout_SelectedIndexChanged(Object sender, EventArgs e)
	{
		_view.Layout = (Layout)_layout.SelectedItem;
	}
}