package clueGame;

import java.awt.Color;
import java.awt.Graphics;
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
	protected ArrayList<Card> seen;
	protected Boolean isAI;
	protected int row, column;
	
	public Player(String name, Color color, int rowLoc, int colLoc) {
		this.name = name;
		this.color = color;
		this.row = rowLoc;
		this.column = colLoc;
		this.hand = new ArrayList<Card>();
		this.seen = new ArrayList<Card>();
	}
	
	/**
	 * Given a suggestion, return a card that matches part of the suggestion if one exists in the player's hand.
	 * If no matching card can be found, return null
	 */
	public Card disproveSuggestion(Solution suggestion) {
		for(Card card: this.hand) {
			if (card == suggestion.getPerson()
					|| card == suggestion.getRoom()
					|| card == suggestion.getWeapon())
				return card;
		}
		return null;
	}
	
	public void drawPlayer(Graphics g, int width) {
		g.setColor(this.getColor());
		g.fillOval(this.column*width, this.row*width, width, width);
	}
	
	//some getters only for testing
	
	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public Boolean isAI() {
		return isAI;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}
	
	public ArrayList<Card> getSeen() {
		return seen;
	}

	public void updateHand(Card card) {
		this.hand.add(card);
	}
	
	public void setLocation(int rowLoc, int colLoc) {
		Board.getInstance().getCell(this.row, this.column).setOccupied(false);
		this.row = rowLoc;
		this.column = colLoc;
		Board.getInstance().getCell(rowLoc, colLoc).setOccupied(true);
	}
	
	public void updateSeen(Card card) {
		this.seen.add(card);
	}
	
}
