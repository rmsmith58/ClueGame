package clueGame;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Player: holds data for a single player in the game. Includes identifying info for name, color, and AI player flag,
 * as well as info for current coordinate location and hand of cards. Also includes a function to add a card to the player's hand.
 * Superclass of HumanPlayer and ComputerPlayer.
 * 
 * @author Ryne Smith
 * @author Mikayla Sherwood
 *
 */
public abstract class Player {
	private String name;
	private Color color;
	private ArrayList<Card> hand;
	protected Boolean isAI;
	protected int row, column;
	
	public Player(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	private void updateHand(Card card);
	
}
