package fourchess
import java.awt.Color
import java.util.*

class Player {
	final int id
	final String name
	final Color color
	final Set<Piece> capturedPieces
	private Set<Piece> capturedPiecesWritable
	
	Player(int id, String name, Color color) {
		this.id = id
		this.name = name
		this.color = color
		this.capturedPiecesWritable = new HashSet<Piece>()
		this.capturedPieces = Collections.unmodifiableSet(capturedPiecesWritable)
	}
	
	void addCapturedPiece(Piece piece) { capturedPiecesWritable.add(piece) }
	
	void removeCapturedPiece(Piece piece) { capturedPiecesWritable.remove(piece); }
	
	int getScore() { (int) capturedPiecesWritable.sum(0) { it.value } }
}
