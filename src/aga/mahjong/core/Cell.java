package aga.mahjong.core;

public class Cell {
	private final Position position;
	private final Tile tile;

	public Position getPosition() {
		return position;
	}

	public Tile getTile() {
		return tile;
	}

	public Cell(Position pos, Tile tile) {
		this.position = pos;
		this.tile = tile;
	}

	@Override
	public String toString() {
		return String.format("%1$s = %2$s", getPosition(), getTile());
	}
}