package fourchess.pieces
import fourchess.*
import static fourchess.Rules.*
import java.awt.*

class Bishop extends Piece {
	Bishop(Board board, Player owner) { super("B", 4, board, owner) }
	
	Set<Point> getValidMoves(Point position) {
		def moves = super.getValidMoves(position)
		def piece = board.getPiece(position.x.intValue(), position.y.intValue())
		
		checkPath(piece, position.x.intValue(), position.y.intValue(), -1, -1, moves) // Up-Left
		checkPath(piece, position.x.intValue(), position.y.intValue(), -1, 1, moves)  // Down-Left
		checkPath(piece, position.x.intValue(), position.y.intValue(), 1, -1, moves)  // Up-Right
		checkPath(piece, position.x.intValue(), position.y.intValue(), 1, 1, moves)   // Down-Right
		
		moves
	}
}
