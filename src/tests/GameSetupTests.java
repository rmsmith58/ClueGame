package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

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
	public void allPlayers() {
		
	}
	
	//Test to make sure the human player is loaded in correctly.
	//It checks their location, color, and name.
	@Test
	public void humanPlayer() {
		
	}
	
	//Test to see if all the card are loaded in correctly.
	//There are 9 rooms, 6 players and 6 weapons.
	@Test
	public void allCards() {
		
	}
	
	
}
