package aga.mahjong.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Position
{
	private int row;
	private int column;
	private int layer;
	
	public int getRow()
	{
		return row;
	}
	
	public void setRow(int value)
	{
		row = value;
	}

	public int getColumn()
	{
		return column;
	}
	
	public void setColumn(int value)
	{
		column = value;
	}

	public int getLayer()
	{
		return layer;
	}
	
	public void setLayer(int value)
	{
		layer = value;
	}

	/*public static boolean OpEquality(Position a, Position b)
	{
		return a.getLayer() == b.getLayer() && a.getRow() == b.getRow() && a.getColumn() == b.getColumn();
	}

	public static boolean OpInequality(Position a, Position b)
	{
		return !(Position.OpEquality(a, b));
	}*/

	public Position(int layer, int row, int column)
	{
		this.layer = layer;
		this.row = row;
		this.column = column;
	}

	@Override
	public String toString()
	{
		return String.format("[%1$s,%2$s,%3$s]", getLayer(), getRow(), getColumn());
	}

	public void write(OutputStream stream) throws IOException
	{
		stream.write((byte)layer);
		stream.write((byte)row);
		stream.write((byte)column);
	}

	public static Position read(InputStream stream) throws IOException
	{
		int lay = stream.read();
		int row = stream.read();
		int col = stream.read();
		return new Position(lay, row, col);
	}

	public Position clone()
	{
		return new Position(layer, row, column);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + layer;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (column != other.column)
			return false;
		if (layer != other.layer)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
}