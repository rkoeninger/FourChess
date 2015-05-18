package fourchess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

import static fourchess.Direction.*

public class DisplayFrame extends JFrame
{
	public final Display display;
	
	public DisplayFrame(Game game)
	{
		super("Four Chess");
		this.display = new Display(game);
		display.addDisplayListener(new DListener());
		game.addGameListener(new GListener());
		setResizable(false);
		add(display);
		setJMenuBar(createMenus());
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private JMenuItem undoMenuItem;
	private JMenuItem redoMenuItem;
	private ButtonGroup viewSelectButtonGroup;
	private JMenuItem rotateOnTurnMenuItem;
	private JMenuItem player1ViewMenuItem;
	private JMenuItem player2ViewMenuItem;
	private JMenuItem player3ViewMenuItem;
	private JMenuItem player4ViewMenuItem;
	
	private JMenuBar createMenus()
	{
		JMenuItem skipMenuItem = new JMenuItem("Skip Turn");
		undoMenuItem = new JMenuItem("Undo");
		redoMenuItem = new JMenuItem("Redo");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		player1ViewMenuItem = new JRadioButtonMenuItem("Player 1");
		player2ViewMenuItem = new JRadioButtonMenuItem("Player 2");
		player3ViewMenuItem = new JRadioButtonMenuItem("Player 3");
		player4ViewMenuItem = new JRadioButtonMenuItem("Player 4");
		rotateOnTurnMenuItem = new JRadioButtonMenuItem("Active Player");
		
		viewSelectButtonGroup = new ButtonGroup();
		viewSelectButtonGroup.add(rotateOnTurnMenuItem);
		viewSelectButtonGroup.add(player1ViewMenuItem);
		viewSelectButtonGroup.add(player2ViewMenuItem);
		viewSelectButtonGroup.add(player3ViewMenuItem);
		viewSelectButtonGroup.add(player4ViewMenuItem);
		
		viewSelectButtonGroup.setSelected(rotateOnTurnMenuItem.getModel(), true);
		
		refreshUndoButtons();
		
		skipMenuItem.setActionCommand("skip");
		undoMenuItem.setActionCommand("undo");
		redoMenuItem.setActionCommand("redo");
		exitMenuItem.setActionCommand("exit");
		rotateOnTurnMenuItem.setActionCommand("rotateonturn");
		player1ViewMenuItem.setActionCommand("viewplayer1");
		player2ViewMenuItem.setActionCommand("viewplayer2");
		player3ViewMenuItem.setActionCommand("viewplayer3");
		player4ViewMenuItem.setActionCommand("viewplayer4");
		
		MenuListener listener = new MenuListener();
		skipMenuItem.addActionListener(listener);
		exitMenuItem.addActionListener(listener);
		undoMenuItem.addActionListener(listener);
		redoMenuItem.addActionListener(listener);
		rotateOnTurnMenuItem.addActionListener(listener);
		player1ViewMenuItem.addActionListener(listener);
		player2ViewMenuItem.addActionListener(listener);
		player3ViewMenuItem.addActionListener(listener);
		player4ViewMenuItem.addActionListener(listener);
		
		JMenu gameMenu = new JMenu("Game");
		gameMenu.add(undoMenuItem);
		gameMenu.add(redoMenuItem);
		gameMenu.add(new JSeparator());
		gameMenu.add(skipMenuItem);
		gameMenu.add(new JSeparator());
		gameMenu.add(exitMenuItem);
		
		JMenu displayMenu = new JMenu("Display");
		displayMenu.add(rotateOnTurnMenuItem);
		displayMenu.add(player1ViewMenuItem);
		displayMenu.add(player2ViewMenuItem);
		displayMenu.add(player3ViewMenuItem);
		displayMenu.add(player4ViewMenuItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(gameMenu);
		menuBar.add(displayMenu);
		return menuBar;
	}
	
	private class MenuListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getActionCommand().equals("undo"))
			{
				if (display.game.canUndo())
					display.game.undo();
			}
			else if (e.getActionCommand().equals("redo"))
			{
				if (display.game.canRedo())
					display.game.redo();
			}
			else if (e.getActionCommand().equals("skip"))
			{
				display.game.skipTurn();
			}
			else if (e.getActionCommand().equals("exit"))
			{
				System.exit(0);
			}
			else if (e.getActionCommand().equals("rotateonturn"))
			{
				display.setPerspectiveFollowsActivePlayer(((JRadioButtonMenuItem) e.getSource()).isSelected());
			}
			else if (e.getActionCommand().startsWith("viewplayer"))
			{
				String playerIdString = e.getActionCommand().substring("viewplayer".length());
				int playerId = Integer.parseInt(playerIdString) - 1;
				display.setPerspectiveFollowsActivePlayer(false);
				display.setPerspectiveForPlayer(playerId);
			}
			
			repaint();
		}
	}
	
	private class GListener implements GameListener
	{
		public void turnStart()
		{
			refreshUndoButtons();
		}
	}
	
	private class DListener implements DisplayListener
	{
		public void settingChanged()
		{
			if (display.getPerspectiveFollowsActivePlayer())
			{
				viewSelectButtonGroup.setSelected(rotateOnTurnMenuItem.getModel(), true);
			}
			else
			{
				switch (display.getPerspective())
				{
				case N: viewSelectButtonGroup.setSelected(player1ViewMenuItem.getModel(), true); break;
				case E: viewSelectButtonGroup.setSelected(player2ViewMenuItem.getModel(), true); break;
				case S: viewSelectButtonGroup.setSelected(player3ViewMenuItem.getModel(), true); break;
				case W: viewSelectButtonGroup.setSelected(player4ViewMenuItem.getModel(), true); break;
				}
			}
		}
	}
	
	public void refreshUndoButtons()
	{
		undoMenuItem.setEnabled(display.game.canUndo());
		redoMenuItem.setEnabled(display.game.canRedo());
	}
	
	private static final long serialVersionUID = 1L;
}
