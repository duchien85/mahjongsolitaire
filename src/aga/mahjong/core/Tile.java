package aga.mahjong.core;

public class Tile {
	private TileKind _kind = TileKind.forValue(0);

	public TileKind getKind() {
		return _kind;
	}

	private int _number;

	public int getNumber() {
		return _number;
	}

	public Tile(TileKind kind, int number) {
		_kind = kind;
		_number = number;
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