package experiment;

import java.util.Collections;
import java.util.Set;

public class TestBoardCell {
	private Boolean isInRoom;
	private Boolean isOccupied;
	private Set<TestBoardCell> adjList;
	
	public TestBoardCell(int row, int col) {
		this.adjList = Collections.<TestBoardCell>emptySet();
		//TODO do something
	}
	
	public void addAdjacency(TestBoardCell cell) {
		//TODO do something
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
