package main;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	
	//Sets our variables if needed.
	public Room(String name, BoardCell centerCell, BoardCell labelCell) {
		this.name = name;
		this.centerCell = centerCell;
		this.labelCell = labelCell;
		
	}
	
	
}
