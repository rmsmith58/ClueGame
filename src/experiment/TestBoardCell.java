package experiment;

import java.util.Collections;
import java.util.*;

/**
 * TestBoardCell: experimental class for a single cell on a game board. Includes flags for
 * marking a cell as occupied or in a room and holds a set of adjacent cells, and getter/setter functions.
 * 
 * @author Ryne Smith and Mikayla Sherwood
 *
 */
public class TestBoardCell {
	private Boolean isInRoom;
	private Boolean isOccupied;
	private Set<TestBoardCell> adjList;;
	private int row, col;
	
	public TestBoardCell(int row, int col) {
		this.row = row;
		this.col = col;
		this.adjList = new HashSet<TestBoardCell>();
	}
	
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
