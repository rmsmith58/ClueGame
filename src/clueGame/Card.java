package clueGame;

import clueGame.Board;
import clueGame.BoardCell;

public class Card {
	private String cardName;
	private CardType card;
	
	//Constructor that is taking in the cardName and CardType.
	public Card(String cardName, CardType card) {
		super();
		this.cardName = cardName;
		this.card = card;
	}
	
	//
	public boolean equals(BoardCell target, CardType card) {
		return true;
	}
}
