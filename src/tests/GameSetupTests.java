package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Set;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;
import clueGame.Solution;

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
		assertTrue(board.getPlayers().get(5).isAI());
	}
	
	//Test to see if all the card are loaded in correctly.
	//There are 9 rooms, 6 players and 6 weapons.
	@Test
	public void allCardsLoaded() {
		ArrayList<Card> deck = board.getDeck();
		assertEquals(21, deck.size());
		for(Card card: deck) {
			assertNotNull(card.getCardName());
			assertNotNull(card.getCardType());
			if(card.getCardType() != CardType.PERSON
					&& card.getCardType() != CardType.ROOM
					&& card.getCardType() != CardType.WEAPON) {
				Assert.fail();
			}
		}
	}
	
	//Check that the Solution object has been correctly populated with one of each card type,
	//as well as assuring all players have 3 cards in their hand
	@Test
	public void handsPopulated() {
		Card[] solution = board.getSolution().getSolution();
		assertEquals(3, solution.length);
		assertEquals(CardType.PERSON, solution[0].getCardType());
		assertEquals(CardType.ROOM, solution[1].getCardType());
		assertEquals(CardType.WEAPON, solution[2].getCardType());
		
		for(Player player: board.getPlayers()) {
			assertEquals(3, player.getHand().size());
		}
	}
	
}
