package main;

public enum DoorDirection {
	//These are the options for our enum type, not sure whether they would take in a string or not
	//but still accounted for it.
	UP("Up"), DOWN("Down"), LEFT("Left"), RIGHT("Right"), NONE("None");
	private String direction;
	
	//This would use the direction the user wants to go and then interpret which enum type to use.
	DoorDirection(String value){
		direction = value;
	}
}
