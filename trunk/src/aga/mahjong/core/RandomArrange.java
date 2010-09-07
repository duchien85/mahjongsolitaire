package aga.mahjong.core;

import java.util.ArrayList;

public class RandomArrange implements IArrangeStrategy {
	private ArrayList<Tile> initLayout = new ArrayList<Tile>();

	public void Arrange(Board board) {
		java.util.Random rnd = new java.util.Random();
		initLayout.clear();
		ArrayList<Tile> tiles = new ArrayList<Tile>(TileSet.getAllTiles());
		for (Position pos : board.getAllPositions()) {
			int i = rnd.nextInt(tiles.size());
			board.setItem(pos, tiles.get(i));
			initLayout.add(tiles.get(i));
			tiles.remove(i);
		}
	}

	public void Restore(Board board) {
		if (initLayout.isEmpty()) {
			Arrange(board);
		} else {
			int i = 0;
			for (Position pos : board.getAllPositions()) {
				board.setItem(pos, initLayout.get(i));
				i++;
			}
		}
	}
}