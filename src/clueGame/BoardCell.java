package clueGame;

import java.util.HashSet;
import java.util.Set;

/**
 * BoardCell: contains information for a single cell on the game board, including row and column location. 
 * Also holds details for room center, secret passages, door direction, and room/occupied flags. Uses a set
 * of other BoardCell objects to track adjacent cells.
 * 
 * @author Ryne Smith
 * @author Mikayla Sherwood
 *
 */
public class BoardCell {
	private Boolean isInRoom, isOccupied, roomLabel, roomCenter, doorway;
	//list of adjacent TestBoardCell objects
	private Set<BoardCell> adjList;
	private int row, col;
	private char initial, secretPassage;
	private DoorDirection doorDirection;

	/**
	 * Public constructor, creates cell at specified
	 * row and column location. All boolean values default to false.
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
		this.doorway = false;
		this.roomLabel = false;
		this.roomCenter = false;
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
	
	public void setRoomLabel(Boolean roomLabel) {
		this.roomLabel = roomLabel;
	}

	public void setRoomCenter(Boolean roomCenter) {
		this.roomCenter = roomCenter;
	}
	
	public char getInitial() {
		return initial;
	}

	public void setInitial(char initial) {
		this.initial = initial;
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
	
	public char getSecretPassage() {
		return secretPassage;
	}
	
	public void setSecretPassage(char destination) {
		this.secretPassage = destination;
	}

	public Set<BoardCell> getAdjList(){
		return this.adjList;
	}
	
	public DoorDirection getDoorDirection() {
		return this.doorDirection;
	}
	
	public void setDoorDirection(DoorDirection dir) {
		this.doorDirection = dir;
	}
	
	public Boolean isDoorway() {
		return this.doorway;
	}
	
	public void setDoorway(Boolean door) {
		this.doorway = door;
	}
	
	public Boolean isLabel() {
		return roomLabel;
	}

	public Boolean isRoomCenter() {
		return roomCenter;
	}
}
