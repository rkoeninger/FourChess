package fourchess.pieces
import fourchess.*
import java.awt.*

class Knight extends Piece
{
	Knight(Board board, Player owner) { super("N", 3, board, owner) }
	
	public Set<Point> getValidMoves(Point position) {
		def moves = super.getValidMoves(position)
		def piece = board.getPiece(position.x.intValue(), position.y.intValue())
		
		Rules.checkSpot(piece, position.x.intValue() - 1, position.y.intValue() - 2, moves) // 1 Left,  2 Up
		Rules.checkSpot(piece, position.x.intValue() + 1, position.y.intValue() - 2, moves) // 1 Right, 2 Up
		Rules.checkSpot(piece, position.x.intValue() - 1, position.y.intValue() + 2, moves) // 1 Left,  2 Down
		Rules.checkSpot(piece, position.x.intValue() + 1, position.y.intValue() + 2, moves) // 1 Right, 2 Down
		Rules.checkSpot(piece, position.x.intValue() - 2, position.y.intValue() - 1, moves) // 2 Left,  1 Up
		Rules.checkSpot(piece, position.x.intValue() + 2, position.y.intValue() - 1, moves) // 2 Right, 1 Up
		Rules.checkSpot(piece, position.x.intValue() - 2, position.y.intValue() + 1, moves) // 2 Left,  1 Down
		Rules.checkSpot(piece, position.x.intValue() + 2, position.y.intValue() + 1, moves) // 2 Right, 1 Down
		
		moves
	}
}
