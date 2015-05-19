package fourchess.pieces
import fourchess.*
import java.awt.*
import static fourchess.Rules.checkSpotAttackOnly
import static fourchess.Rules.checkSpotMoveOnly;

class Pawn extends Piece {
	final Direction facing
	
	Pawn(Board board, Player owner, Direction facing) {
		super("P", 1, board, owner)
		this.facing = facing
	}
	
	Set<Point> getValidMoves(Point position) {
		def moves = super.getValidMoves(position)
		
		if (!hasMoved())
			checkSpotMoveOnly(board, facing.shiftForward(facing.shiftForward(position)), moves)
		
		checkSpotMoveOnly(board, facing.shiftForward(position), moves)
		checkSpotAttackOnly(board, this, facing.shiftForwardLeft(position), moves)
		checkSpotAttackOnly(board, this, facing.shiftForwardRight(position), moves)
		
		moves
	}
}
