package fourchess.pieces;

import java.awt.Point;
import java.util.Set;

import fourchess.Board;
import fourchess.Piece;
import fourchess.Player;
import fourchess.Rules;

public class Knight extends Piece
{
	public Knight(Board board, Player owner)
	{
		super("N", 3, board, owner);
	}
	
	public Set<Point> getValidMoves(Point position)
	{
		Set<Point> moves = super.getValidMoves(position);
		Piece piece = board.getPiece(position.x.intValue(), position.y.intValue());
		
		Rules.checkSpot(piece, position.x.intValue() - 1, position.y.intValue() - 2, moves); // 1 Left,  2 Up
		Rules.checkSpot(piece, position.x.intValue() + 1, position.y.intValue() - 2, moves); // 1 Right, 2 Up
		Rules.checkSpot(piece, position.x.intValue() - 1, position.y.intValue() + 2, moves); // 1 Left,  2 Down
		Rules.checkSpot(piece, position.x.intValue() + 1, position.y.intValue() + 2, moves); // 1 Right, 2 Down
		Rules.checkSpot(piece, position.x.intValue() - 2, position.y.intValue() - 1, moves); // 2 Left,  1 Up
		Rules.checkSpot(piece, position.x.intValue() + 2, position.y.intValue() - 1, moves); // 2 Right, 1 Up
		Rules.checkSpot(piece, position.x.intValue() - 2, position.y.intValue() + 1, moves); // 2 Left,  1 Down
		Rules.checkSpot(piece, position.x.intValue() + 2, position.y.intValue() + 1, moves); // 2 Right, 1 Down
		
		return moves;
	}
}
