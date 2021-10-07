package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiment.*;

/*
 * Tests for experimental board objects and functionality
 */
class BoardTestsExp {
	private TestBoard board;
	
	//initialize a new 4x4 board for each test
	@BeforeEach
	void setup() {
		board = new TestBoard(4, 4);
	}
	
	/*
	 * Test to ensure adjacency lists are correct for individual cells in different locations.
	 */
	@Test
	void testAdjacencyLists() {
		//test adjacency for top left corner
		Set<TestBoardCell> adjList = board.getCell(0, 0).getAdjList();
		Assert.assertTrue(adjList.contains(board.getCell(0, 1)));
		Assert.assertTrue(adjList.contains(board.getCell(1, 0)));

		//test adjacency for bottom right corner
		adjList = board.getCell(3, 3).getAdjList();
		Assert.assertTrue(adjList.contains(board.getCell(2, 3)));
		Assert.assertTrue(adjList.contains(board.getCell(3, 2)));

		//test adjacency for right edge
		adjList = board.getCell(2, 3).getAdjList();
		Assert.assertTrue(adjList.contains(board.getCell(2, 2)));
		Assert.assertTrue(adjList.contains(board.getCell(1, 3)));
		Assert.assertTrue(adjList.contains(board.getCell(3, 3)));

		//test adjacency for left edge
		adjList = board.getCell(2, 0).getAdjList();
		Assert.assertTrue(adjList.contains(board.getCell(1, 0)));
		Assert.assertTrue(adjList.contains(board.getCell(2, 1)));
		Assert.assertTrue(adjList.contains(board.getCell(3, 0)));
	}
	
	//Test to ensure the generated target list for a move of length 2 starting from (0,0) is correct
	//with no occupied spaces or spaces marked as being in a room.
	@Test
	void testTargetsEmptyTwo() {
		//sets up the game board
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targets = board.getTargets();

		//tests all valid target spots.
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
	}
	
	//Test to ensure the generated target list for a move of length 3 starting from (0,0) is correct
	//with no occupied spaces or spaces marked as being in a room.
	@Test
	void testTargetsEmptyThree() {
		//sets up the game board
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();

		//tests all valid target spots.
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(3, 0)));
		Assert.assertTrue(targets.contains(board.getCell(2, 1)));
		Assert.assertTrue(targets.contains(board.getCell(0, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(0, 3)));
		Assert.assertTrue(targets.contains(board.getCell(1, 0)));
	}
	
	//Test to ensure target list for a move of length 2 starting from (0,0) is correct
	//with an occupied space at (1,1).
	@Test
	void testTargetOccupied() {
		//sets up the game board
		TestBoardCell cell = board.getCell(0, 0);
		board.getCell(1, 1).setOccupied(true);	//sets an occupied space
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targets = board.getTargets();

		//test all valid targets
		Assert.assertEquals(2, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
	}
	
	//Test to ensure target list for a move of length 2 starting from (0,0) is correct
	//with a space marked as a room at (1,0).
	@Test
	void testTargetRoom() {
		//sets up the game board
		TestBoardCell cell = board.getCell(0, 0);
		board.getCell(1, 0).setRoom(true);	//sets a space in a room
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targets = board.getTargets();

		//test all valid targets
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 0)));
	}
	
	//Test to ensure target list for a move of length 2 starting from (0,0) is correct
	//with a space marked as a room at (1,0) and an occupied space at (1,1).
	@Test
	void testTargetRoomAndOccupied() {
		//sets up the game board
		TestBoardCell cell = board.getCell(0, 0);
		board.getCell(1, 0).setRoom(true);	//sets a space in a room
		board.getCell(1, 1).setOccupied(true); //sets an occupied space
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targets = board.getTargets();

		//test all valid targets
		Assert.assertEquals(2, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 0)));
	}
}
