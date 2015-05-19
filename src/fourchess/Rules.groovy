package fourchess
import java.awt.*

public class Rules
{
	public static void checkPath(Piece piece, int x, int y, int dx, int dy, Set<Point> moves)
	{
		for (;;)
		{
			x += dx;
			y += dy;
			
			if (checkSpot(piece, x, y, moves))
				break;
		}
	}
	
	public static boolean pathContainsAnyPiece(Board board, int x, int y, int dx, int dy, int count)
	{
		for (int c = 0; c < count; ++c)
		{
			x += dx;
			y += dy;
			
			if (board.hasPiece(x, y))
				return true;
		}
		
		return false;
	}
	
	public static Piece getPieceInPath(Board board, int x, int y, int dx, int dy)
	{
		dx = (int) Math.signum(dx);
		dy = (int) Math.signum(dy);
		x += dx;
		y += dy;

		while (board.isValidSpot(x, y))
			if (board.hasPiece(x, y))
				return board.getPiece(x, y);

		return null;
	}
	
	/**
	 * Returns true if position contains a piece or is off the board.
	 * Returns true to indicate that a loop should stop scanning a row across the board.
	 */
	public static boolean checkSpot(Piece piece, int x, int y, Set<Point> moves)
	{
		if (!piece.board.isValidSpot(x, y))
			return true;
		
		Piece occupant = piece.board.getPiece(x, y);
		
		if (occupant == null)
		{
			moves.add(new Point(x, y));
		}
		else
		{
			if (!occupant.owner.equals(piece.owner))
			{
				moves.add(new Point(x, y));
			}
			
			return true;
		}
		
		return false;
	}
	
	public static void checkSpotAttackOnly(Board board, Piece piece, Point p, Set<Point> moves)
	{
		checkSpotAttackOnly(board, piece, p.x.intValue(), p.y.intValue(), moves);
	}
	
	public static void checkSpotAttackOnly(Board board, Piece piece, int x, int y, Set<Point> moves)
	{
		if (board.isValidSpot(x, y))
		{
			Piece occupant = board.getPiece(x, y);
			
			if (occupant != null && !occupant.owner.equals(piece.owner))
			{
				moves.add(new Point(x, y));
			}
		}
	}
	
	public static void checkSpotMoveOnly(Board board, Point p, Set<Point> moves)
	{
		checkSpotMoveOnly(board, p.x.intValue(), p.y.intValue(), moves);
	}
	
	public static void checkSpotMoveOnly(Board board, int x, int y, Set<Point> moves)
	{
		if (board.isValidSpot(x, y))
		{
			Piece occupant = board.getPiece(x, y);
			
			if (occupant == null)
			{
				moves.add(new Point(x, y));
			}
		}
	}
	
	public static boolean checkThreat(Piece attacker, Piece target)
	{
		Point pa = attacker.board.find(attacker);
		Point pt = attacker.board.find(target);
		return checkThreat(attacker, pa.x.intValue(), pa.y.intValue(), pt.x.intValue(), pt.y.intValue());
	}
	
	public static boolean checkThreat(Piece attacker, int xa, int ya, int xt, int yt)
	{
		return attacker.canMove(new Point(xa, ya), new Point(xt, yt));
	}
}
