package fourchess.pieces;
import java.awt.Point;
import fourchess.*
import static fourchess.Rules.*

class Rook extends Piece {
	Rook(Board board, Player owner) { super("R", 6, board, owner) }
	
	Set<Point> getValidMoves(Point position) {
		def moves = super.getValidMoves(position)
		def piece = board.getPiece(position.x.intValue(), position.y.intValue())
		
		checkPath(piece, position.x.intValue(), position.y.intValue(), 0, -1, moves) // Up
		checkPath(piece, position.x.intValue(), position.y.intValue(), 0, 1, moves)  // Down
		checkPath(piece, position.x.intValue(), position.y.intValue(), -1, 0, moves) // Left
		checkPath(piece, position.x.intValue(), position.y.intValue(), 1, 0, moves)  // Right
		
		moves
	}
}
