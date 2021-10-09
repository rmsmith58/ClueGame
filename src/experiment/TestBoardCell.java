package experiment;

import java.util.*;

/**
 * TestBoardCell: experimental class for a single cell on a game board. Includes flags for
 * marking a cell as occupied or in a room and holds a set of adjacent cells, and getter/setter functions.
 * 
 * @author Ryne Smith
 * @author Mikayla Sherwood
 *
 */
public class TestBoardCell {
	private Boolean isInRoom;
	private Boolean isOccupied;
	//list of adjacent TestBoardCell objects
	private Set<TestBoardCell> adjList;;
	private int row, col;
	
	/**
	 * Public constructor, creates cell at specified
	 * row and column location.
	 * 
	 * @param row
	 * @param col
	 */
	public TestBoardCell(int row, int col) {
		this.row = row;
		this.col = col;
		this.adjList = new HashSet<TestBoardCell>();
		this.isOccupied = false;
		this.isInRoom = false;
	}
	
	/**
	 * Adds a TestBoardCell object to the adjacency list
	 * for this cell.
	 * 
	 * @param cell
	 */
	public void addAdjacency(TestBoardCell cell) {
		this.adjList.add(cell);
	}

	public Boolean getRoom() {
		return isInRoom;
	}

	public void setRoom(Boolean isInRoom) {
		this.isInRoom = isInRoom;
	}

	public Boolean getOccupied() {
		return isOccupied;
	}

	public void setOccupied(Boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	
	public Set<TestBoardCell> getAdjList(){
		return this.adjList;
	}
}
