package fourchess;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import fourchess.moves.CastleMove;
import fourchess.moves.Move;
import fourchess.moves.SimpleMove;
import fourchess.moves.SkipMove;
import fourchess.pieces.King;

public class Board implements Iterable<Point>
{
	private Piece[][] pieces; // x, y
	private List<Point> points = new ArrayList<Point>();
	private List<Move> moves = new ArrayList<Move>();
	private int movePointer = -1;
	
	public Board()
	{
		pieces = new Piece[14][14];
		
		for (int x = 0; x < pieces.length; ++x)
		for (int y = 0; y < pieces[x].length; ++y)
			if (isValidSpot(x, y))
				points.add(new Point(x, y));
	}
	
	public int getWidth()
	{
		return pieces.length;
	}
	
	public int getHeight()
	{
		return pieces[0].length;
	}
	
	public boolean isValidSpot(int x, int y)
	{
		return !((x < 3 || x > 10) && (y < 3 || y > 10)) && x >= 0 && x < 14 && y >= 0 && y < 14;
	}
	
	public Piece getPiece(int x, int y)
	{
		if (!isValidSpot(x, y))
			throw new IllegalArgumentException("Invalid spot");
		
		return pieces[x][y];
	}
	
	public King getKing(Player player)
	{
		for (Point p : this)
		{
			Piece piece = getPiece(p.x.intValue(), p.y.intValue());
			
			if (piece != null && piece.owner.equals(player) && (piece instanceof King))
			{
				return (King) piece;
			}
		}
		
		return null;
	}
	
	public boolean hasPiece(int x, int y)
	{
		return getPiece(x, y) != null;
	}
	
	public boolean hasPiece(int x, int y, Player owner)
	{
		return getPiece(x, y) != null && getPiece(x, y).owner.equals(owner);
	}
	
	/**
	 * Returns the piece formerly inhabiting that spot, null if there wasn't one.
	 */
	public Piece putPiece(int x, int y, Piece piece)
	{
		if (!isValidSpot(x, y))
			throw new IllegalArgumentException("Invalid spot");
		
		try     { return pieces[x][y];  }
		finally { pieces[x][y] = piece; }
	}
	
	public void removePiece(int x, int y)
	{
		putPiece(x, y, null);
	}
	
	public Piece movePiece(Piece piece, int x, int y)
	{
		Point piecePosition = find(piece);
		return movePiece(piecePosition.x.intValue(), piecePosition.y.intValue(), x, y);
	}
	
	/**
	 * Returns the captured piece, null if there wasn't one.
	 */
	public Piece movePiece(int x1, int y1, int x2, int y2)
	{
		if (!hasPiece(x1, y1))
			throw new NoSuchElementException("No piece at position");
		
		Piece piece = getPiece(x1, y1);
		
		while (canRedo())
		{
			moves.remove(moves.size() - 1);
		}
		
		Move move;
		
		// Check for castling, move rook as well
		if (piece.type.equals("K") && (Math.abs(x1 - x2) == 2) ^ (Math.abs(y1 - y2) == 2))
		{
			move = new CastleMove((King) piece, x2 - x1, y2 - y1);
		}
		else
		{
			move = new SimpleMove(piece.owner, piece, x1, y1, x2, y2);
		}
		
		move.doMove(this);
		moves.add(move);
		movePointer++;
		
		return move.getCapturedPiece();
	}
	
	public void addSkipMove()
	{
		moves.add(new SkipMove());
		movePointer++;
	}
	
	public boolean canUndo()
	{
		return movePointer >= 0;
	}
	
	public boolean canRedo()
	{
		return !moves.isEmpty() && movePointer < moves.size() - 1;
	}
	
	public Piece undo()
	{
		if (!canUndo())
			throw new IllegalStateException("No previous moves to undo");
		
		Move move = moves.get(movePointer);
		move.undoMove(this);
		movePointer--;
		return move.getCapturedPiece();
	}
	
	public Piece redo()
	{
		if (!canRedo())
			throw new IllegalStateException("No undone moves to redo");

		Move move = moves.get(movePointer + 1);
		move.doMove(this);
		movePointer++;
		return move.getCapturedPiece();
	}
	
	public Point find(Piece piece)
	{
		for (Point p : this)
			if (pieces[p.x.intValue()][p.y.intValue()] == piece)
				return new Point(p);
		
		return new Point(-1, -1);
	}
	
	public boolean contains(Piece piece)
	{
		return find(piece) != null;
	}
	
	public Iterator<Point> iterator()
	{
		return new PointIterator();
	}
	
	private class PointIterator implements Iterator<Point>
	{
		private Iterator<Point> itr = points.iterator();
		
		public boolean hasNext()
		{
			return itr.hasNext();
		}
		
		public Point next()
		{
			return new Point(itr.next());
		}
		
		public void remove()
		{
			itr.remove();
		}
	}
	
	public Board copyIgnoreHistory()
	{
		Board copy = new Board();
		
		for (Point p : this)
			copy.pieces[p.x.intValue()][p.y.intValue()] = this.pieces[p.x.intValue()][p.y.intValue()];
		
		return copy;
	}
}
