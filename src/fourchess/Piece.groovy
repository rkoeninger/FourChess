package fourchess;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Piece
{
	public final String type;
	public final Player owner;
	public final Board board;
	public final int value;
	private int moveCount = 0;
	
	public Piece(String type, int value, Board board, Player owner)
	{
		this.type = type;
		this.value = value;
		this.board = board;
		this.owner = owner;
	}
	
	public Point getPosition()
	{
		return board.find(this);
	}
	
	public int getX()
	{
		return board.find(this).x;
	}
	
	public int getY()
	{
		return board.find(this).y;
	}
	
	public void move()
	{
		moveCount++;
	}
	
	public void unmove()
	{
		moveCount = Math.max(moveCount - 1, 0);
	}
	
	public boolean hasMoved()
	{
		return moveCount > 0;
	}
	
	public Set<Point> getValidMoves(Point position)
	{
		return new HashSet<Point>();
	}
	
	public boolean hasAnyMove()
	{
		return !getValidMoves(board.find(this)).isEmpty();
	}
	
	public boolean canMove(Point start, Point end)
	{
		return getValidMoves(start).contains(end);
	}
}
