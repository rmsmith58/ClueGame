package clueGame;

import java.awt.Color;

/**
 * ComputerPlayer: extends Player, class for human player in game. Includes a constructor to correctly set
 * the isAI flag.
 * 
 * @author Ryne Smith
 * @author Mikayla Sherwood
 *
 */
public class ComputerPlayer extends Player {
	public ComputerPlayer(String name, Color color, int rowLocation, int colLocation) {
		super(name, color, rowLocation, colLocation);
		this.isAI = true;
	}
	
	public Soltuion createSuggestion() {
		//If the player is in a room in need to create a suggestion.
		//The weapon and person is randomly chosen out of the unseen list, and the room is the room the player is in.
		BoardCell location = new BoardCell(player.getRowLocation(), player.getcolLocation());
		if(location.isRoomCenter()) {
			
		}
		
	}
	
	public BoardCell selectTargets() {
		
	}
}
