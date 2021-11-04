package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Set;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Solution;


//Check the computer's decision making.
public class ComputerAITest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;

	@BeforeEach
	public void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("BoardLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();

		// Creates the test solution deck
		Solution test = new Solution();
		Card room = new Card("Bathroom", CardType.ROOM);
		Card person = new Card("Phil Hopkins", CardType.PERSON);
		Card weapon = new Card("Cyanide", CardType.WEAPON);
		test.setSolution(person, room, weapon);
	}

	@AfterEach
	public void reset() {
		//We reset the board to not add on additional Players or Cards.
		board.resetBoard();
	}

	//Test the logic within selectTarget in the computer class.
	//Having trouble on how to implement test when things are random.
	@Test
	public void selectTargets() {
		//test when the player is on a walkway
		ComputerPlayer player = new ComputerPlayer("AI", Color.red, 11, 16);
		Set<BoardCell> targets = null;
		for(int i = 0; i < 100; i++) {
			targets.add(player.selectTargets());
		}

		assertTrue(targets.contains(board.getCell(10, 16)));
		assertTrue(targets.contains(board.getCell(11, 15)));
		assertTrue(targets.contains(board.getCell(11, 17)));

		//test when he player is near a door to a room in their seen list.
		player.setLocation(8, 5);
		Card testRoom = new Card("Office", CardType.ROOM);
		player.updateHand(testRoom);

		for(int i = 0; i < 100; i++) {
			targets.add(player.selectTargets());
		}

		assertTrue(targets.contains(board.getCell(7, 5)));
		assertTrue(targets.contains(board.getCell(8, 6)));
		assertFalse(targets.contains(board.getCell(11, 2)));
	}

	//Tests the logic on how a computer creates the suggestion.
	@Test
	public void createSuggestion() {
		//Checks the suggestions in the Game Room
		ComputerPlayer player = new ComputerPlayer("AI", Color.red, 2, 2); 
		ArrayList<Card> unseen = board.getDeck();
		unseen.removeAll(player.getHand());
		
		//Holds all the suggestions created and doesn't duplicate.
		Set<Solution> suggestions;

		//randomly generates suggestions.
		for(int i = 0; i < 100; i++) {
			suggestions.add(player.createSuggestion());	
		}

		//Only one room card the player can only make a suggestion in the room they are in.
		Card room = new Card("Game Room", CardType.ROOM);
		Card person1 = new Card("Lisa Johnson", CardType.PERSON);
		Card person2 = new Card("Judy Edwards", CardType.PERSON);
		Card weapon1 = new Card("Axe", CardType.WEAPON);
		Card weapon2 = new Card("Cyanide", CardType.PERSON);
		
		//Creates random solutions from the created card.
		Solution sol1 = new Solution(person1, weapon1, room);
		Solution sol2= new Solution(person1, weapon2, room);
		Solution sol3= new Solution(person2, weapon1, room);
		Solution sol4= new Solution(person2, weapon2, room);

		//Checks to see if these random suggestions were made.
		assertTrue(suggestions.contains(sol1));
		assertTrue(suggestions.contains(sol2));
		assertTrue(suggestions.contains(sol3));
		assertTrue(suggestions.contains(sol4));

		//Checks the suggestions in the Lounge
		player.setLocation(2, 10); 
		unseen = board.getDeck();
		unseen.removeAll(player.getHand());
		
		//Clears to get rid of old suggestions.
		suggestions.clear();

		//randomly generates suggestions.
		for(int i = 0; i < 100; i++) {
			suggestions.add(player.createSuggestion());	
		}

		//Only one room card the player can only make a suggestion in the room they are in.
		Card room2 = new Card("Lounge", CardType.ROOM);
		
		//Creates random solutions from the created card.
		Solution solt1 = new Solution(person1, weapon1, room2);
		Solution solt2= new Solution(person1, weapon2, room2);
		Solution solt3= new Solution(person2, weapon1, room2);
		Solution solt4= new Solution(person2, weapon2, room2);

		//Checks to see if these random suggestions were made.
		assertTrue(suggestions.contains(solt1));
		assertTrue(suggestions.contains(solt2));
		assertTrue(suggestions.contains(solt3));
		assertTrue(suggestions.contains(solt4));
	}
}
