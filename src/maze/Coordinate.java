package maze;

/**
 * Coordinates is a state class for a game
 * 
 * @author Ying Hao
 */
public class Coordinate {
	public final int x;
	public final int y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		return x | (y << 16);
	}

	@Override
	public boolean equals(Object obj) {
		return obj.hashCode() == this.hashCode();
	}

	@Override
	public String toString() {
		return "Coordinate(" + x + ", " + y + ")";
	}
	
}
