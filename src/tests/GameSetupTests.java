package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Player;

public class GameSetupTests {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
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
	
	//Test to see if all the players are loaded correctly.
	//It checks for 6 players, with 1 human and 5 computer players, some of the players names, color, and location.
	@Test
	public void allPlayersLoaded() {
		assertEquals(6, board.getPlayers().size());
		//Checking fields for several players
		//players should be populated in order from the setup config so we can access by index
		assertFalse(board.getPlayers().get(0).isAI());
		assertEquals(11, board.getPlayers().get(1).getRow());
		assertEquals(6, board.getPlayers().get(2).getColumn());
		assertEquals("Judy Edwards", board.getPlayers().get(3).getName());
		assertEquals(Color.yellow, board.getPlayers().get(4).getColor());
	}
	
	//Test to make sure the human player is loaded in correctly.
	//It checks their location, color, and name.
	@Test
	public void humanPlayerLoaded() {
		
	}
	
	//Test a computer player to make sure it is loaded correctly
	@Test
	public void compPlayerLoaded() {
		
	}
	
	//Test to see if all the card are loaded in correctly.
	//There are 9 rooms, 6 players and 6 weapons.
	@Test
	public void allCardsLoaded() {
		
	}
	
	//Check that the Solution object has been correctly populated with one of each card type
	@Test
	public void answerPopulated() {
		
	}
	
}
