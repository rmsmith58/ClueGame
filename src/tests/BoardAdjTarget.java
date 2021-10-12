package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTarget {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms() {
		// we want to test a couple of different rooms.
		// First, the Lounge that only has a single door
		Set<BoardCell> testList = board.getAdjList(2, 10);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(6, 10)));

		// Second, the Office that only has a single door but a secret room
		Set<BoardCell> testList = board.getAdjList(11, 2);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(8, 5)));
		assertTrue(testList.contains(board.getCell(9, 21)));

		// Lastly, the Bathroom that only has a single door
		testList = board.getAdjList(20, 11);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(19, 17)));
	}

	// Ensure door locations include their rooms and also additional walkways
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacencyDoor() {
		Set<BoardCell> testList = board.getAdjList(6, 5);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(2, 2)));
		assertTrue(testList.contains(board.getCell(6, 4)));
		assertTrue(testList.contains(board.getCell(7, 5)));
		assertTrue(testList.contains(board.getCell(6, 6)));

		testList = board.getAdjList(6, 16);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(6, 15)));
		assertTrue(testList.contains(board.getCell(7, 16)));
		assertTrue(testList.contains(board.getCell(6, 17)));
		assertTrue(testList.contains(board.getCell(3, 20)));

		testList = board.getAdjList(12, 18);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(9, 21)));
		assertTrue(testList.contains(board.getCell(11, 18)));
		assertTrue(testList.contains(board.getCell(12, 17)));
		assertTrue(testList.contains(board.getCell(13, 18)));
	}

	// Test a variety of walkway scenarios
	// These tests are Dark Orange on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways() {
		// Test next to room
		testList = board.getAdjList(17,21);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(18, 21)));
		assertTrue(testList.contains(board.getCell(18, 22)));
		assertTrue(testList.contains(board.getCell(17, 23)));

		// Test near a door but not adjacent
		testList = board.getAdjList(9, 6);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(8, 6)));
		assertTrue(testList.contains(board.getCell(9, 7)));
		assertTrue(testList.contains(board.getCell(10, 6)));

		// Test adjacent to walkways
		testList = board.getAdjList(19, 16);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(18, 16)));
		assertTrue(testList.contains(board.getCell(19, 17)));
		assertTrue(testList.contains(board.getCell(20, 16)));

		// Test the edge of the board in a narrow walkway.
		testList = board.getAdjList(6, 23);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(6, 22)));

	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsInDiningRoom() {
		// test a roll of 1
		board.calcTargets(board.getCell(21, 3), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(18, 5)));	

		// test a roll of 3
		board.calcTargets(board.getCell(21, 3), 3);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(16, 5)));
		assertTrue(targets.contains(board.getCell(18, 7)));	
		assertTrue(targets.contains(board.getCell(17, 4)));	

		// test a roll of 4
		board.calcTargets(board.getCell(12, 20), 4);
		targets= board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(15, 5)));
		assertTrue(targets.contains(board.getCell(19, 7)));	
		assertTrue(targets.contains(board.getCell(17, 3)));
		assertTrue(targets.contains(board.getCell(17, 7)));
	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsInCommonArea() {

	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsInWalkway() {

	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {

	}

	// Test to make sure occupied locations do not cause problems
	// These are RED on the planning spreadsheet
	@Test
	public void testTargetsOccupied() {

	}

}
