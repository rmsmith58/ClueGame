package clueGame;

import java.awt.Color;

/**
 * HumanPlayer: extends Player, class for human player in game. Includes a constructor to correctly set
 * the isAI flag.
 * 
 * @author Ryne Smith
 * @author Mikayla Sherwood
 *
 */
public class HumanPlayer extends Player {
	public HumanPlayer(String name, Color color, int rowLocation, int colLocation) {
		super(name, color, rowLocation, colLocation);
		this.isAI = false;
	}
}
