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
	private ArrayList<Card> unseen;
	public ComputerPlayer(String name, Color color, int rowLocation, int colLocation) {
		super(name, color, rowLocation, colLocation);
		this.isAI = true;
	}
	
	private void setUnseen(ArrayList<Card> unseen) {
		this.unseen = unseen;
	}

	public Solution createSuggestion() {
		//If the player is in a room in need to create a suggestion.
		//The weapon and person is randomly chosen out of the unseen list, and the room is the room the player is in.
		Card room = new Card("Lounge", CardType.ROOM);	//Figure out how to get the name of the room.
		
		//Creates a array list of an unseen deck, and then separates that deck into people and weapons.
		ArrayList<Card> unseen = deck;  //Figure out how to access the deck;
		unseen.removeAll(seen);
		ArrayList<Card> people;
		ArrayList<Card> weapons;
		
		//Separates the unseen cards into people and weapons.
		//We only want these items because a suggestion room can only be the room the player is in.
		for(Card card: unseen) {
			if(card.getCardType() == CardType.PERSON) {
				people.add(card);
			}
			else if(card.getCardType() == CardType.WEAPON) {
				weapons.add(card);
			}
		}
		
		//Creates a random number for picking the cards.
		Random rand = new Random();
		int peopleIndex = rand.nextInt(people.size());
		int weaponIndex = rand.nextInt(weapons.size());
		
		//Sets the cards for the solution.
		Card person = people.get(peopleIndex);
		Card weapon = weapons.get(weaponIndex);
		
		//Creates the solution.
		Solution sol = new Solution(person, room, weapon);
		
		return sol;
	}
	
	
	public BoardCell selectTargets() {
		BoardCell location = new BoardCell(row, column);
		if(location.isRoomCenter()) {
			
		}
		
		return location;
	}
}
