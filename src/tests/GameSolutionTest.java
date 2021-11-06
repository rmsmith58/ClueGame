package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Solution;

class GameSolutionTest {
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("BoardLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}
	
	@AfterAll
	public static void reset() {
		//reset board data to avoid data contamination for other tests
		board.resetBoard();
	}
	
	//test various combinations of correct and incorrect accusations to verify Board.checkAccusation is working correctly
	@Test
	void checkAccusations() {
		Solution testAnswer = new Solution();
		Card room = new Card("Room", CardType.ROOM);
		Card weapon = new Card("Weapon", CardType.WEAPON);
		Card person = new Card("Person", CardType.PERSON);
		testAnswer.setSolution(person, room, weapon);
		board.setSolution(testAnswer);
		
		Card newRoom = new Card("NewRoom", CardType.ROOM);
		Card newWeapon = new Card("NewWeapon", CardType.WEAPON);
		Card newPerson = new Card("NewPerson", CardType.PERSON);
		
		assertTrue(board.checkAccusation(person, room, weapon));
		assertFalse(board.checkAccusation(newPerson, room, weapon));
		assertFalse(board.checkAccusation(person, newRoom, weapon));
		assertFalse(board.checkAccusation(person, room, newWeapon));
		assertFalse(board.checkAccusation(newPerson, newRoom, newWeapon));	
	}
	
	//Tests to verify Player.disproveSuggestion is working correctly
	@Test
	void disproveSuggestion() {
		Card room1 = new Card("Lounge", CardType.ROOM);
		Card room2 = new Card("Kitchen", CardType.ROOM);
		Card weapon1 = new Card("Baseball Bat", CardType.WEAPON);
		Card weapon2 = new Card("Revolver", CardType.WEAPON); 
		Card person1 = new Card("Mr. Smith", CardType.PERSON);
		Card person2 = new Card("Ms. Sherwood", CardType.PERSON);
		
		ComputerPlayer player = new ComputerPlayer("AI", Color.red, 0, 0, board);
		player.updateHand(person2);
		player.updateHand(room2);
		player.updateHand(weapon2);
		
		//case if player has no matching cards, should return null
		assertEquals(null, player.disproveSuggestion(new Solution(person1, room1, weapon1)));
		
		//case if player has 1 matching card, should return the matching card
		assertEquals(person2, player.disproveSuggestion(new Solution(person2, room1, weapon1)));
		assertEquals(room2, player.disproveSuggestion(new Solution(person1, room2, weapon1)));
		assertEquals(weapon2, player.disproveSuggestion(new Solution(person1, room1, weapon2)));
		
		//case if player has more than 1 matching card, should return one of the matching cards
		if(player.disproveSuggestion(new Solution(person2, room2, weapon1)) != person2 
				&& player.disproveSuggestion(new Solution(person2, room2, weapon1)) != room2) {
			Assert.fail();
		}	
	}
	
	//Tests to verify Board.handleSuggestion is working correctly
	@Test
	void handleSuggestions() {
		Card room1 = new Card("Lounge", CardType.ROOM);
		Card room2 = new Card("Kitchen", CardType.ROOM);
		Card room3 = new Card("Study", CardType.ROOM);
		Card weapon1 = new Card("Baseball Bat", CardType.WEAPON);
		Card weapon2 = new Card("Revolver", CardType.WEAPON);
		Card weapon3 = new Card("Knife", CardType.WEAPON);
		Card person1 = new Card("Mr. Smith", CardType.PERSON);
		Card person3 = new Card("Flint Lockwood", CardType.PERSON);
		
		ComputerPlayer accuser = new ComputerPlayer("accuser", Color.red, 0, 0, board);
		ComputerPlayer player1 = new ComputerPlayer("player1", Color.blue, 0, 0, board);
		ComputerPlayer player2 = new ComputerPlayer("player2", Color.green, 0, 0, board);
		
		accuser.updateHand(person1);
		accuser.updateHand(room1);
		accuser.updateHand(weapon1);
		
		player1.updateHand(room2);
		player2.updateHand(weapon2);
		
		board.resetBoard();
		board.addPlayer(accuser);
		board.addPlayer(player1);
		board.addPlayer(player2);
		
		//case where no player can disprove suggestion, expected return null
		assertEquals(null, board.handleSuggestion(accuser, new Solution(person3, room3, weapon3)));
		
		//case where only the accuser can disprove suggestion, expected return null
		assertEquals(null, board.handleSuggestion(accuser, new Solution(person1, room3, weapon3)));
		
		//case where only player1 can disprove suggestion, expected return player1's card
		assertEquals(room2, board.handleSuggestion(accuser, new Solution(person3, room2, weapon3)));
		
		//case where only player2 can disprove suggestion, expected return player2's card
		assertEquals(weapon2, board.handleSuggestion(accuser, new Solution(person3, room3, weapon2)));
		
		//case where player1 and player2 can disprove suggestion, expected return is player1's card (player 2 should not be reached)
		assertEquals(room2, board.handleSuggestion(accuser, new Solution(person3, room2, weapon2)));
	}

}