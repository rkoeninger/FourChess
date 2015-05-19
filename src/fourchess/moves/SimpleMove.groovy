package fourchess.moves
import fourchess.*

class SimpleMove implements Move
{
	final Player player
	final Piece piece
	final int x0, y0, x1, y1
	
	private boolean movePerformed = false;
	private Piece captured;
	
	SimpleMove(Player player, Piece piece, int x0, int y0, int x1, int y1)
	{
		this.player = player
		this.piece = piece
		this.x0 = x0
		this.y0 = y0
		this.x1 = x1
		this.y1 = y1
	}
	
	Piece getCapturedPiece() { captured }

	void doMove(Board board)
	{
		if (board.hasPiece(x1, y1))
			captured = board.getPiece(x1, y1)
		
		board.removePiece(x0, y0)
		board.putPiece(x1, y1, piece)
		piece.move()
		movePerformed = true
	}
	
	void undoMove(Board board)
	{
		board.removePiece(x1, y1)
		board.putPiece(x0, y0, piece)
		
		if (captured != null)
			board.putPiece(x1, y1, captured)
		
		piece.unmove()
		movePerformed = false
	}
}
