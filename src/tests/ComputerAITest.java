package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
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
import clueGame.Room;
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
	//Hard code the unseen, seenRooms, and the target list.
	@Test
	public void selectTargets() {
		//test when the player is on a walkway
		ComputerPlayer player = new ComputerPlayer("AI", Color.red, 11, 16, board);
		Set<BoardCell> targets = new HashSet<BoardCell>();

		//creates the unseen list.
		ArrayList <Card> tempUnseen = new ArrayList<Card>();
		Card room1 = new Card("Bathroom", CardType.ROOM);
		Card room2 = new Card("Hall", CardType.ROOM);
		Card person1 = new Card("Phil Hopkins", CardType.ROOM);
		Card person2 = new Card("Judy Edwards", CardType.ROOM);
		Card person3 = new Card("You", CardType.ROOM);
		Card weapon1 = new Card("Cyanide", CardType.ROOM);
		Card weapon2 = new Card("Axe", CardType.ROOM);
		Card weapon3 = new Card("Steak Knife", CardType.ROOM);

		tempUnseen.add(room1);
		tempUnseen.add(room2);
		tempUnseen.add(person1);
		tempUnseen.add(person2);
		tempUnseen.add(person3);
		tempUnseen.add(weapon1);
		tempUnseen.add(weapon2);
		tempUnseen.add(weapon3);
		
		player.setUnseen(tempUnseen);
		
		//create the seenRoom list.
		ArrayList<Room> seenRoom = new ArrayList<Room>();
		Room sRoom1 = new Room("Dining Room");
		Room sRoom2 = new Room("Lounge");
		Room sRoom3 = new Room("Library");
		Room sRoom4 = new Room("Game Room");
		Room sRoom5 = new Room("Kitchen");
		Room sRoom6 = new Room("Common Area");
		
		seenRoom.add(sRoom1);
		seenRoom.add(sRoom2);
		seenRoom.add(sRoom3);
		seenRoom.add(sRoom4);
		seenRoom.add(sRoom5);
		seenRoom.add(sRoom6);
		
		
		
		//creates the target list;
		Set<BoardCell> testTarget = new HashSet<BoardCell>();
		BoardCell target1 = new BoardCell(10, 16);
		BoardCell target2 = new BoardCell(11, 15);
		BoardCell target3 = new BoardCell(11, 17);
		BoardCell target4 = new BoardCell(12, 16);
		
		testTarget.add(target1);
		testTarget.add(target2);
		testTarget.add(target3);
		testTarget.add(target4);

		assertTrue(targets.contains(board.getCell(10, 16)));
		assertTrue(targets.contains(board.getCell(11, 15)));
		assertTrue(targets.contains(board.getCell(11, 17)));

		//test when he player is near a door to a room in their seen list.
		player.setLocation(8, 5);
		Room testRoom = new Room("Office");
		seenRoom.add(testRoom);
		
		assertTrue(targets.contains(board.getCell(7, 5)));
		assertTrue(targets.contains(board.getCell(8, 6)));
		assertFalse(targets.contains(board.getCell(11, 2)));
	}

	//Tests the logic on how a computer creates the suggestion.
	//Hard code unseen.
	@Test
	public void createSuggestion() {
		//Checks the suggestions in the Game Room
		ComputerPlayer player = new ComputerPlayer("AI", Color.red, 2, 2, board); 

		//creates the unseen list.
		ArrayList <Card> tempUnseen = new ArrayList<Card>();
		Card room1 = new Card("Bathroom", CardType.ROOM);
		Card room2 = new Card("Hall", CardType.ROOM);
		Card person1 = new Card("Phil Hopkins", CardType.ROOM);
		Card person2 = new Card("Judy Edwards", CardType.ROOM);
		Card person3 = new Card("You", CardType.ROOM);
		Card weapon1 = new Card("Cyanide", CardType.ROOM);
		Card weapon2 = new Card("Axe", CardType.ROOM);
		Card weapon3 = new Card("Steak Knife", CardType.ROOM);

		tempUnseen.add(room1);
		tempUnseen.add(room2);
		tempUnseen.add(person1);
		tempUnseen.add(person2);
		tempUnseen.add(person3);
		tempUnseen.add(weapon1);
		tempUnseen.add(weapon2);
		tempUnseen.add(weapon3);
		
		player.setUnseen(tempUnseen);
		
		//Holds all the suggestions created and doesn't duplicate.
		Set<Solution> suggestions = new HashSet<Solution>();

		//randomly generates suggestions.
		for(int i = 0; i < 100; i++) {
			suggestions.add(player.createSuggestion());	
		}

		//Only one room card the player can only make a suggestion in the room they are in.
		Card roomSug = new Card("Game Room", CardType.ROOM);
		Card person1Sug = new Card("Lisa Johnson", CardType.PERSON);
		Card person2Sug = new Card("Judy Edwards", CardType.PERSON);
		Card weapon1Sug = new Card("Axe", CardType.WEAPON);
		Card weapon2Sug = new Card("Cyanide", CardType.PERSON);

		//Creates random solutions from the created card.
		Solution sol1 = new Solution(person1Sug, weapon1Sug, roomSug);
		Solution sol2= new Solution(person1Sug, weapon2Sug, roomSug);
		Solution sol3= new Solution(person2Sug, weapon1Sug, roomSug);
		Solution sol4= new Solution(person2Sug, weapon2Sug, roomSug);

		//Checks to see if these random suggestions were made.
		assertTrue(suggestions.contains(sol1));
		assertTrue(suggestions.contains(sol2));
		assertTrue(suggestions.contains(sol3));
		assertTrue(suggestions.contains(sol4));

		//Checks the suggestions in the Lounge
		player.setLocation(2, 10); 

		//Clears to get rid of old suggestions.
		suggestions.clear();

		//randomly generates suggestions.
		for(int i = 0; i < 100; i++) {
			suggestions.add(player.createSuggestion());	
		}

		//Only one room card the player can only make a suggestion in the room they are in.
		Card room2Sug = new Card("Lounge", CardType.ROOM);

		//Creates random solutions from the created card.
		Solution solt1 = new Solution(person1Sug, weapon1Sug, room2Sug);
		Solution solt2= new Solution(person1Sug, weapon2Sug, room2Sug);
		Solution solt3= new Solution(person2Sug, weapon1Sug, room2Sug);
		Solution solt4= new Solution(person2Sug, weapon2Sug, room2Sug);

		//Checks to see if these random suggestions were made.
		assertTrue(suggestions.contains(solt1));
		assertTrue(suggestions.contains(solt2));
		assertTrue(suggestions.contains(solt3));
		assertTrue(suggestions.contains(solt4));
	}
}
