package clueGame;

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

	//constructor with only name parameter
	public Room(String name) {
		this.name = name;
	}

	//constructor with all parameters
	public Room(String name, BoardCell centerCell, BoardCell labelCell) {
		this.name = name;
		this.centerCell = centerCell;
		this.labelCell = labelCell;	
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
	
	
}
