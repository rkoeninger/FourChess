package fourchess
import fourchess.pieces.*

import java.awt.*
import java.util.List
import static fourchess.Direction.*

public class Game
{
	public static class Builder
	{
		private List<Player> players = new ArrayList<Player>();
		
		public Builder addPlayer(String name, Color color)
		{
			players.add(new Player(players.size() + 1, name, color));
			return this;
		}
		
		public Game build()
		{
			if (players.size() != 4)
				throw new IllegalStateException("Must have 4 players");
			
			return new Game(players.toArray(new Player[4]));
		}
	}
	
	public enum Phase
	{
		Select,
		Move
	}
	
	public final Board board = new Board();
	private Player[] players;
	private int currentPlayer = 0;
	private int turn = 0;
	private Phase phase = Phase.Select;
	private Point selection = null;
	
	private Game(Player[] players)
	{
		this.players = players;
		setupStartingPieces();
	}
	
	private Set<GameListener> listeners = new HashSet<GameListener>();
	
	public void addGameListener(GameListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeGameListener(GameListener listener)
	{
		listeners.remove(listener);
	}
	
	private void fireTurnCompleted()
	{
		for (GameListener listener : listeners)
		{
			listener.turnStart();
		}
	}
	
	public boolean canUndo()
	{
		return board.canUndo();
	}
	
	public boolean canRedo()
	{
		return board.canRedo();
	}
	
	public void undo()
	{
		Piece captured = board.undo();
		getCurrentPlayer().removeCapturedPiece(captured);
		moveToPreviousPlayer();
		fireTurnCompleted();
	}
	
	public void redo()
	{
		Piece captured = board.redo();
		getCurrentPlayer().addCapturedPiece(captured);
		moveToNextPlayer();
		fireTurnCompleted();
	}
	
	public void skipTurn()
	{
		board.addSkipMove();
		moveToNextPlayer();
		fireTurnCompleted();
	}
	
	public boolean isActive()
	{
		return currentPlayer >= 0;
	}
	
	public boolean isInCheck(Player player)
	{
		return board.getKing(player).isInCheck();
	}
	
	public Set<Point> getValidMoves()
	{
		return getSelectedPiece() == null ? null : getSelectedPiece().getValidMoves(selection); 
	}
	
	public void select(int x, int y)
	{
		if (phase == Phase.Select)
		{
			if (board.isValidSpot(x, y) && board.hasPiece(x, y, getCurrentPlayer()))
			{
				selection = new Point(x, y);
				phase = Phase.Move;
			}
		}
		else if (phase == Phase.Move)
		{
			Point target = new Point(x, y);
			Piece piece = getSelectedPiece();
			
			if (piece.getValidMoves(selection).contains(target))
			{
				// TODO: check for check/checkmate
				// TODO: en passant
				// TODO: pawn promotion
				
				Piece captured = board.movePiece(piece, x, y);
				
				if (captured != null)
					players[currentPlayer].addCapturedPiece(captured);
				
				phase = Phase.Select;
				selection = null;
				moveToNextPlayer();
			}
			
			fireTurnCompleted();
		}
	}
	
	public void cancelSelection()
	{
		if (phase == Phase.Move)
		{
			selection = null;
			phase = Phase.Select;
		}
	}
	
	public boolean hasSelection()
	{
		return selection != null;
	}
	
	public Point getSelection()
	{
		return selection;
	}
	
	public Piece getSelectedPiece()
	{
		return selection == null ? null : board.getPiece(selection.x.intValue(), selection.y.intValue());
	}
	
	public Phase getPhase()
	{
		return phase;
	}
	
	public int getCurrentTurn()
	{
		return turn;
	}
	
	public void moveToNextPlayer()
	{
		currentPlayer = (currentPlayer + 1) % players.length;
		turn++;
	}
	
	public void moveToPreviousPlayer()
	{
		currentPlayer = (currentPlayer + 3) % players.length;
		turn++;
	}
	
	public int getCurrentRound()
	{
		return turn % players.length + 1;
	}
	
	public Player getCurrentPlayer()
	{
		return players[currentPlayer];
	}
	
	public List<Player> getPlayers()
	{
		return Arrays.asList(players);
	}
	
	private void setupStartingPieces()
	{
		setupStartingPieces(players[0], N);
		setupStartingPieces(players[1], E);
		setupStartingPieces(players[2], S);
		setupStartingPieces(players[3], W);
	}
	
	private void setupStartingPieces(Player player, Direction position)
	{
		switch (position)
		{
		case E:
			for (int y = 3; y < 11; ++y)
				board.putPiece(12, y, new Pawn(board, player, position.getOpposite()));
			
			board.putPiece(13, 3, new Rook(board, player));
			board.putPiece(13, 4, new Knight(board, player));
			board.putPiece(13, 5, new Bishop(board, player));
			board.putPiece(13, 6, new Queen(board, player));
			board.putPiece(13, 7, new King(board, player));
			board.putPiece(13, 8, new Bishop(board, player));
			board.putPiece(13, 9, new Knight(board, player));
			board.putPiece(13, 10, new Rook(board, player));
			break;
		case N:
			for (int x = 3; x < 11; ++x)
				board.putPiece(x, 1, new Pawn(board, player, position.getOpposite()));
			
			board.putPiece(3, 0, new Rook(board, player));
			board.putPiece(4, 0, new Knight(board, player));
			board.putPiece(5, 0, new Bishop(board, player));
			board.putPiece(6, 0, new Queen(board, player));
			board.putPiece(7, 0, new King(board, player));
			board.putPiece(8, 0, new Bishop(board, player));
			board.putPiece(9, 0, new Knight(board, player));
			board.putPiece(10, 0, new Rook(board, player));
			break;
		case W:
			for (int y = 3; y < 11; ++y)
				board.putPiece(1, y, new Pawn(board, player, position.getOpposite()));
			
			board.putPiece(0, 3, new Rook(board, player));
			board.putPiece(0, 4, new Knight(board, player));
			board.putPiece(0, 5, new Bishop(board, player));
			board.putPiece(0, 6, new Queen(board, player));
			board.putPiece(0, 7, new King(board, player));
			board.putPiece(0, 8, new Bishop(board, player));
			board.putPiece(0, 9, new Knight(board, player));
			board.putPiece(0, 10, new Rook(board, player));
			break;
		case S:
			for (int x = 3; x < 11; ++x)
				board.putPiece(x, 12, new Pawn(board, player, position.getOpposite()));
			
			board.putPiece(3, 13, new Rook(board, player));
			board.putPiece(4, 13, new Knight(board, player));
			board.putPiece(5, 13, new Bishop(board, player));
			board.putPiece(6, 13, new Queen(board, player));
			board.putPiece(7, 13, new King(board, player));
			board.putPiece(8, 13, new Bishop(board, player));
			board.putPiece(9, 13, new Knight(board, player));
			board.putPiece(10, 13, new Rook(board, player));
			break;
		}
	}
}
