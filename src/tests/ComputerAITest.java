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
		
		/* Not sure if I will need this. I really need to read first.
		// Creates the soltuion deck
		Solution test = new Solution();
		Card room = new Card("Bathroom", CardType.ROOM);
		Card person = new Card("Phil Hopkins", CardType.PERSON);
		Card weapon = new Card("Cyanide", CardType.WEAPON);
		test.setSolution(person, room, weapon);
		
		//Creates test card in the AI hand.
		//Set AI computer to Dale Jenkins.
		
		Card judyCard = new Card("Judy Edwards", CardType.PERSON);
		Card hallCard = new Card("Hall", CardType.ROOM);
		Card axeCard = new Card("Axe", CardType.WEAPON);
		board.getPlayers().
		*/
		//Creates test card in the Players hand.
		
	}

	@AfterEach
	public void reset() {
		//We reset the board to not add on additional Players or Cards.
		board.resetBoard();
	}
	
	//Test the logic within selectTarget in the computer class.
	@Test
	public void selectTargets() {
		
		
	}
	
	//Tests the logic on how a computer creates the suggestion.
	@Test
	public void createSuggestion() {
		
	}
}
