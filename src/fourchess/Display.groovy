package fourchess
import fourchess.pieces.King

import javax.swing.*
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.Rectangle2D

import static fourchess.Direction.*

public class Display extends JComponent
{
	public final Game game;
	private Direction perspective = N;
	private int tileSize = 32;
	private boolean changePerspectiveOnTurn = false;
	
	public Display(Game game)
	{
		this.game = game;
		setPreferredSize(new Dimension(
			game.board.getWidth() * tileSize,
			game.board.getHeight() * tileSize));
		addMouseListener(new MListener());
		addKeyListener(new KListener());
		game.addGameListener(new GListener());
	}
	
	private class MListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				Point p = rotateViewToBoard(new Point((int) (e.getX() / tileSize), (int) (e.getY() / tileSize)));
				
				if (game.board.isValidSpot((int) p.x, (int) p.y))
				{
					if (game.getPhase() == Game.Phase.Select && game.board.hasPiece((int) p.x, (int) p.y) && !game.board.getPiece((int) p.x, (int) p.y).hasAnyMove())
					{
						JOptionPane.showMessageDialog(Display.this, "Piece has no moves!");
					}
					else
					{
						try
						{
							game.select((int) p.x, (int) p.y);
						}
						catch (ChessException exc)
						{
							JOptionPane.showMessageDialog(Display.this, exc.getMessage());
						}
					}
				}
			}
			else if (e.getButton() == MouseEvent.BUTTON3)
			{
				game.cancelSelection();
			}
			
			repaint();
		}
	}
	
	private class KListener extends KeyAdapter
	{
	}
	
	private class GListener implements GameListener
	{
		public void turnStart()
		{
			if (getPerspectiveFollowsActivePlayer())
			{
				setPerspectiveForCurrentPlayer();
				repaint();
			}
		}
	}
	
	private Set<DisplayListener> listeners = new HashSet<DisplayListener>();
	
	public void addDisplayListener(DisplayListener listener)
	{
		listeners.add(listener);
	}

	public void fireSettingChanged()
	{
		for (DisplayListener listener : listeners)
			listener.settingChanged();
	}
	
	public void setPerspectiveForCurrentPlayer()
	{
		setPerspectiveForPlayer(game.getCurrentPlayer().id - 1);
	}
	
	public void setPerspectiveForPlayer(int playerId)
	{
		switch (playerId)
		{
		case 0: setPerspective(N); break;
		case 1: setPerspective(E); break;
		case 2: setPerspective(S); break;
		case 3: setPerspective(W); break;
		default: throw new IllegalArgumentException("Invalid player id");
		}
	}
	
	public void setPerspectiveFollowsActivePlayer(boolean change)
	{
		this.changePerspectiveOnTurn = change;
		
		if (change)
		{
			setPerspectiveForCurrentPlayer();
		}
		
		fireSettingChanged();
	}
	
	public boolean getPerspectiveFollowsActivePlayer()
	{
		return changePerspectiveOnTurn;
	}
	
	public Direction getPerspective()
	{
		return perspective;
	}
	
	/**
	 * Perspective is the direction we are looking from.
	 * Typically,
	 * player 1 is looking from the north,
	 * player 2 is looking from the east,
	 * player 3 is looking from the south,
	 * player 4 is looking from the west
	 * 
	 * By default, the board is looking from the south.
	 */
	private void setPerspective(Direction facing)
	{
		perspective = facing;
		repaint();
	}
	
	private static final Color squareColor1 = new Color(64, 64, 64);
	private static final Color squareColor2 = new Color(32, 32, 32);
	
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		paintBoard(g);
		
		if (game.isActive())
		{
			paintOverlay(g);
			paintCheck(g);
			
			if (game.hasSelection())
			{
				paintSelection(g);
			}
		}
	}
	
	private void paintBoard(Graphics g)
	{
		for (Point pBoard : game.board)
		{
			Point pView = rotateBoardToView(pBoard);
			
			if (game.board.isValidSpot((int) pBoard.x, (int) pBoard.y))
			{
				g.setColor(((pBoard.x.intValue() ^ pBoard.y.intValue()) % 2 == 0) ? squareColor1 : squareColor2);
				g.fillRect(pView.x.intValue() * tileSize, pView.y.intValue() * tileSize, tileSize, tileSize);
				
				Piece piece = game.board.getPiece((int) pBoard.x, (int) pBoard.y);
				
				if (piece != null)
				{
					g.setColor(piece.owner.color);
					drawCenteredString(g, piece.type,
						pView.x.intValue() * tileSize,
						pView.y.intValue() * tileSize,
						tileSize,
						tileSize);
				}
			}
		}
	}
	
	private void paintOverlay(Graphics g)
	{
		Player currentPlayer = game.getCurrentPlayer();
		g.setColor(currentPlayer.color);
		String status = currentPlayer.name + "\'s Turn\n";
		status += "Round " + game.getCurrentRound() + "\n";
		status += game.getPhase().toString();
		
		if (game.isInCheck(currentPlayer))
		{
			status += "\n(Check)";
		}
		
		drawCenteredString(g, status, 0, 0, tileSize * 3, tileSize * 3);
		
		for (Player player : game.getPlayers())
		{
			g.setColor(player.color);
			String scoreStatus = String.valueOf(player.getScore());
			
			if (game.isInCheck(player))
				scoreStatus += " (C)";
			
			drawCenteredString(g, scoreStatus,
					(getWidth() - 3 * tileSize).intValue(),
					((player.id - 1) * tileSize * 3 / 4 - 2).intValue(),
					(tileSize * 3).intValue(),
					(tileSize * 3 / 4).intValue());
		}
	}
	
	private void paintCheck(Graphics g)
	{
		for (Player player : game.getPlayers())
		{
			King king = game.board.getKing(player);
			
			if (king != null)
			{
				Point pKingBoard = game.board.find(king);
				Point pKingView = rotateBoardToView(pKingBoard);
				
				for (Piece threatPiece : king.getThreats())
				{
					Point pThreatBoard = game.board.find(threatPiece);
					Point pThreatView = rotateBoardToView(pThreatBoard);
					
					g.setColor(Color.WHITE);
					g.drawLine(
							(pKingView.x * tileSize + tileSize / 2).intValue(),
							(pKingView.y * tileSize + tileSize / 2).intValue(),
							(pThreatView.x * tileSize + tileSize / 2).intValue(),
							(pThreatView.y * tileSize + tileSize / 2).intValue());
				}
			}
		}
	}
	
	private void paintSelection(Graphics g)
	{
		Point pSelectionBoard = game.getSelection();
		Point pSelectionView = rotateBoardToView(pSelectionBoard);
		
		Player currentPlayer = game.getCurrentPlayer();
		g.setColor(currentPlayer.color);
		g.drawRect(
			pSelectionView.x.intValue() * tileSize,
			pSelectionView.y.intValue() * tileSize,
			tileSize,
			tileSize);
		
		for (Point pMoveBoard : game.getValidMoves())
		{
			Point pMoveView = rotateBoardToView(pMoveBoard);
			g.setColor(currentPlayer.color);
			g.drawOval(
					(pMoveView.x * tileSize + tileSize / 8).intValue(),
					(pMoveView.y * tileSize + tileSize / 8).intValue(),
					(tileSize * 3 / 4).intValue(),
					(tileSize * 3 / 4).intValue());
		}
	}
	
	private Point rotateBoardToView(Point p)
	{
		p = new Point(p);
		
		int x0 = p.x;
		int y0 = p.y;
		
		switch (perspective)
		{
		case N:
			p.x = 13 - x0;
			p.y = 13 - y0;
			break;
		case W:
			p.x = y0;
			p.y = 13 - x0;
			break;
		case S:
			break;
		case E:
			p.x = 13 - y0;
			p.y = x0;
			break;
		}
		
		return p;
	}
	
	private Point rotateViewToBoard(Point p)
	{
		p = new Point(p);
		
		int x0 = p.x;
		int y0 = p.y;
		
		switch (perspective)
		{
		case N:
			p.x = 13 - x0;
			p.y = 13 - y0;
			break;
		case W:
			p.x = 13 - y0;
			p.y = x0;
			break;
		case S:
			break;
		case E:
			p.x = y0;
			p.y = 13 - x0;
			break;
		}
		
		return p;
	}
	
	private static void drawCenteredString(Graphics g, String string, int x, int y, int w, int h)
	{
		String[] lines = string.split("\n");
		
		Rectangle2D bounds = g.getFontMetrics().getStringBounds(lines[0], g);
		x += (w - bounds.getWidth()) / 2;
		y += (h - bounds.getHeight() * lines.length) / 2 + bounds.getHeight();
		
		for (String line : lines)
		{
			g.drawString(line, x, y);
			y += bounds.getHeight();
		}
	}
}
