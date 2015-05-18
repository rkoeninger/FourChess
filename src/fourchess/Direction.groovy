package fourchess;

import java.awt.Point;

public enum Direction
{
	E, N, W, S;
	
	public Direction getOpposite()
	{
		switch (this)
		{
		case E: return W;
		case N: return S;
		case W: return E;
		case S: return N;
		}
		
		throw new IllegalArgumentException("Undefined Direction");
	}
	
	public Point shiftForward(Point p)
	{
		switch (this)
		{
		case E: return new Point(p.x + 1, p.y);
		case N: return new Point(p.x, p.y - 1);
		case W: return new Point(p.x - 1, p.y);
		case S: return new Point(p.x, p.y + 1);
		}
		
		throw new IllegalArgumentException("Undefined Direction");
	}
	
	public Point shiftForwardLeft(Point p)
	{
		switch (this)
		{
		case E: return new Point(p.x + 1, p.y - 1);
		case N: return new Point(p.x - 1, p.y - 1);
		case W: return new Point(p.x - 1, p.y + 1);
		case S: return new Point(p.x + 1, p.y + 1);
		}
		
		throw new IllegalArgumentException("Undefined Direction");
	}
	
	public Point shiftForwardRight(Point p)
	{
		switch (this)
		{
		case E: return new Point(p.x + 1, p.y + 1);
		case N: return new Point(p.x + 1, p.y - 1);
		case W: return new Point(p.x - 1, p.y - 1);
		case S: return new Point(p.x - 1, p.y + 1);
		}
		
		throw new IllegalArgumentException("Undefined Direction");
	}
}
