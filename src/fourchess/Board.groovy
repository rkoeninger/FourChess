package fourchess
import java.awt.Point
import java.util.*
import fourchess.moves.*
import fourchess.pieces.King

class Board implements Iterable<Point> {
	private Piece[][] pieces // x, y
	private List<Point> points = new ArrayList<Point>()
	private List<Move> moves = new ArrayList<Move>()
	private int movePointer = -1

    private static final int WIDTH = 14
    private static final int HEIGHT = 14
	
	Board() {
		pieces = new Piece[WIDTH][HEIGHT]
		
		for (int x = 0; x < WIDTH; ++x)
		for (int y = 0; y < HEIGHT; ++y)
			if (isValidSpot(x, y))
				points.add(new Point(x, y))
	}
	
	int getWidth() { WIDTH }
	
	int getHeight() { HEIGHT }
	
	boolean isValidSpot(x, y) {
		!((x < 3 || x > WIDTH - 4) && (y < 3 || y > HEIGHT - 4)) && x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT
	}

    Piece getPiece(Point p) { getPiece(p.x.intValue(), p.y.intValue()) }

	Piece getPiece(int x, int y) {
		if (isValidSpot(x, y))
            return pieces[x][y]

        throw new IllegalArgumentException("Invalid spot")
	}
	
	King getKing(Player player) {
		for (def p : this) {
			def piece = getPiece(p)

			if (piece != null && piece.owner.equals(player) && (piece instanceof King))
				return (King) piece
        }
		
		null
	}
	
	boolean hasPiece(int x, int y) { getPiece(x, y) != null }
	
	boolean hasPiece(int x, int y, Player owner) { hasPiece(x, y) && getPiece(x, y).owner.equals(owner) }
	
	/**
	 * Returns the piece formerly inhabiting that spot, null if there wasn't one.
	 */
	Piece putPiece(int x, int y, Piece piece) {
		if (!isValidSpot(x, y))
			throw new IllegalArgumentException("Invalid spot")

        def previousPiece = getPiece(x, y)
        pieces[x][y] = piece
        previousPiece
	}
	
	void removePiece(int x, int y) { putPiece(x, y, null) }
	
	Piece movePiece(Piece piece, int x, int y) {
		def piecePosition = find(piece)
		movePiece(piecePosition.x.intValue(), piecePosition.y.intValue(), x, y)
	}
	
	/**
	 * Returns the captured piece, null if there wasn't one.
	 */
	Piece movePiece(int x1, int y1, int x2, int y2) {
		if (!hasPiece(x1, y1))
			throw new NoSuchElementException("No piece at position")
		
		Piece piece = getPiece(x1, y1)
		
		while (canRedo())
			moves.remove(moves.size() - 1)

		Move move

		// Check for castling, move rook as well
		if (piece.type.equals("K") && (Math.abs(x1 - x2) == 2) ^ (Math.abs(y1 - y2) == 2))
		{
			move = new CastleMove((King) piece, x2 - x1, y2 - y1)
		}
		else
		{
			move = new SimpleMove(piece.owner, piece, x1, y1, x2, y2)
		}
		
		move.doMove(this)
		moves.add(move)
		movePointer++
		move.capturedPiece
	}
	
	void addSkipMove() {
		moves.add(new SkipMove())
		movePointer++
	}
	
	boolean canUndo() { movePointer >= 0 }
	
	boolean canRedo() { !moves.isEmpty() && movePointer < moves.size() - 1 }
	
	Piece undo() {
		if (!canUndo())
			throw new IllegalStateException("No previous moves to undo")
		
		def move = moves.get(movePointer)
		move.undoMove(this)
		movePointer--
		move.getCapturedPiece()
	}
	
	Piece redo() {
		if (!canRedo())
			throw new IllegalStateException("No undone moves to redo")

		def move = moves.get(movePointer + 1)
		move.doMove(this)
		movePointer++
		move.getCapturedPiece()
	}
	
	Point find(Piece piece) {
		for (Point p : this)
			if (pieces[p.x.intValue()][p.y.intValue()] == piece)
				return new Point(p)
		
		null
	}
	
	Iterator<Point> iterator() { new PointIterator() }
	
	private class PointIterator implements Iterator<Point> {
		private Iterator<Point> itr = points.iterator()
		
		boolean hasNext() { itr.hasNext() }
		
		Point next() { new Point(itr.next()) }
		
		void remove() {	itr.remove() }
	}
}
