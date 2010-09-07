package aga.mahjong;

import PocketMahjong.Core.*;

public class MainForm extends Form
{
	private BoardView _view;
	private ImageList _images;

	public MainForm()
	{
		InitializeComponent();
		CreateMenu();

		_view = new BoardView();
		_view.Dock = DockStyle.Fill;
		_view.Parent = this;
		_view.getController().StartNewGame();
	}

	private void CreateMenu()
	{
		_images = new ImageList();
		_images.ImageSize = new Size(32, 32);
		_toolBar.ImageList = _images;

		AddButton("Exit", Properties.Resources.cancel);
		AddButton("Tools", Properties.Resources.tools);
		AddButton(null, null);
		AddButton("New", Properties.Resources._new);
		AddButton("Restart", Properties.Resources.restart);
		AddButton(null, null);
		AddButton("Undo", Properties.Resources.undo);
		AddButton("Hint", Properties.Resources.find);
		AddButton(null, null);
		AddButton("Help", Properties.Resources.help);
	}

	private void AddButton(String tag, Bitmap bitmap)
	{
		ToolBarButton button = new ToolBarButton();
		if (tag != null)
		{
			button.Tag = tag;
			button.ImageIndex = _images.Images.size();
			_images.Images.Add(bitmap);
		}
		else
		{
			button.Style = ToolBarButtonStyle.Separator;
		}
		_toolBar.Buttons.Add(button);
	}

	private void ExitClick()
	{
		//var res = MessageBox.Show("Exit game?", Text, MessageBoxButtons.OKCancel,
		//	MessageBoxIcon.None, MessageBoxDefaultButton.Button2);
		//if (res == DialogResult.OK)
		this.Close();
	}

	private void NewClick()
	{
		//var res = MessageBox.Show("Start new game?", Text, MessageBoxButtons.OKCancel, 
		//	MessageBoxIcon.None, MessageBoxDefaultButton.Button2);
		//if (res == DialogResult.OK)
		_view.getController().StartNewGame();
	}

	private void RestartClick()
	{
		//var res = MessageBox.Show("Restart current game?", Text, MessageBoxButtons.OKCancel,
		//	MessageBoxIcon.None, MessageBoxDefaultButton.Button2);
		//if (res == DialogResult.OK)
		_view.getController().Restart();
	}

	private void UndoClick()
	{
		_view.getController().Undo();
	}

	private void HintClick()
	{
		_view.getController().ShowHint();
	}

	private void ToolsClick()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
		var lay = Config.Instance.Layout;
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
		var ar = Config.Instance.IsRandom;
		ConfigForm form = new ConfigForm();
		form.ShowDialog();
		if (lay != Config.Instance.Layout || ar != Config.Instance.IsRandom)
		{
			_view.getController().StartNewGame();
		}
	}

	private void HelpClick()
	{
		AboutForm form = new AboutForm();
		form.ShowDialog();
	}

	private void _toolBar_ButtonClick(Object sender, ToolBarButtonClickEventArgs e)
	{
		String name = ((String)((e.Button.Tag instanceof String) ? e.Button.Tag : null)) + "Click";
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
		var mi = this.getClass().GetMethod(name, BindingFlags.Instance | BindingFlags.NonPublic);
		if (mi != null)
		{
			mi.invoke(this, null);
		}
	}
}