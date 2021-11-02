package clueGame;
import java.util.ArrayList;

public enum CardType {
	//9 rooms, 6 players and 6 weapons
	ROOM("ROOM"), 
	PERSON("PERSON"), 
	WEAPON("WEAPON");
	
	private String cardName;
	private ArrayList<String> rooms;
	private ArrayList<String> people;
	private ArrayList<String> weapons;
	
	
	//Constructor that gets the card name from Card.
	CardType(String card){
		this.cardName = card;
	}
	
	//Assigns the cardName to the type of Card it is.
	public String toString() {
		return cardName;
	}
}
