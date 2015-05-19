package fourchess.moves
import fourchess.*

class SkipMove implements Move
{
	private boolean movePerformed = false

	void doMove(Board board)
	{
		movePerformed = true
	}
	
	void undoMove(Board board)
	{
		movePerformed = false
	}
	
	Piece getCapturedPiece() { null }
}
