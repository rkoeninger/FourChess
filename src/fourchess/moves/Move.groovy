package fourchess.moves
import fourchess.*

interface Move
{
	void doMove(Board board)
	void undoMove(Board board)
	Piece getCapturedPiece()
}
