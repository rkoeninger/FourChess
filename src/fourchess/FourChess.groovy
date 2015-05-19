package fourchess

import java.awt.Color
import javax.swing.UIManager

public class FourChess
{
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
		}
		catch (Exception ignored)
		{
			System.out.println("Unable to load system look and feel")
		}
		
		Game game = new Game.Builder()
			.addPlayer("Rob", Color.RED)
			.addPlayer("Job", new Color(127, 127, 255))
			.addPlayer("Joe", Color.GREEN)
			.addPlayer("Roe", Color.YELLOW)
			.build()
		
		DisplayFrame frame = new DisplayFrame(game)
		frame.display.setPerspectiveFollowsActivePlayer false
		frame.setVisible true
	}
	
}
