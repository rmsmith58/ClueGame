package clueGame;

public enum CardType {
	//9 rooms, 6 players and 6 weapons
	ROOM("Common Area", "Bathroom", "Game Room", "Library", "Office", "Dining Room", "Lounge", "Hall"), 
	PERSON("", "", "", "", "", ""), 
	WEAPON("", "", "", "", "", "");
	
	private String cardName;
	
	//Constructor that gets the card name from Card.
	CardType(String card){
		this.cardName = card;
	}
	
	//Assigns the cardName to the type of Card it is.
	public String toString() {
		return cardName;
	}
}
