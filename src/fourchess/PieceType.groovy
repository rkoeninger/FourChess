package fourchess;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class PieceType
{
	public final String name;
	public final String shortName;
	public final int value;
	
	public PieceType(String name, String shortName, int value)
	{
		this.name = name;
		this.shortName = shortName;
		this.value = value;
	}
	
	public Set<Point> getValidMoves(Point position)
	{
		return new HashSet<Point>();
	}
}
