package aga.mahjong.core;

public class Tile {
	private final TileKind kind;
	private final int number;

	public TileKind getKind() {
		return kind;
	}

	public int getNumber() {
		return number;
	}

	public Tile(TileKind kind, int number) {
		this.kind = kind;
		this.number = number;
	}

	@Override
	public String toString() {
		String str = "";
		switch (getKind()) {
		case Bamboo:
			str = "B";
			break;
		case Character:
			str = "C";
			break;
		case Dot:
			str = "D";
			break;
		case Dragon:
			str = "R";
			break;
		case Flower:
			str = "F";
			break;
		case Season:
			str = "S";
			break;
		case Wind:
			str = "W";
			break;
		}
		return String.format("%1$s%2$s", str, getNumber());
	}

	public static boolean isMatch(Tile a, Tile b) {
		if (a.getKind().equals(b.getKind())) {
			if (a.getKind().equals(TileKind.Season)
					|| a.getKind().equals(TileKind.Flower)) {
				return true;
			} else {
				return a.getNumber() == b.getNumber();
			}
		} else {
			return false;
		}
	}

}