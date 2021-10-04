package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiment.*;

class BoardTestsExp {
	private TestBoard board;
	
	@BeforeEach
	void setup() {
		board = new TestBoard(4, 4);
	}
	
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

}
