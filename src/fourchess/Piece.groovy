package fourchess
import java.awt.*

class Piece {
	final String type
    final int value
	final Player owner
	final Board board
	private int moveCount = 0
	
	Piece(type, value, board, owner) {
		this.type = type
		this.value = value
		this.board = board
		this.owner = owner
	}
	
	Point getPosition() { board.find this }
	
	int getX() { board.find(this).x }
	
	int getY() { board.find(this).y }
	
	void move() { moveCount++ }
	
	void undoMove() { moveCount = Math.max(moveCount - 1, 0) }
	
	boolean hasMoved() { moveCount > 0 }
	
	Set<Point> getValidMoves(Point position) { new HashSet<Point>() }
	
	boolean hasAnyMove() { !getValidMoves(board.find(this)).isEmpty() }
	
	boolean canMove(start, end) { getValidMoves start contains end }
}
