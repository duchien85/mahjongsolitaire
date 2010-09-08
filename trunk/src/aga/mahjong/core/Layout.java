package aga.mahjong.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Layout {

	private java.util.ArrayList<Position> privatePositions;

	public java.util.ArrayList<Position> getPositions() {
		return privatePositions;
	}

	private void setPositions(java.util.ArrayList<Position> value) {
		privatePositions = value;
	}

	private String privateName;

	public String getName() {
		return privateName;
	}

	public void setName(String value) {
		privateName = value;
	}

	private int privateLayerCount;

	public int getLayerCount() {
		return privateLayerCount;
	}

	private void setLayerCount(int value) {
		privateLayerCount = value;
	}

	private int privateRowCount;

	public int getRowCount() {
		return privateRowCount;
	}

	private void setRowCount(int value) {
		privateRowCount = value;
	}

	private int privateColumnCount;

	public int getColumnCount() {
		return privateColumnCount;
	}

	private void setColumnCount(int value) {
		privateColumnCount = value;
	}

	public Layout() {
		setPositions(new java.util.ArrayList<Position>());
	}

	@Override
	public String toString() {
		return getName();
	}

	// public static Layout Import(Stream data)
	// {
	// var res = new Layout();
	// StreamReader sr = new StreamReader(data);
	// while (!sr.EndOfStream)
	// {
	// var str = sr.ReadLine();
	// if (str.StartsWith("#"))
	// {
	// if (string.IsNullOrEmpty(res.Description))
	// res.Description = str.TrimStart('#').Trim();
	// }
	// else
	// {
	// var match = Regex.Match(str, @"(\d+?)\s+?(\d+?)\s+?(\d+?)");
	// if (match.Success)
	// {
	// var pos = new Position(int.Parse(match.Groups[3].Value),
	// int.Parse(match.Groups[1].Value),
	// int.Parse(match.Groups[2].Value));
	// res.LayerCount = Math.Max(res.LayerCount, pos.Layer + 1);
	// res.RowCount = Math.Max(res.RowCount, pos.Row + 1);
	// res.ColumnCount = Math.Max(res.ColumnCount, pos.Column + 1);
	// res.Positions.Add(pos);
	// }
	// }
	// }
	// return res;
	// }

	public static Layout load(InputStream stream) throws IOException {
		Layout res = new Layout();
		int count = stream.read();
		res.setPositions(new java.util.ArrayList<Position>(count));
		for (int i = 0; i < count; i++) {
			Position pos = Position.read(stream);
			res.getPositions().add(pos);
			res.setLayerCount(Math.max(res.getLayerCount(), pos.getLayer() + 1));
			res.setRowCount(Math.max(res.getRowCount(), pos.getRow() + 1));
			res.setColumnCount(Math.max(res.getColumnCount(), pos.getColumn() + 1));
		}
		return res;
	}

	public void save(OutputStream stream) throws IOException {
		stream.write(getPositions().size());
		for (Position pos : getPositions()) {
			pos.write(stream);
		}
	}
}