package clueGame;

import java.awt.Color;

/**
 * ComputerPlayer: extends Player, class for human player in game. Includes a constructor to correctly set
 * the isAI flag.
 * 
 * @author Ryne Smith
 * @author Mikayla Sherwood
 *
 */
public class ComputerPlayer extends Player {
	public ComputerPlayer(String name, Color color, int rowLocation, int colLocation) {
		super(name, color, rowLocation, colLocation);
		this.isAI = true;
	}
}
