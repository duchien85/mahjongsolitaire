package aga.mahjong.core;

public class Cell {
	private Position _position;
	private Tile _tile;

	public Position getPosition() {
		return _position;
	}

	public void setPosition(Position value) {
		_position = value;
	}

	public Tile getTile() {
		return _tile;
	}

	public void setTile(Tile value) {
		_tile = value;
	}

	public Cell(Position pos, Tile tile) {
		_position = pos;
		_tile = tile;
	}

	@Override
	public String toString() {
		return String.format("%1$s = %2$s", getPosition(), getTile());
	}

	public Cell clone() {
		return new Cell(_position, _tile);
	}
}