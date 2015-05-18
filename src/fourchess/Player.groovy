package fourchess;

import java.awt.Color;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Player
{
	public final int id;
	public final String name;
	public final Color color;
	public final Set<Piece> capturedPieces;
	private Set<Piece> capturedPiecesWritable;
	
	public Player(int id, String name, Color color)
	{
		this.id = id;
		this.name = name;
		this.color = color;
		this.capturedPiecesWritable = new HashSet<Piece>();
		this.capturedPieces = Collections.unmodifiableSet(capturedPiecesWritable);
	}
	
	public void addCapturedPiece(Piece piece)
	{
		capturedPiecesWritable.add(piece);
	}
	
	public void removeCapturedPiece(Piece piece)
	{
		capturedPiecesWritable.remove(piece);
	}
	
	public int getScore()
	{
		int score = 0;
		
		for (Piece piece : capturedPiecesWritable)
			score += piece.value;
		
		return score;
	}
}
