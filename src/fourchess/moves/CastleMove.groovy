package fourchess.moves
import fourchess.*
import fourchess.pieces.*

class CastleMove implements Move {
	final King king
	final Rook rook
	private int kx0, ky0, kx1, ky1
	private int rx0, ry0, rx1, ry1
	private boolean movePerformed
	
	CastleMove(King king, int dx, int dy) {
        if (king == null)
            throw new IllegalArgumentException("king")

		rook = (Rook) Rules.getPieceInPath(king.board, king.getX(), king.getY(), dx, dy)
		
		if (rook == null)
			throw new IllegalStateException("Where's the rook?")
		
		this.king = king
		kx0 = king.getX()
		ky0 = king.getY()
		kx1 = kx0 + dx
		ky1 = ky0 + dy
		
		rx0 = rook.getX()
		ry0 = rook.getY()
		
		if (dx < 0) {
			rx1 = kx1 + 1
			ry1 = ky1
		}
		else if (dx > 0) {
			rx1 = kx1 - 1
			ry1 = ky1
		}
		else if (dy < 0) {
			rx1 = kx1
			ry1 = ky1 + 1
		}
		else if (dy > 0) {
			rx1 = kx1
			ry1 = ky1 - 1
		}
	}
	
	Piece getCapturedPiece() { null }

	void doMove(Board board) {
		board.removePiece(kx0, ky0)
		board.putPiece(kx1, ky1, king)
		board.removePiece(rx0, ry0)
		board.putPiece(rx1, ry1, rook)
		king.move()
		rook.move()
		
		movePerformed = true
	}
	
	void undoMove(Board board) {
		board.removePiece(kx1, ky1)
		board.putPiece(kx0, ky0, king)
		board.removePiece(rx1, ry1)
		board.putPiece(rx0, ry0, rook)
		king.unmove()
		rook.unmove()
		
		movePerformed = false
	}
}