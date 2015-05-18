package fourchess.pieces;

import java.awt.Point;
import java.util.Set;

import fourchess.Board;
import fourchess.Piece;
import fourchess.Player;
import fourchess.Rules;

public class Rook extends Piece
{
	public Rook(Board board, Player owner)
	{
		super("R", 6, board, owner);
	}
	
	public Set<Point> getValidMoves(Point position)
	{
		Set<Point> moves = super.getValidMoves(position);
		Piece piece = board.getPiece(position.x, position.y);
		
		Rules.checkPath(piece, position.x, position.y, 0, -1, moves); // Up
		Rules.checkPath(piece, position.x, position.y, 0, 1, moves);  // Down
		Rules.checkPath(piece, position.x, position.y, -1, 0, moves); // Left
		Rules.checkPath(piece, position.x, position.y, 1, 0, moves);  // Right
		
		return moves;
	}
}
