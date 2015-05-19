package fourchess.pieces
import fourchess.Board
import fourchess.Piece
import fourchess.Player
import fourchess.Rules

import java.awt.*

public class King extends Piece
{
	public King(Board board, Player owner)
	{
		super("K", 10, board, owner);
	}
	
	public Set<Point> getValidMoves(Point position)
	{
		Set<Point> moves = super.getValidMoves(position);

		Rules.checkSpot(this, position.x.intValue(), position.y.intValue() - 1, moves);     // Up
		Rules.checkSpot(this, position.x.intValue(), position.y.intValue() + 1, moves);     // Down
		Rules.checkSpot(this, position.x.intValue() - 1, position.y.intValue(), moves);     // Left
		Rules.checkSpot(this, position.x.intValue() + 1, position.y.intValue(), moves);     // Right
		Rules.checkSpot(this, position.x.intValue() - 1, position.y.intValue() - 1, moves); // Up-Left
		Rules.checkSpot(this, position.x.intValue() - 1, position.y.intValue() + 1, moves); // Down-Left
		Rules.checkSpot(this, position.x.intValue() + 1, position.y.intValue() - 1, moves); // Up-Right
		Rules.checkSpot(this, position.x.intValue() + 1, position.y.intValue() + 1, moves); // Down-Right
		
		// Look for castling moves
		if (!hasMoved() && !isInCheck())
		{
			if (position.x == 0 || position.x == board.getWidth() - 1)
			{
				int leftY = 3;
				int rightY = 10;
				
				Piece pieceLeft = board.getPiece(0, leftY);
				Piece pieceRight = board.getPiece(0, rightY);
				
				if (pieceLeft != null && !pieceLeft.hasMoved() && pieceLeft instanceof Rook)
				{
					if (!Rules.pathContainsAnyPiece(board, position.x.intValue(), position.y.intValue(), 0, -1, position.y.intValue() - leftY - 1))
						moves.add(new Point(position.x.intValue(), position.y.intValue() - 2));
				}
				
				if (pieceRight != null && !pieceRight.hasMoved() && pieceRight instanceof Rook)
				{
					if (!Rules.pathContainsAnyPiece(board, position.x.intValue(), position.y.intValue(), 0, 1, rightY - position.y.intValue() - 1))
						moves.add(new Point(position.x.intValue(), position.y.intValue() + 2));
				}
			}
			else if (position.y == 0 || position.y == board.getHeight() - 1)
			{
				int leftX = 3;
				int rightX = 10;
				
				Piece pieceLeft = board.getPiece(leftX, 0);
				Piece pieceRight = board.getPiece(rightX, 0);
				
				if (pieceLeft != null && !pieceLeft.hasMoved() && pieceLeft instanceof Rook)
				{
					if (!Rules.pathContainsAnyPiece(board, position.x.intValue(), position.y.intValue(), -1, 0, position.x.intValue() - leftX - 1))
						moves.add(new Point(position.x.intValue() - 2, position.y.intValue()));
				}
				
				if (pieceRight != null && !pieceRight.hasMoved() && pieceRight instanceof Rook)
				{
					if (!Rules.pathContainsAnyPiece(board, position.x.intValue(), position.y.intValue(), 1, 0, rightX - position.x.intValue() - 1))
						moves.add(new Point(position.x.intValue() + 2, position.y.intValue()));
				}
			}
		}
		
		return moves;
	}
	
	public boolean isInCheck()
	{
		return !getThreats().isEmpty();
	}
	
	public Set<Piece> getThreats()
	{
		Set<Piece> threats = new HashSet<Piece>();
		
		for (Point point : board)
		{
			Piece piece = board.getPiece(point.x.intValue(), point.y.intValue());
			
			if (piece != null && !piece.owner.equals(owner) && ! (piece instanceof King))
			{
				if (Rules.checkThreat(piece, this))
					threats.add(piece);
			}
		}
		
		return threats;
	}
}
