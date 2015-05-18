package fourchess.moves;

import fourchess.Board;
import fourchess.Piece;

public class SkipMove implements Move
{
	private boolean movePerformed = false;
	
	public boolean wasMovePerformed()
	{
		return movePerformed;
	}
	
	public void doMove(Board board)
	{
		movePerformed = true;
	}
	
	public void undoMove(Board board)
	{
		movePerformed = false;
	}
	
	public Piece getCapturedPiece()
	{
		return null;
	}

}
