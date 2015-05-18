package fourchess.pieces;

import java.awt.Point;
import java.util.Set;

import fourchess.Board;
import fourchess.Direction;
import fourchess.Piece;
import fourchess.Player;
import fourchess.Rules;

public class Pawn extends Piece
{
	public final Direction facing;
	
	public Pawn(Board board, Player owner, Direction facing)
	{
		super("P", 1, board, owner);
		this.facing = facing;
	}
	
	public Set<Point> getValidMoves(Point position)
	{
		Set<Point> moves = super.getValidMoves(position);
		
		if (!hasMoved())
		{
			Rules.checkSpotMoveOnly(board, this, facing.shiftForward(facing.shiftForward(position)), moves);
		}
		
		Rules.checkSpotMoveOnly(board, this, facing.shiftForward(position), moves);
		Rules.checkSpotAttackOnly(board, this, facing.shiftForwardLeft(position), moves);
		Rules.checkSpotAttackOnly(board, this, facing.shiftForwardRight(position), moves);
		
		return moves;
	}
}
