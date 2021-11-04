package clueGame;

import java.awt.Color;
import java.util.Random;
import java.util.ArrayList;

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
	
	public Solution createSuggestion() {
		//If the player is in a room in need to create a suggestion.
		//The weapon and person is randomly chosen out of the unseen list, and the room is the room the player is in.
		BoardCell location = new BoardCell(row, column);
		Card room = new Card("Lounge", CardType.ROOM);
		Card person = new Card("You", CardType.PERSON);
		Card weapon = new Card("Axe", CardType.WEAPON);
		/*
		ArrayList<Card> unseen = deck;
		unseen.removeAll(seen);
		ArrayList<Card> people;
		ArrayList<Card> weapon;
		
		for(Card card: seen) {
			if(card.getCardType() == CardType.PERSON) {
				people.add(card);
			}
			else if(card.getCardType() == CardType.WEAPON) {
				weapon.add(card);
			}
		}
		*/
		
		Solution sol = new Solution(person, room, weapon);
		return sol;
	}
	
	
	public BoardCell selectTargets() {
		BoardCell test = new BoardCell(0,0);
		return test;
	}
}
