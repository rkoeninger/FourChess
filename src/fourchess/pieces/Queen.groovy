package fourchess.pieces
import java.awt.Point
import fourchess.*
import static fourchess.Rules.*

class Queen extends Piece {
	Queen(Board board, Player owner) { super("Q", 10, board, owner) }

	Set<Point> getValidMoves(Point position) {
		def moves = super.getValidMoves(position)
		def piece = board.getPiece(position.x.intValue(), position.y.intValue())
		
		checkPath(piece, position.x.intValue(), position.y.intValue(), 0, -1, moves);  // Up
		checkPath(piece, position.x.intValue(), position.y.intValue(), 0, 1, moves);   // Down
		checkPath(piece, position.x.intValue(), position.y.intValue(), -1, 0, moves);  // Left
		checkPath(piece, position.x.intValue(), position.y.intValue(), 1, 0, moves);   // Right
		checkPath(piece, position.x.intValue(), position.y.intValue(), -1, -1, moves); // Up-Left
		checkPath(piece, position.x.intValue(), position.y.intValue(), -1, 1, moves);  // Down-Left
		checkPath(piece, position.x.intValue(), position.y.intValue(), 1, -1, moves);  // Up-Right
		checkPath(piece, position.x.intValue(), position.y.intValue(), 1, 1, moves);   // Down-Right
		
		moves
	}
}
