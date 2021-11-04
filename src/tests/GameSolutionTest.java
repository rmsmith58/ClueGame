package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
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
		
	}
	
	//Tests to verify Board.handleSuggestion is working correctly
	@Test
	void 

}
