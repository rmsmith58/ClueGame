package main;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private Boolean isInRoom, isOccupied, roomLabel, roomCenter;
	//list of adjacent TestBoardCell objects
	private Set<BoardCell> adjList;
	private int row, col;
	private char initial, secretPassage;
	private DoorDirection doorDirection;
	
	/**
	 * Public constructor, creates cell at specified
	 * row and column location.
	 * 
	 * @param row
	 * @param col
	 */
	public BoardCell(int row, int col) {
		this.row = row;
		this.col = col;
		this.adjList = new HashSet<BoardCell>();
		this.isOccupied = false;
		this.isInRoom = false;
	}
	
	/**
	 * Adds a TestBoardCell object to the adjacency list
	 * for this cell.
	 * 
	 * @param cell
	 */
	public void addAdj(BoardCell cell) {
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
	
	public Set<BoardCell> getAdjList(){
		return this.adjList;
	}
}
