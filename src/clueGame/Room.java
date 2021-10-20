package clueGame;

import java.util.HashSet;
import java.util.Set;

/**
 * Room: contains information for a room identified in the config files, including room name and
 * pointers to the BoardCell objects acting as room label and center locations.
 * 
 * @author Ryne Smith
 * @author Mikayla Sherwood
 *
 */
public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private Set<BoardCell> doorways;
	private Boolean hasSecretPassage;
	private char secretPassageDestinationInitial;

	//constructor with only name parameter
	public Room(String name) {
		this.name = name;
		this.doorways = new HashSet<BoardCell>();
		this.hasSecretPassage = false;
	}

	//constructor with all parameters
	public Room(String name, BoardCell centerCell, BoardCell labelCell) {
		this.name = name;
		this.centerCell = centerCell;
		this.labelCell = labelCell;	
		this.doorways = new HashSet<BoardCell>();
		this.hasSecretPassage = false;
	}

	public String getName() {
		return name;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}
	

	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}
	
	public void addDoorway(BoardCell doorway) {
		this.doorways.add(doorway);
	}
	
	public Set<BoardCell> getDoorways(){
		return this.doorways;
	}

	public char getSecretPassageDestinationInitial() {
		return secretPassageDestinationInitial;
	}

	public void setSecretPassageDestinationInitial(char secretPassageDestinationInitial) {
		this.secretPassageDestinationInitial = secretPassageDestinationInitial;
	}

	public Boolean hasSecretPassage() {
		return hasSecretPassage;
	}

	public void setHasSecretPassage(Boolean hasSecretPassage) {
		this.hasSecretPassage = hasSecretPassage;
	}
	
	
}
