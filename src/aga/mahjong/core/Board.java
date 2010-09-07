package aga.mahjong.core;

import java.util.ArrayList;
import java.util.HashSet;

public class Board {

	/*private static class PairComparer implements IEqualityComparer<Pair> {
		public boolean equals(Pair x, Pair y) {
			return ((x.Position1 == y.Position1 && x.Position2 == y.Position2) || (x.Position1 == y.Position2 && x.Position2 == y.Position1));
		}

		public int hashCode(Pair obj) {
			return 0; // TODO: optimize
		}
	}

	private static class TileComparer implements IEqualityComparer<Tile> {
		public boolean equals(Tile x, Tile y) {
			return x.Match(y);
		}

		public int hashCode(Tile obj) {
			return 0;
		}
	}*/

	private IArrangeStrategy _arrangement;
	private Layout _layout;
	private Tile[][][] _tiles;

	public Tile getItem(Position pos) {
		return _tiles[pos.getLayer()][pos.getRow()][pos.getColumn()];
	}

	public void setItem(Position pos, Tile value) {
		_tiles[pos.getLayer()][pos.getRow()][pos.getColumn()] = value;
	}

	public Tile getItem(int layer, int row, int column) {
		if (layer < getLayerCount() && row < getRowCount()
				&& column < getColumnCount() && layer >= 0 && row >= 0
				&& column >= 0) {
			return _tiles[layer][row][column];
		} else {
			return null;
		}
	}

	public void setItem(int layer, int row, int column, Tile value) {
		if (layer < getLayerCount() && row < getRowCount()
				&& column < getColumnCount() && layer >= 0 && row >= 0
				&& column >= 0) {
			ResetStatus();
			_tiles[layer][row][column] = value;
		}
	}

	private java.util.ArrayList<Position> _selection = new java.util.ArrayList<Position>();

	public java.util.List<Position> getSelection() {
		return _selection;
	}

	private int _layerCount;

	public int getLayerCount() {
		return _layerCount;
	}

	private int _rowCount;

	public int getRowCount() {
		return _rowCount;
	}

	private int _columnCount;

	public int getColumnCount() {
		return _columnCount;
	}

	public Iterable<Position> getAllPositions() {
		return _layout.getPositions();
	}

	private int _tilesCount;

	public synchronized int getTilesCount()
	{
		if (_tilesCount < 0)
		{
			_tilesCount = 0;
			for(Position p : getAllPositions()) {
				if (getItem(p) != null)
					_tilesCount++;
			}
		}
		return _tilesCount;
	}

	private int _payersCount;

	public int getPayersCount() {
		if (_payersCount < 0) {
			_payersCount = GetPairs().length;
		}
		return _payersCount;
	}

	public Board(Layout layout, IArrangeStrategy arrangement) {
		_arrangement = arrangement;
		_layout = layout;
		_tiles = new Tile[layout.getLayerCount()][layout.getRowCount()][layout
				.getColumnCount()];
		_layerCount = layout.getLayerCount();
		_rowCount = layout.getRowCount();
		_columnCount = layout.getColumnCount();
		ResetStatus();
		Init();
	}

	public Cell[] GetFreePositions()
	{
		ArrayList<Cell> res = new ArrayList<Cell>();
		for(Position p : getAllPositions()) {
			if (getItem(p) != null && IsFree(p))
				res.add(new Cell(p, this.getItem(p)));
		}
		return res.toArray(new Cell[res.size()]);
	}

	public boolean IsFree(Position pos) {
		boolean flag1 = true;
		boolean flag2 = true;
		
		for (int row = pos.getRow() - 1; row <= pos.getRow() + 1; row++) {
			if (_tiles[pos.getLayer()][row][pos.getColumn() - 2] != null) {
				flag1 = false;
			}
		}
		
		for (int row = pos.getRow() - 1; row <= pos.getRow() + 1; row++) {
			if (_tiles[pos.getLayer()][row][pos.getColumn() + 2] != null) {
				flag2 = false;
			}
		}

		if (flag1 || flag2) {
			if (pos.getLayer() < getLayerCount() - 1) {
				for (int row = pos.getRow() - 1; row <= pos.getRow() + 1; row++) {
					for (int col = pos.getColumn() - 1; col <= pos.getColumn() + 1; col++) {
						if (_tiles[pos.getLayer() + 1][row][col] != null) {
							return false;
						}
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public Pair[] GetPairs()
	{
		HashSet<Pair> res = new HashSet<Pair>();
		Cell[] positions = GetFreePositions();
		for(Cell p : positions) {
			for(Cell q : positions) {
				if (!p.getPosition().equals(q.getPosition()) && Tile.isMatch(p.getTile(), q.getTile()))
					res.add(new Pair(p.getPosition(), q.getPosition()));
			}
		}
		return res.toArray(new Pair[res.size()]);
		/*return positions.Join(positions, p => p.Tile, p => p.Tile, 
				(p, q) => new Pair(p.Position, q.Position), new TileComparer())
				.Where(p => p.Position1 != p.Position2)
				.Distinct(new PairComparer()).toArray();*/
	}

	public void Init() {
		getSelection().clear();
		_arrangement.Arrange(this);
	}

	public void Restart() {
		getSelection().clear();
		_arrangement.Restore(this);
	}

	private void ResetStatus() {
		_payersCount = _tilesCount = -1;
	}
}