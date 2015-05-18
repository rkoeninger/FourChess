package fourchess.pieces;

import java.awt.Point;
import java.util.Set;

import fourchess.Board;
import fourchess.Piece;
import fourchess.Player;
import fourchess.Rules;

public class Bishop extends Piece
{
	public Bishop(Board board, Player owner)
	{
		super("B", 4, board, owner);
	}
	
	public Set<Point> getValidMoves(Point position)
	{
		Set<Point> moves = super.getValidMoves(position);
		Piece piece = board.getPiece(position.x, position.y);
		
		Rules.checkPath(piece, position.x, position.y, -1, -1, moves); // Up-Left
		Rules.checkPath(piece, position.x, position.y, -1, 1, moves);  // Down-Left
		Rules.checkPath(piece, position.x, position.y, 1, -1, moves);  // Up-Right 
		Rules.checkPath(piece, position.x, position.y, 1, 1, moves);   // Down-Right
		
		return moves;
	}
}
