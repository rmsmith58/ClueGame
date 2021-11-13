package clueGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
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
	private Color roomColor;

	//constructor with only name parameter
	public Room(String name) {
		this.name = name;
		this.doorways = new HashSet<BoardCell>();
		this.hasSecretPassage = false;
	}

	//constructor with all parameters
	public Room(String name, BoardCell centerCell, BoardCell labelCell, Color color) {
		this.name = name;
		this.centerCell = centerCell;
		this.labelCell = labelCell;	
		this.doorways = new HashSet<BoardCell>();
		this.hasSecretPassage = false;
		this.roomColor = color;
	}
	
	public void drawRoomLabel(Graphics g, int y, int x, int width) {
		g.setColor(Color.white);
		g.drawString(this.name, x*width, y*width);
	}
	
	public void drawDoorways(Graphics g, int width, int offset) {
		for(BoardCell d: this.getDoorways()) {
			g.setColor(Color.cyan);
			switch(d.getDoorDirection()) {
				case UP:
					g.drawLine(d.getCol()*width, d.getRow()*width-offset, d.getCol()*width+width, d.getRow()*width-offset);
					break;
				case DOWN:
					g.drawLine(d.getCol()*width, d.getRow()*width+width+offset, d.getCol()*width+width, d.getRow()*width+width+offset);
					break;
				case LEFT:
					g.drawLine(d.getCol()*width-offset, d.getRow()*width, d.getCol()*width-offset, d.getRow()*width+width);
					break;
				case RIGHT:
					g.drawLine(d.getCol()*width+width+offset, d.getRow()*width, d.getCol()*width+width+offset, d.getRow()*width+width);
					break;
			}
		}
	}

	public Color getRoomColor() {
		return roomColor;
	}

	public void setRoomColor(Color roomColor) {
		this.roomColor = roomColor;
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
