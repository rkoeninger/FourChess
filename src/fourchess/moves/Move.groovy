package fourchess.moves;

import fourchess.Board;
import fourchess.Piece;

public interface Move
{
	public boolean wasMovePerformed();
	public void doMove(Board board);
	public void undoMove(Board board);
	public Piece getCapturedPiece();
}
